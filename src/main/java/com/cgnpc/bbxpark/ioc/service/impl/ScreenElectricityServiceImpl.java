package com.cgnpc.bbxpark.ioc.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.energy.domain.*;
import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordBranchCountModel;
import com.cgnpc.bbxpark.energy.dto.param.BranchEnergyFlowQueryParam;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordCountParam;
import com.cgnpc.bbxpark.energy.mapper.*;
import com.cgnpc.bbxpark.energy.service.IBranchEnergyFlowService;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordCountService;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.BranchEnergyFlowParam;
import com.cgnpc.bbxpark.ioc.service.IScreenElectricityService;
import com.cgnpc.bbxpark.property.domain.MeterReadingPlan;
import com.cgnpc.bbxpark.property.mapper.MeterReadingPlanRepository;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.domain.SpaceBasicInfo;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.mapper.ParkSpaceRepository;
import com.cgnpc.bbxpark.space.mapper.SpaceBasicInfoRepository;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderDevice;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderDeviceRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScreenElectricityServiceImpl implements IScreenElectricityService {
    @Autowired
    private EnergyBranchRepository energyBranchRepository;
    @Autowired
    private BranchDeviceRepository branchDeviceRepository;
    @Autowired
    private MeterPersonRecordRepository meterPersonRecordRepository;
    @Autowired
    private MeterPersonRecordCountRepository meterPersonRecordCountRepository;
    @Autowired
    private ParkSpaceRepository parkSpaceRepository;
    @Autowired
    private SpaceBasicInfoRepository spaceBasicInfoRepository;
    @Autowired
    private MeterReadingPlanRepository meterReadingPlanRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private WorkOrderDeviceRepository workOrderDeviceRepository;
    @Autowired
    private IocDeviceRepository iocDeviceRepository;
    @Autowired
    private EnergyAbnormalRemindRepository energyAbnormalRemindRepository;
    @Autowired
    private IMeterPersonRecordCountService meterPersonRecordCountService;
    @Autowired
    private IParkSpaceService parkSpaceService;
    @Autowired
    private IBranchEnergyFlowService branchEnergyFlowService;

    private static Long kjySpaceId = 2338L;
    private static Long kjyBranchId = 35L;

    @Override
    public ElectricityOverviewModel getElectricityOverview(Integer year, Integer month) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        Date start = DateUtil.getFirstTimeBy(year, month);
        Date end = DateUtil.getLastTimeBy(year, month);
        end = end.compareTo(new Date()) > 0 ? new Date() : end;
        // 1. 获取最上级电力支路设备ID列表
        List<Long> branchIds = getTopLevelElectricityBranchIds(tenantId);
        if (branchIds.isEmpty()) {
            return buildEmptyModel();
        }
        // 2. 获取所有设备ID
        List<Long> deviceIds = getDeviceIdsByBranchIds(branchIds);
        if (deviceIds.isEmpty()) {
            return buildEmptyModel();
        }
        // 3. 计算总用电量（KWh）
        BigDecimal totalElectricityKWh = calculateTotalElectricity(deviceIds, start, end);
        // 4. 计算总面积（㎡）
        BigDecimal totalArea = getTotalArea(tenantId);
        // 5. 计算天数（实际自然天数，而非间隔天数）
        long days = DateUtil.between(start, end, DateUnit.DAY) + 1;
        // 6. 计算单位面积日用电强度（KWh/㎡/天）
        BigDecimal dailyIntensityKWh = BigDecimal.ZERO;
        if (totalArea.compareTo(BigDecimal.ZERO) > 0) {
            dailyIntensityKWh = totalElectricityKWh.divide(totalArea.multiply(BigDecimal.valueOf(days)), 2, RoundingMode.HALF_UP);
        }
        // 7. 使用公用工具进行单位换算
        EnergyUnitUtil.EnergyValue totalEnergy = EnergyUnitUtil.formatEnergy(totalElectricityKWh);
        EnergyUnitUtil.EnergyValue intensityEnergy = EnergyUnitUtil.formatEnergy(dailyIntensityKWh);
        // 8. 组装返回对象
        ElectricityOverviewModel model = new ElectricityOverviewModel();
        model.setTotalElectricity(totalEnergy.getValue());
        model.setTotalElectricityUnit(totalEnergy.getUnit());
        model.setDailyElectricityIntensity(intensityEnergy.getValue());
        model.setDailyElectricityIntensityUnit(intensityEnergy.getUnit() + "/㎡/天");
        return model;
    }

    @Override
    public List<ElectricityTrendModel> getElectricityTrend(Integer year, Integer month) {
        MeterRecordCountParam param = new MeterRecordCountParam();
        param.setReadingType(DeviceReadingTypeEnum.ELECTRICITY.getCode());
        //电表暂时不会按日来抄,所以统计年度每月的趋势
        param.setTimeType(month == null ? "year":"month");
        //调用PC端的能耗月度趋势
        List<MeterRecordBranchCountModel> list = meterPersonRecordCountService.energyBranchCount(param,DateUtil.getFirstTimeBy(year, month),getTopLevelElectricityBranchIds(WebFrameworkUtils.getHeaderTenantId()));
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        //获取最高的数值，用于换算单位
        Double max = list.stream().flatMap(e-> Stream.of(e.getValue(),e.getLastValue()))
                .filter(Objects::nonNull).max(Double::compareTo).orElse(0.0);
        String unit = EnergyUnitUtil.getUnit(new BigDecimal(max));
        return list.stream().map(m->{
            ElectricityTrendModel model = new ElectricityTrendModel();
            model.setDate(m.getTime());
            model.setValue(EnergyUnitUtil.conversion(new BigDecimal(m.getValue()),unit));
            model.setLastValue(EnergyUnitUtil.conversion(new BigDecimal(m.getLastValue()),unit));
            model.setUnit(unit);
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ElectricityAreaCompareModel> getAreaCompare(Long id, Integer year, Integer month) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        Date start = DateUtil.getFirstTimeBy(year, month);
        Date end = DateUtil.getLastTimeBy(year, month);
        List<ElectricityAreaCompareModel> list = meterPersonRecordCountRepository.findAreaCompare(id,DeviceReadingTypeEnum.ELECTRICITY.getCode(),start,end,tenantId);
        if(CollectionUtils.isEmpty(list)){
            return list;
        }
        //获取最高的数值，用于换算单位
        BigDecimal max = list.stream().map(ElectricityAreaCompareModel::getElectricity).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        String unit = EnergyUnitUtil.getUnit(max);
        list.forEach(m->{
            m.setElectricity(EnergyUnitUtil.conversion(m.getElectricity(),unit));
            m.setUnit(unit);
        });
        return list;
    }

    @Override
    public ElectricityManageAnalysisModel getManageAnalysis(String branchType) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //抄表计划数
        Integer planCount = meterReadingPlanRepository.selectCount(Wrappers.<MeterReadingPlan>lambdaQuery()
                .eq(MeterReadingPlan::getReadingType,branchType)
                .eq(MeterReadingPlan::getStatus, Status.enabled.getKey()).eq(MeterReadingPlan::getTenantId,tenantId));
        //执行中工单数量
        int workOrderCount = 0;
        List<WorkOrder> workOrders = workOrderRepository.selectList(Wrappers.<WorkOrder>lambdaQuery().eq(WorkOrder::getSource, WorkOrderSourceEnum.METERPLAN.getCode())
                .ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode()).eq(WorkOrder::getDeleted, Delete.NORMAL.getKey())
                .eq(WorkOrder::getTenantId,tenantId).select(WorkOrder::getId));
        List<Long> orderIds = workOrders.stream().map(WorkOrder::getId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(orderIds)){
            List<WorkOrderDevice> workOrderDevices = workOrderDeviceRepository.selectList(Wrappers.<WorkOrderDevice>lambdaQuery().in(WorkOrderDevice::getWorkOrderId,orderIds)
                    .eq(WorkOrderDevice::getReadingType,branchType).select(WorkOrderDevice::getWorkOrderId));
            workOrderCount = (int)workOrderDevices.stream().map(WorkOrderDevice::getWorkOrderId).distinct().count();
        }
        //数量(启用状态)
        List<EnergyBranch> branches = energyBranchRepository.selectList(Wrappers.<EnergyBranch>lambdaQuery().eq(EnergyBranch::getBranchType,DeviceReadingTypeEnum.ELECTRICITY.getCode())
                .eq(EnergyBranch::getStatus,Status.enabled.getKey()).eq(EnergyBranch::getTenantId,tenantId).select(EnergyBranch::getId));
        List<Long> branchIds = branches.stream().map(EnergyBranch::getId).collect(Collectors.toList());
        List<BranchDevice> devices = branchDeviceRepository.selectList(Wrappers.<BranchDevice>lambdaQuery().in(BranchDevice::getBranchId,branchIds));
        List<Long> deviceIds = devices.stream().map(BranchDevice::getDeviceId).distinct().collect(Collectors.toList());
        //抄表次数
        Integer meterReadingCount = meterPersonRecordRepository.selectCount(Wrappers.<MeterPersonRecord>lambdaQuery()
                .eq(MeterPersonRecord::getReadingType,branchType).eq(MeterPersonRecord::getTenantId,tenantId).select(MeterPersonRecord::getId));
        return ElectricityManageAnalysisModel.builder().meterReadingPlanCount(planCount)
                .runningMeterReadingPlanCount(workOrderCount).meterCount(deviceIds.size())
                .meterReadingCount(meterReadingCount).build();
    }

    @Override
    public List<ElectricityRealtimeConfigModel> getRealtimeConfig(Long id,String branchType) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //先查询所有支路数据
        List<EnergyBranch> allBranches = energyBranchRepository.selectList(Wrappers.<EnergyBranch>lambdaQuery()
                        .eq(EnergyBranch::getBranchType,branchType).eq(EnergyBranch::getStatus,Status.enabled.getKey())
                        .eq(EnergyBranch::getDeleted,Delete.NORMAL.getKey()).eq(EnergyBranch::getTenantId, tenantId));
        //构建父子映射（parentId -> 子支路列表）
        Map<Long, List<EnergyBranch>> parentToChildren = allBranches.stream().filter(b -> b.getParentId() != null)
                .collect(Collectors.groupingBy(EnergyBranch::getParentId));
        //找到根节点集合（如果branchId为null，则是所有顶级支路；否则是以该节点为根的子树）
        List<EnergyBranch> roots;
        if (id == null) {
            roots = allBranches.stream().filter(b -> b.getParentId() == null).collect(Collectors.toList());
        } else {
            EnergyBranch target = allBranches.stream().filter(b -> b.getId().equals(id))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("支路不存在"));
            roots = Collections.singletonList(target);
        }
        // 获取所有支路下关联的设备ID（批量）
        List<Long> allBranchIds = allBranches.stream().map(EnergyBranch::getId).collect(Collectors.toList());
        List<BranchDevice> branchDevices = branchDeviceRepository.selectList(Wrappers.<BranchDevice>lambdaQuery()
                .in(BranchDevice::getBranchId, allBranchIds).eq(BranchDevice::getTenantId, tenantId));
        // 支路ID -> 设备ID列表
        Map<Long, List<Long>> branchIdToDeviceIds = branchDevices.stream().collect(Collectors.groupingBy(BranchDevice::getBranchId,
                        Collectors.mapping(BranchDevice::getDeviceId, Collectors.toList())));
        // 查询所有设备详情
        List<Long> allDeviceIds = branchDevices.stream().map(BranchDevice::getDeviceId).distinct().collect(Collectors.toList());
        List<IocDevice> devices = iocDeviceRepository.selectList(Wrappers.<IocDevice>lambdaQuery().in(IocDevice::getId, allDeviceIds));
        Map<Long, IocDevice> deviceMap = devices.stream().collect(Collectors.toMap(IocDevice::getId, Function.identity()));
        // 批量查询空间名称
        Set<Long> spaceIds = devices.stream().map(IocDevice::getSpaceId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, ParkSpaceFullModel> spaceIdToName = parkSpaceService.findFullSpaceMap(new ArrayList<>(spaceIds),tenantId);
        //超时还未完成的工单
        List<WorkOrder> workOrders = workOrderRepository.selectList(Wrappers.<WorkOrder>lambdaQuery().eq(WorkOrder::getType,WorkOrderTypeEnum.METERPLAN.getCode())
                .eq(WorkOrder::getOutStatus,Status.enabled.getKey()).ne(WorkOrder::getStatus,WorkOrderStatusEnum.COMPLETED.getCode())
                .eq(WorkOrder::getDeleted,Delete.NORMAL.getKey()).eq(WorkOrder::getTenantId,tenantId).select(WorkOrder::getId));
        List<Long> workOrderIds = workOrders.stream().map(WorkOrder::getId).collect(Collectors.toList());
        List<WorkOrderDevice> workOrderDevices = workOrderDeviceRepository.selectList(Wrappers.<WorkOrderDevice>lambdaQuery().in(WorkOrderDevice::getWorkOrderId,workOrderIds)
                .isNotNull(WorkOrderDevice::getDeviceId).select(WorkOrderDevice::getDeviceId));
        Set<Long> abnormalDeviceIds = workOrderDevices.stream().map(WorkOrderDevice::getDeviceId).collect(Collectors.toSet());
        // 8. 递归构建树
        List<ElectricityRealtimeConfigModel> result = new ArrayList<>();
        for (EnergyBranch root : roots) {
            ElectricityRealtimeConfigModel node = buildNode(root, parentToChildren, branchIdToDeviceIds, deviceMap, spaceIdToName, abnormalDeviceIds);
            result.add(node);
        }
        return result;
    }

    @Override
    public List<ElectricityLossRateModel> getLossRate(Integer year, Integer month) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        Date start = DateUtil.getFirstTimeBy(year, month);
        Date end = DateUtil.getLastTimeBy(year, month);
        //查出最上级能耗(科技园和BBX)
        List<ElectricityAreaCompareModel> parents = meterPersonRecordCountRepository.findAreaCompare(null,DeviceReadingTypeEnum.ELECTRICITY.getCode(),start,end,tenantId);
        if(CollectionUtils.isEmpty(parents)){
            return Collections.emptyList();
        }
        //筛选出科技园
        ElectricityAreaCompareModel kejiyuan = parents.stream().filter(a->a.getId().equals(kjyBranchId)).findFirst().orElse(null);
        if(kejiyuan == null || kejiyuan.getElectricity().compareTo(BigDecimal.ZERO) <= 0){
            return Collections.emptyList();
        }
        //查询科技园下级支路的能耗情况
        List<ElectricityAreaCompareModel> children = meterPersonRecordCountRepository.findAreaCompare(kjyBranchId,DeviceReadingTypeEnum.ELECTRICITY.getCode(),start,end,tenantId);
        if(CollectionUtils.isEmpty(children)){
            return Collections.emptyList();
        }
        //流失电量及流失占比
        BigDecimal[] loss = {kejiyuan.getElectricity(),new BigDecimal(100)};
        List<ElectricityLossRateModel> list = children.stream().map(model->{
            BigDecimal ratio = model.getElectricity().divide(kejiyuan.getElectricity(),4,RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            loss[0] = loss[0].subtract(model.getElectricity());
            loss[1] = loss[1].subtract(ratio);
            return new ElectricityLossRateModel(model.getBranchName(),model.getElectricity(),ratio);
        }).collect(Collectors.toList());

        if(loss[0].compareTo(BigDecimal.ZERO) > 0){
            //存在流失电量
            list.add(new ElectricityLossRateModel("流失电量",loss[0],loss[1]));
        }else if(loss[0].compareTo(BigDecimal.ZERO) == 0){
            //刚好不存在流失电量，那么子支路可能因为除法四舍五入导致百分比不为100%，此时补上剩余百分比
            ElectricityLossRateModel m =  list.get(list.size() - 1);
            m.setRatio(m.getRatio().add(loss[1]));
        }
        //获取最高的数值，用于换算单位
        BigDecimal max = list.stream().map(ElectricityLossRateModel::getElectricity).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        String unit = EnergyUnitUtil.getUnit(max);
        list.forEach(m->{
            m.setElectricity(EnergyUnitUtil.conversion(m.getElectricity(),unit));
            m.setUnit(unit);
        });
        return list;
    }

    @Override
    public List<EnergyBranchModel> getFlowData(BranchEnergyFlowParam param) {
        BranchEnergyFlowQueryParam p = BeanUtils.convertTo(param,BranchEnergyFlowQueryParam::new);
        p.setQueryType(param.getMonth() != null ? 1:2);
        p.setStartDate(DateUtil.getFirstTimeBy(param.getYear(),param.getMonth()));
        p.setEndDate(DateUtil.getLastTimeBy(param.getYear(),param.getMonth()));
        if(DeviceReadingTypeEnum.ELECTRICITY.getCode().equals(p.getBranchType())){
            param.setBranchId(kjyBranchId);
        }
        return branchEnergyFlowService.queryByBranchId(param.getBranchId(),p);
    }

    @Override
    public List<ElectricityPerCapitaTrendModel> getPerCapitaTrend(Integer year, Integer month) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<ElectricityPerCapitaTrendModel> list;
        if(month == null){
            list = meterPersonRecordCountRepository.getMonthlyPerCapitaTrend(DeviceReadingTypeEnum.ELECTRICITY.getCode(),year,tenantId);
        }else {
            list = meterPersonRecordCountRepository.getDailyPerCapitaTrend(DeviceReadingTypeEnum.ELECTRICITY.getCode(),year,month,tenantId);
        }
        if(CollectionUtils.isEmpty(list)){
            return list;
        }
        //查询科技园人数
        List<SpaceBasicInfo> spaces = spaceBasicInfoRepository.selectList(Wrappers.<SpaceBasicInfo>lambdaQuery().eq(SpaceBasicInfo::getSpaceId,kjySpaceId));
        int people = CollectionUtils.isEmpty(spaces) || spaces.get(0).getCapacity() == null ? 1 : spaces.get(0).getCapacity();
        Date now = new Date();
        int now_year = DateUtil.year(now);
        int now_month = DateUtil.month(now) + 1;
        int now_dayOfMonth = DateUtil.dayOfMonth(now);
        list.forEach(l->{
            //天数
            int days = month != null ? 1 : YearMonth.of(year,Integer.parseInt(l.getDate())).lengthOfMonth();
            if(now_year == year && now_month == Integer.parseInt(l.getDate())){
                days = now_dayOfMonth;
            }
            String date = month != null ? DateUtil.format(DateUtil.parse(l.getDate()),"MM-dd") : l.getDate() + "月";
            l.setDate(date);
            //计算人均每天用电量
            l.setPersonElectricity(l.getPersonElectricity().divide(new BigDecimal(people).multiply(new BigDecimal(days)),2,RoundingMode.HALF_UP));
        });

        //获取最高的数值，用于换算单位
        BigDecimal max = list.stream().map(ElectricityPerCapitaTrendModel::getPersonElectricity).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        String unit = EnergyUnitUtil.getUnit(max);
        list.forEach(m->{
            m.setPersonElectricity(EnergyUnitUtil.conversion(m.getPersonElectricity(),unit));
            m.setUnit(unit);
        });
        return list;
    }

    @Override
    public List<ElectricitySafetyReminderModel> getSafetyReminderList(Integer year, Integer month,String branchType) {
        IPage<EnergyAbnormalRemind> page = energyAbnormalRemindRepository.selectPage(new Page<>(1,-1),Wrappers.<EnergyAbnormalRemind>lambdaQuery()
                .between(EnergyAbnormalRemind::getCreateTime,DateUtil.getFirstTimeBy(year,month),DateUtil.getLastTimeBy(year,month))
                .eq(EnergyAbnormalRemind::getRemindType,branchType).eq(EnergyAbnormalRemind::getDeleted,Delete.NORMAL.getKey())
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), EnergyAbnormalRemind::getTenantId, WebFrameworkUtils.getHeaderTenantId()).orderByDesc(EnergyAbnormalRemind::getCreateTime));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return Collections.emptyList();
        }
        return page.getRecords().stream().map(r->{
            ElectricitySafetyReminderModel m = new ElectricitySafetyReminderModel();
            m.setRemindTime(r.getCreateTime());
            m.setContent(r.getRemindContent());
            return m;
        }).collect(Collectors.toList());
    }

    /**
     * 获取最上级电力支路ID列表
     */
    private List<Long> getTopLevelElectricityBranchIds(Long tenantId) {
        List<EnergyBranch> branches = energyBranchRepository.selectList(Wrappers.<EnergyBranch>lambdaQuery()
                .eq(EnergyBranch::getBranchType, BranchEnergyTypeEnum.ELECTRICITY.getCode())
                .isNull(EnergyBranch::getParentId).eq(EnergyBranch::getDeleted, Delete.NORMAL.getKey())
                .eq(EnergyBranch::getTenantId, tenantId).select(EnergyBranch::getId));
        return branches.stream().map(EnergyBranch::getId).collect(Collectors.toList());
    }

    /**
     * 根据支路ID列表获取设备ID列表
     */
    private List<Long> getDeviceIdsByBranchIds(List<Long> branchIds) {
        List<BranchDevice> devices = branchDeviceRepository.selectList(Wrappers.<BranchDevice>lambdaQuery()
                .in(BranchDevice::getBranchId, branchIds).select(BranchDevice::getDeviceId));
        return devices.stream().map(BranchDevice::getDeviceId).collect(Collectors.toList());
    }

    /**
     * 计算指定设备在时间范围内的总用电量（KWh）
     */
    private BigDecimal calculateTotalElectricity(List<Long> deviceIds, Date start, Date end) {
        BigDecimal total = BigDecimal.ZERO;
        for (Long deviceId:deviceIds){
            //获取时间范围内的最后一条记录
            List<MeterPersonRecordCount> lastRecords = meterPersonRecordCountRepository.selectList(Wrappers.<MeterPersonRecordCount>lambdaQuery()
                    .between(MeterPersonRecordCount::getCountTime, start, end).eq(MeterPersonRecordCount::getDeviceId, deviceId).orderByDesc(MeterPersonRecordCount::getCountTime).last("LIMIT 1"));
            if(!CollectionUtils.isEmpty(lastRecords)){
                //获取时间范围前的最后一条记录
                List<MeterPersonRecordCount> firstRecords = meterPersonRecordCountRepository.selectList(Wrappers.<MeterPersonRecordCount>lambdaQuery()
                        .lt(MeterPersonRecordCount::getCountTime, start).eq(MeterPersonRecordCount::getDeviceId, deviceId).orderByDesc(MeterPersonRecordCount::getCountTime).last("LIMIT 1"));
                BigDecimal last = lastRecords.get(0).getReadingValue();
                BigDecimal first = CollectionUtils.isEmpty(firstRecords) ? BigDecimal.ZERO : firstRecords.get(0).getReadingValue();
                total = total.add(last.subtract(first));
            }
        }
        return total;
    }

    /**
     * 获取最上级空间的总面积（㎡）
     */
    private BigDecimal getTotalArea(Long tenantId) {
        List<ParkSpace> parkSpaces = parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery()
                .eq(ParkSpace::getParentSpaceId, 0L).eq(ParkSpace::getDeleted, Delete.NORMAL.getKey())
                .eq(ParkSpace::getTenantId, tenantId).select(ParkSpace::getId));
        List<Long> spaceIds = parkSpaces.stream().map(ParkSpace::getId).collect(Collectors.toList());
        if (spaceIds.isEmpty()) {
            return BigDecimal.ZERO;
        }
        List<SpaceBasicInfo> spaces = spaceBasicInfoRepository.selectList(Wrappers.<SpaceBasicInfo>lambdaQuery().in(SpaceBasicInfo::getSpaceId, spaceIds));
        return spaces.stream().map(SpaceBasicInfo::getArea).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 返回空模型（无数据时使用）
     */
    private ElectricityOverviewModel buildEmptyModel() {
        ElectricityOverviewModel model = new ElectricityOverviewModel();
        model.setTotalElectricity(BigDecimal.ZERO);
        model.setTotalElectricityUnit("KWh");
        model.setDailyElectricityIntensity(BigDecimal.ZERO);
        model.setDailyElectricityIntensityUnit("KWh/㎡/天");
        return model;
    }

    private ElectricityRealtimeConfigModel buildNode(EnergyBranch branch, Map<Long, List<EnergyBranch>> parentToChildren, Map<Long, List<Long>> branchIdToDeviceIds,
                                                     Map<Long, IocDevice> deviceMap, Map<Long, ParkSpaceFullModel> spaceIdToName, Set<Long> abnormalDeviceIds) {
        ElectricityRealtimeConfigModel node = new ElectricityRealtimeConfigModel();
        node.setBranchName(branch.getBranchName());
        node.setBranchCode(branch.getBranchCode()); // 如果支路表有该字段
        node.setBranchType(branch.getBranchType());
        // 填充设备列表
        List<Long> deviceIds = branchIdToDeviceIds.getOrDefault(branch.getId(), Collections.emptyList());
        List<ElectricityRealtimeConfigModel.ElectricityDeviceModel> deviceModels = deviceIds.stream().filter(deviceMap::containsKey).map(deviceId -> {
            IocDevice device = deviceMap.get(deviceId);
            ElectricityRealtimeConfigModel.ElectricityDeviceModel model = new ElectricityRealtimeConfigModel.ElectricityDeviceModel();
            model.setDeviceName(device.getDeviceName());
            model.setReadingCode(device.getReadingCode());
            model.setReadingValue(device.getReadingValue());
            model.setSpaceName(spaceIdToName.getOrDefault(device.getSpaceId(),new ParkSpaceFullModel()).getSpaceName());
            model.setNormal(!abnormalDeviceIds.contains(deviceId));
            return model;
        }).collect(Collectors.toList());
        node.setDevices(deviceModels);
        // 递归构建子节点
        List<EnergyBranch> children = parentToChildren.getOrDefault(branch.getId(), Collections.emptyList());
        List<ElectricityRealtimeConfigModel> childNodes = children.stream().map(child -> buildNode(child, parentToChildren, branchIdToDeviceIds,
                deviceMap, spaceIdToName, abnormalDeviceIds)).collect(Collectors.toList());
        node.setChildren(childNodes);
        return node;
    }
}
