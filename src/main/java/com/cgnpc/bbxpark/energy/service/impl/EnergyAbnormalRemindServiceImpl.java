package com.cgnpc.bbxpark.energy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.settings.dto.model.DictItemModel;
import com.cgnpc.bbxpark.settings.service.impl.DictServiceImpl;
import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.common.enums.ProblemTypeEnum;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.energy.domain.EnergyAbnormalRemind;
import com.cgnpc.bbxpark.energy.domain.MeterAutoRecord;
import com.cgnpc.bbxpark.energy.domain.MeterPersonRecordCount;
import com.cgnpc.bbxpark.energy.dto.model.EnergyAbnormalRemindModel;
import com.cgnpc.bbxpark.energy.dto.param.EnergyAbnormalRemindParam;
import com.cgnpc.bbxpark.energy.mapper.EnergyAbnormalRemindRepository;
import com.cgnpc.bbxpark.energy.service.IEnergyAbnormalRemindService;
import com.cgnpc.bbxpark.energy.service.IMeterAutoRecordService;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordCountService;
import com.cgnpc.bbxpark.problemReport.domain.ProblemDevice;
import com.cgnpc.bbxpark.problemReport.model.ProblemReportModel;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportParam;
import com.cgnpc.bbxpark.problemReport.service.IProblemReportService;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.mapper.ParkSpaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @create zhaoshuo
 * @time 2025/4/22
 * @desc 能耗异常提醒服务实现类
 */
@Service
@Slf4j
public class EnergyAbnormalRemindServiceImpl extends ServiceImpl<EnergyAbnormalRemindRepository, EnergyAbnormalRemind> implements IEnergyAbnormalRemindService {
    @Autowired
    private EnergyAbnormalRemindRepository energyAbnormalRemindRepository;

    @Autowired
    private IProblemReportService problemReportService;

    @Autowired
    private IMeterPersonRecordCountService meterPersonRecordCountService;

    @Autowired
    private IIocDeviceService iocDeviceService;

    @Autowired
    private IMeterAutoRecordService meterAutoRecordService;

    @Autowired
    private DictServiceImpl dictService;

    @Autowired
    private ParkSpaceRepository parkSpaceRepository;


    @Override
    public Boolean submitProblem(Long id) {
        if (id == null)
            throw GenericException.fail("id不能为空！");
        try {
            EnergyAbnormalRemind energyAbnormalRemind = energyAbnormalRemindRepository.selectById(id);
            if (energyAbnormalRemind == null)
                throw GenericException.fail("该记录不存在！");
            if (energyAbnormalRemind.getStatus() == 1)
                throw GenericException.fail("该报单已提交！");
            energyAbnormalRemind.setStatus(1);
            //创建工单
            ProblemReportModel problemReport = createProblemReport(energyAbnormalRemind);
            //写入业务id
            energyAbnormalRemind.setBusinessId(problemReport.getId());
            energyAbnormalRemindRepository.updateById(energyAbnormalRemind);
            return true;
        } catch (Exception e) {
            throw GenericException.fail("报单失败！请联系管理员");
        }
    }


    /**
     * 搜索同比能耗异常偏差
     */
    @Override
    public void searchEnergyAbnormal() {
        log.info("定时搜索同比能耗异常偏差开始！");
        List<DictItemModel> result = dictService.findItemsByDictType("EnergyAbnormalRemind");
        if (CollectionUtils.isEmpty(result)) {
            log.info("未找到字典分类能耗异常提醒");
            return;
        }
        BigDecimal quota = null;
        for (DictItemModel dictItemModel : result) {
            if (dictItemModel.getLabel().equals("能耗偏差定额")) {
                quota = new BigDecimal(dictItemModel.getValue().replace("%", "")).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                break;
            }
        }
        if (quota == null) {
            log.info("未找到能耗偏差定额,能耗异常偏差搜索结束");
            return;
        }
        //获取上个月第一天的开始时间和上个月最后一天的结束时间
        // 获取当前年月
        YearMonth currentYearMonth = YearMonth.now();
        // 获取上个月年月
        YearMonth lastYearMonth = currentYearMonth.minusMonths(1);

        // 获取上个月第一天
        LocalDate firstDayOfLastMonth = lastYearMonth.atDay(1);
        // 上个月第一天的开始时间
        LocalDateTime startOfFirstDay = firstDayOfLastMonth.atStartOfDay();

        // 获取上个月最后一天
        LocalDate lastDayOfLastMonth = lastYearMonth.atEndOfMonth();
        // 上个月最后一天的结束时间
        LocalDateTime endOfLastDay = lastDayOfLastMonth.atTime(23, 59, 59);

        //获取上上个月的第一天
        YearMonth yearMonth = lastYearMonth.minusMonths(1);
        LocalDate firstDayOfLastLastMonth = yearMonth.atDay(1);
        LocalDateTime startLastLastMonth = firstDayOfLastLastMonth.atStartOfDay();
        //获取上上个月的最后一天
        LocalDate lastDayOfLastLastMonth = yearMonth.atEndOfMonth();
        LocalDateTime endOfLastLastMonth = lastDayOfLastLastMonth.atTime(23, 59, 59);

        //查询上个月数据
        List<MeterPersonRecordCount> lastMonthlist = meterPersonRecordCountService.list(new LambdaQueryWrapper<MeterPersonRecordCount>().between(MeterPersonRecordCount::getCountTime, startOfFirstDay, endOfLastDay));
        //根据设备id进行分组过滤只取countTime统计时间最晚的一条数据
        Map<Long, MeterPersonRecordCount> lastMonthRecord = groupByDeviceIdAndGetLatest(lastMonthlist);

        //查询上上个月的数据
        List<MeterPersonRecordCount> lastLastMonthList = meterPersonRecordCountService.list(new LambdaQueryWrapper<MeterPersonRecordCount>().between(MeterPersonRecordCount::getCountTime, startLastLastMonth, endOfLastLastMonth));
        Map<Long, MeterPersonRecordCount> lastlastMonthRecord = groupByDeviceIdAndGetLatest(lastLastMonthList);

        for (Long deviceId : lastMonthRecord.keySet()) {
            if (lastlastMonthRecord.containsKey(deviceId)) {
                MeterPersonRecordCount lastMonthRecordCount = lastMonthRecord.get(deviceId);
                MeterPersonRecordCount lastlastMonthRecordCount = lastlastMonthRecord.get(deviceId);
                //上月数据减去上上月数据计算出差值
                //如果上个月比上上个月的数值更大
                if (lastMonthRecordCount.getReadingValue().compareTo(lastlastMonthRecordCount.getReadingValue()) >= 0) {
                    BigDecimal lastMothEnergy = lastMonthRecordCount.getReadingValue().subtract(lastlastMonthRecordCount.getReadingValue()).setScale(2, RoundingMode.HALF_UP);
                    ;
                    //获取去年的上个月数据
                    YearMonth lastYear = lastYearMonth.minusYears(1);
                    LocalDateTime lastYearLastMonthFirthDay = lastYear.atDay(1).atStartOfDay();
                    LocalDateTime lastYearLastMonthEndDay = lastYear.atEndOfMonth().atTime(23, 59, 59);
                    List<MeterPersonRecordCount> lastYearLastMonthList = meterPersonRecordCountService.list(new LambdaQueryWrapper<MeterPersonRecordCount>().between(MeterPersonRecordCount::getCountTime, lastYearLastMonthFirthDay, lastYearLastMonthEndDay)
                            .eq(MeterPersonRecordCount::getDeviceId, deviceId)
                            .orderByDesc(MeterPersonRecordCount::getCountTime)
                            .last("limit 1"));
                    //
                    if (lastYearLastMonthList.size() > 0) {
                        MeterPersonRecordCount lastYearLastMonthRecordCount = lastYearLastMonthList.get(0);
                        //获取去年的上上个月数据
                        YearMonth lastLastYear = lastYear.minusMonths(1);
                        LocalDateTime lastYearLastLastMonthFirthDay = lastLastYear.atDay(1).atStartOfDay();
                        LocalDateTime lastYearLastLastMonthEndDay = lastLastYear.atEndOfMonth().atTime(23, 59, 59);
                        List<MeterPersonRecordCount> lastYearLastLastMonthList = meterPersonRecordCountService.list(new LambdaQueryWrapper<MeterPersonRecordCount>().between(MeterPersonRecordCount::getCountTime, lastYearLastLastMonthFirthDay, lastYearLastLastMonthEndDay)
                                .eq(MeterPersonRecordCount::getDeviceId, deviceId)
                                .orderByDesc(MeterPersonRecordCount::getCountTime)
                                .last("limit 1"));
                        if (lastYearLastLastMonthList.size() > 0) {
                            MeterPersonRecordCount lastYearLastLastMonthRecordCount = lastYearLastLastMonthList.get(0);
                            //如果去年上个月比上上个月的数值更大
                            if (lastYearLastMonthRecordCount.getReadingValue().compareTo(lastYearLastLastMonthRecordCount.getReadingValue()) >= 0) {
                                BigDecimal lastYearEnergy = lastYearLastMonthRecordCount.getReadingValue().subtract(lastYearLastLastMonthRecordCount.getReadingValue()).setScale(2, RoundingMode.HALF_UP);
                                //如果上个月耗能同比去年有增长计算偏差比例
                                //计算占比
                                BigDecimal energyAbnormalRemind = (lastMothEnergy.subtract(lastYearEnergy)).abs().divide(lastMothEnergy, 2, RoundingMode.HALF_UP);
                                //如果占比大于0.1则生成能耗偏差提醒
                                if (energyAbnormalRemind.compareTo(quota) > 0) {
                                    IocDeviceModel deviceDetail = iocDeviceService.detail(deviceId);
                                    // 自定义格式
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");
                                    EnergyAbnormalRemind abnormalRemind = new EnergyAbnormalRemind();
                                    abnormalRemind.setRemindName("能耗偏差提醒");
                                    abnormalRemind.setRemindContent("设备抄表数据同比偏差超出设定值；【" + deviceDetail.getDeviceName() + "】" + lastYearMonth.format(formatter) + "用能为" + lastMothEnergy + "，同期" + lastYear.format(formatter) + "用能为" + lastYearEnergy + "，设定差额为" + quota.multiply(new BigDecimal("100")) + "%，实际差额为" + energyAbnormalRemind.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%");
                                    abnormalRemind.setSpaceId(deviceDetail.getSpaceId());
                                    abnormalRemind.setSpaceName(deviceDetail.getSpaceName());
                                    abnormalRemind.setTenantId(deviceDetail.getTenantId());
                                    abnormalRemind.setDeviceId(String.valueOf(deviceId));
//                                    abnormalRemind.setDeviceName(deviceDetail.getDeviceName());
                                    abnormalRemind.setRemindType(deviceDetail.getReadingType());
                                    this.save(abnormalRemind);
                                }

                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public IPage<EnergyAbnormalRemindModel> queryPage(EnergyAbnormalRemindParam param) {
        IPage<EnergyAbnormalRemind> ipage = new Page<>(param.getCurrent(), param.getSize());
        LambdaQueryWrapper<EnergyAbnormalRemind> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(param.getRemindName())) {
            wrapper.like(EnergyAbnormalRemind::getRemindName, param.getRemindName());
        }

        if (param.getStatus() != null) {
            wrapper.eq(EnergyAbnormalRemind::getStatus, param.getStatus());
        }

        if (param.getCreateTimeStart() != null && param.getCreateTimeEnd() != null) {
            wrapper.between(EnergyAbnormalRemind::getCreateTime, param.getCreateTimeStart(), param.getCreateTimeEnd());
        }

        if (ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId())) {
            wrapper.eq(EnergyAbnormalRemind::getTenantId, WebFrameworkUtils.getHeaderTenantId());
        }

        wrapper.orderByDesc(EnergyAbnormalRemind::getCreateTime);

        IPage<EnergyAbnormalRemind> page = energyAbnormalRemindRepository.selectPage(ipage, wrapper);

        List<EnergyAbnormalRemindModel> remindModels = BeanUtils.convertListTo(page.getRecords(), EnergyAbnormalRemindModel::new);
        // 创建分页结果对象
        return  ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(),remindModels);
    }

    /**
     * 搜索休息时段能耗异常
     */
    @Override
    public void searchSleepEnergy() {
        log.info("定时搜索休息时段能耗情况开始！");
        List<DictItemModel> result = dictService.findItemsByDictType("EnergyAbnormalRemind");
        if (CollectionUtils.isEmpty(result))
            return;
        //配置的开始时间
        LocalDateTime beginTime = null;
        //配置的开始查询结束时间
        LocalDateTime endTime = null;
        //限制额度
        BigDecimal quota = null;
        //支路编码
        String spaceCode = null;

        String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String lastDay = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        for (DictItemModel dictItemModel : result) {
            switch (dictItemModel.getLabel()) {
                case "开始时间":
                    beginTime = LocalDateTime.parse(lastDay + " " + dictItemModel.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    break;
                case "结束时间":
                    endTime = LocalDateTime.parse(now + " " + dictItemModel.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    break;
                case "休息时间能耗定额":
                    quota = new BigDecimal(dictItemModel.getValue());
                    break;
                case "空间编码":
                    spaceCode = dictItemModel.getValue();
                    break;
                default:
                    continue;
            }
        }
        if (beginTime == null || endTime == null || quota == null || spaceCode == null) {
            log.info("缺少配置信息，请检查配置信息！休息时段能耗查询结束");
            return;
        }
        log.info("查询到休息时段能耗字典配置" + result);
        //查询支路信息 - 修改为查询空间
//        List<EnergyBranch> energyBranches = energyBranchService.list(new LambdaQueryWrapper<EnergyBranch>().eq(EnergyBranch::getBranchCode, branchCode).eq(EnergyBranch::getBranchType, BranchEnergyTypeEnum.ELECTRICITY.getCode()));
//        if (CollectionUtils.isEmpty(energyBranches)){
//            log.info("支路编码错误，休息时段能耗查询结束！");
//            return;
//        }
//        EnergyBranch energyBranch = energyBranches.get(0);
        //        //查询支路下的电力设备
//        List<BranchDevice> energyDevices = branchDeviceService.list(new LambdaQueryWrapper<BranchDevice>().eq(BranchDevice::getBranchId, energyBranch.getId()));
//        if (CollectionUtils.isEmpty(energyDevices)){
//            log.info("未查询到支路下电力设备，休息时段能耗查询结束！");
//            return;
//        }
//        //获取设备id集合
//        List<Long> deviceIds = energyDevices.stream().map(BranchDevice::getDeviceId).collect(Collectors.toList());
        List<ParkSpace> parkSpace = parkSpaceRepository.selectList(new LambdaQueryWrapper<ParkSpace>().eq(ParkSpace::getSpaceCode, spaceCode));
        if (CollectionUtils.isEmpty(parkSpace)) {
            log.info("空间编码错误，休息时段能耗查询结束！");
            return;
        }
        Map<Long,ParkSpace> parkSpaces = parkSpace.stream().collect(Collectors.toMap(ParkSpace::getId, space -> space));
        LambdaQueryWrapper<IocDevice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(IocDevice::getSpaceId, parkSpaces.keySet());
        queryWrapper.eq(IocDevice::getReadingType, DeviceReadingTypeEnum.ELECTRICITY.getCode());
        queryWrapper.eq(IocDevice::getDeleted, Status.enabled.getKey());
        queryWrapper.eq(IocDevice::getEnableStatus, Status.enabled.getKey());
        List<IocDevice> deviceServiceList = iocDeviceService.list(queryWrapper);

        if (CollectionUtils.isEmpty(deviceServiceList)) {
            log.info("未查询到空间下电力设备，休息时段能耗查询结束！");
        }
        Map<Long, List<IocDevice>> deviceMap = deviceServiceList.stream().collect(Collectors.groupingBy(IocDevice::getSpaceId));
        for (ParkSpace space : parkSpace) {
            List<IocDevice> iocDevices = deviceMap.get(space.getId());
            if (CollectionUtils.isEmpty(iocDevices))
                continue;
            List<Long> deviceIds = iocDevices.stream().map(IocDevice::getId).collect(Collectors.toList());
            //获取所有设备的上报数值
            List<MeterAutoRecord> autoRecords = meterAutoRecordService.list(new LambdaQueryWrapper<MeterAutoRecord>().eq(MeterAutoRecord::getReadingType, DeviceReadingTypeEnum.ELECTRICITY.getCode()).between(MeterAutoRecord::getCreateTime, beginTime, endTime).in(MeterAutoRecord::getDeviceId, deviceIds));
            if (CollectionUtils.isNotEmpty(autoRecords)) {
                //根据设备id进行分组
                Map<Long, List<MeterAutoRecord>> groupByDeviceId = autoRecords.stream().collect(Collectors.groupingBy(MeterAutoRecord::getDeviceId));
                //总能量消耗
                BigDecimal energy = new BigDecimal(0);
                //循环将分组后的数据进行时间从大到小排序
                for (Long deviceId : groupByDeviceId.keySet()) {
                    List<MeterAutoRecord> meterAutoRecordList = groupByDeviceId.get(deviceId);
                    if (CollectionUtils.isNotEmpty(meterAutoRecordList)) {
                        Collections.sort(meterAutoRecordList, new Comparator<MeterAutoRecord>() {
                            @Override
                            public int compare(MeterAutoRecord o1, MeterAutoRecord o2) {
                                return o2.getCreateTime().compareTo(o1.getCreateTime());
                            }
                        });
                        //将最大时间的数据的抄表值减去最小时间的抄表值就按差值
                        MeterAutoRecord maxRecord = meterAutoRecordList.get(0);
                        MeterAutoRecord minRecord = meterAutoRecordList.get(meterAutoRecordList.size() - 1);
                        energy = energy.add(maxRecord.getReadingValue().subtract(minRecord.getReadingValue())).setScale(2, RoundingMode.HALF_UP);
                    }
                }

                if (energy.compareTo(quota) > 0) {
                    EnergyAbnormalRemind abnormalRemind = new EnergyAbnormalRemind();
                    String ids = groupByDeviceId.keySet().stream().map(Objects::toString).collect(Collectors.joining(","));
                    abnormalRemind.setDeviceId(ids);
                    abnormalRemind.setRemindName("休息时段能耗异常提醒");
                    abnormalRemind.setRemindContent("区域休息时段能耗超出；" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(beginTime) + "至" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(endTime) + "时间段【" + space.getSpaceName() + "】耗电为" + energy + "度，设定值为" + quota + "度，超出" + energy.subtract(quota).setScale(2, RoundingMode.HALF_UP) + "度");
                    abnormalRemind.setSpaceId(space.getId());
                    abnormalRemind.setSpaceName(space.getSpaceName());
                    abnormalRemind.setTenantId(space.getTenantId());
                    this.save(abnormalRemind);
                }
            }
        }
    }

    public static Map<Long, MeterPersonRecordCount> groupByDeviceIdAndGetLatest(List<MeterPersonRecordCount> recordList) {
        Map<Long, MeterPersonRecordCount> resultMap = new HashMap<>();
        for (MeterPersonRecordCount record : recordList) {
            Long deviceId = record.getDeviceId();
            Date currentCountTime = record.getCountTime();
            if (resultMap.containsKey(deviceId)) {
                MeterPersonRecordCount existingRecord = resultMap.get(deviceId);
                Date existingCountTime = existingRecord.getCountTime();
                if (currentCountTime.after(existingCountTime)) {
                    resultMap.put(deviceId, record);
                }
            } else {
                resultMap.put(deviceId, record);
            }
        }
        return resultMap;
    }

    private ProblemReportModel createProblemReport(EnergyAbnormalRemind energyAbnormalRemind) {
        //创建问题报修数据
        ProblemReportParam param = new ProblemReportParam();
        param.setProblemDesc(energyAbnormalRemind.getRemindContent());
        param.setProblemType(ProblemTypeEnum.ENERGY_ABNORMAL.getCode());
        param.setSpaceId(energyAbnormalRemind.getSpaceId());
        param.setSpaceName(energyAbnormalRemind.getSpaceName());
        //添加问题设备
        if (energyAbnormalRemind.getDeviceId() != null) {
            String[] deviceIds = energyAbnormalRemind.getDeviceId().split(",");
            if (deviceIds.length > 0) {
                List<IocDevice> iocDevices = iocDeviceService.list(new LambdaQueryWrapper<IocDevice>().in(IocDevice::getId, Arrays.asList(deviceIds)));
                if (CollectionUtils.isNotEmpty(iocDevices)) {
                    List<ProblemDevice> problemDeviceList = getProblemDevices(energyAbnormalRemind, iocDevices);
                    param.setProblemDeviceList(problemDeviceList);
                }
            }
        }
        ProblemReportModel reportModel = problemReportService.save(param);
        return reportModel;
    }

    private List<ProblemDevice> getProblemDevices(EnergyAbnormalRemind energyAbnormalRemind, List<IocDevice> iocDevices) {
        List<ProblemDevice> problemDeviceList = new ArrayList<>();
        for (IocDevice iocDevice : iocDevices) {
            ProblemDevice problemDevice = new ProblemDevice();
            problemDevice.setDeviceId(iocDevice.getId());
            problemDevice.setDeviceName(iocDevice.getDeviceName());
            problemDevice.setSpaceId(energyAbnormalRemind.getSpaceId());
            problemDevice.setSpaceName(energyAbnormalRemind.getSpaceName());
            problemDeviceList.add(problemDevice);
        }
        return problemDeviceList;
    }
}
