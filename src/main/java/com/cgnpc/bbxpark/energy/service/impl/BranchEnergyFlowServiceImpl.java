package com.cgnpc.bbxpark.energy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.common.enums.DeviceMeterMethodEnum;
import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.StringUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.energy.domain.EnergyAbnormalRemind;
import com.cgnpc.bbxpark.energy.domain.EnergyBranch;
import com.cgnpc.bbxpark.energy.domain.MeterAutoRecordCount;
import com.cgnpc.bbxpark.energy.domain.MeterPersonRecordCount;
import com.cgnpc.bbxpark.energy.dto.model.BranchDeviceModel;
import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.energy.dto.param.BranchEnergyFlowQueryParam;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchQueryParam;
import com.cgnpc.bbxpark.energy.mapper.EnergyAbnormalRemindRepository;
import com.cgnpc.bbxpark.energy.mapper.EnergyBranchRepository;
import com.cgnpc.bbxpark.energy.service.*;
import com.cgnpc.bbxpark.ioc.dto.model.RestaurantConsumptionShareItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @create zhaoshuo
 * @time 2025/4/24
 * @desc
 */
@Service
@Slf4j
public class BranchEnergyFlowServiceImpl implements IBranchEnergyFlowService {
    @Autowired
    private EnergyBranchRepository energyBranchRepository;
    @Autowired
    private IEnergyBranchService energyBranchService;

    @Autowired
    private IBranchDeviceService branchDeviceService;

    @Autowired
    private IMeterPersonRecordCountService meterPersonRecordCountService;

    @Autowired
    private IMeterAutoRecordCountService meterAutoRecordCountService;

    @Autowired
    private EnergyAbnormalRemindRepository energyAbnormalRemindRepository;


    @Override
    public List<EnergyBranchModel> queryData(BranchEnergyFlowQueryParam param) {
        if (param == null || param.getStartDate() == null || param.getEndDate() == null || StringUtils.isEmpty(param.getBranchType())  || param.getQueryType() == null || StringUtils.isEmpty(param.getMeterMethod())) {
            throw GenericException.fail("参数不能为空");
        }

        // 初始化查询参数
        EnergyBranchQueryParam energyBranchQueryParam = new EnergyBranchQueryParam();
        energyBranchQueryParam.setBranchType(param.getBranchType());
        energyBranchQueryParam.setStatus(Status.enabled.getKey());
        energyBranchQueryParam.setDeleted(Status.enabled.getKey());

        // 获取树形结构数据
//        List<EnergyBranchModel> treeList = energyBranchService.findTreeList(energyBranchQueryParam);
        List<EnergyBranchModel> energyBranchModels = energyBranchService.buildQuery(energyBranchQueryParam);
        return flowHandle(energyBranchModels,param);
    }

    @Override
    public List<EnergyBranchModel> queryByBranchId(Long branchId, BranchEnergyFlowQueryParam param) {
        if(branchId == null){
            return queryData(param);
        }
        if (param == null || param.getStartDate() == null || param.getEndDate() == null || param.getQueryType() == null || StringUtils.isEmpty(param.getMeterMethod())) {
            throw GenericException.fail("参数不能为空");
        }
        List<EnergyBranch> energyBranches = energyBranchRepository.listWithChildren(branchId, WebFrameworkUtils.getHeaderTenantId());
        energyBranches.stream().filter(b->b.getId().equals(branchId)).forEach(b->b.setParentId(null));
        List<EnergyBranchModel> energyBranchModels = BeanUtils.convertListTo(energyBranches, EnergyBranchModel::new);
        return flowHandle(energyBranchModels,param);
    }

    @Override
    public Boolean executeAutoReadingRemind(String type) {
        // 初始化查询参数
        EnergyBranchQueryParam energyBranchQueryParam = new EnergyBranchQueryParam();
        energyBranchQueryParam.setBranchType(type);
        energyBranchQueryParam.setStatus(Status.enabled.getKey());
        energyBranchQueryParam.setDeleted(Status.enabled.getKey());

        List<EnergyBranchModel> energyBranchModels = energyBranchService.buildQuery(energyBranchQueryParam);
        if(CollectionUtil.isEmpty(energyBranchModels)){
            return true;
        }
        Map<Long, List<EnergyBranchModel>> energyBranchModelMap = energyBranchModels.stream().collect(Collectors.groupingBy(EnergyBranchModel::getTenantId));
        Date date = DateUtil.date();
        Date yesterday = DateUtil.offsetDay(date, -1);
        for(Map.Entry<Long, List<EnergyBranchModel>> entry : energyBranchModelMap.entrySet()){
            executeFlowHandle(entry.getValue(), yesterday, entry.getKey());
        }
        return true;
    }

    private void executeFlowHandle(List<EnergyBranchModel> list, Date yesterday, Long tenantId){
        List<EnergyBranchModel> treeList = EnergyBranchServiceImpl.buildTree(list);
        if (CollectionUtil.isEmpty(treeList)) {
            log.info("树形结构为空，直接返回");
            return;
        }

        for (EnergyBranchModel energyBranchModel : treeList) {
            // 获取支路下的能耗
            executeEnergyFlow(energyBranchModel, yesterday, tenantId);
        }
        //操作树判断能耗是否异常
        for (EnergyBranchModel energyBranchModel : treeList) {
            detectEnergyAbnormal(energyBranchModel);
        }
    }

    /**
     * 检测能耗异常
     * @param branchModel 支路模型
     */
    private void detectEnergyAbnormal(EnergyBranchModel branchModel) {
        if (branchModel == null) {
            return;
        }
        BigDecimal branchEnergy = branchModel.getTotalPower();
        BigDecimal childrenEnergySum = calculateChildrenEnergySum(branchModel.getChildren());
        // 计算下级支路能耗之和
        if(branchEnergy != null && branchEnergy.compareTo(BigDecimal.ZERO) > 0  && childrenEnergySum.compareTo(BigDecimal.ZERO) > 0) {

            // 计算差值：支路能耗 - 下级支路能耗之和
            BigDecimal difference = branchEnergy.subtract(childrenEnergySum);

            // 检测异常条件
            StringBuilder remindBuilder = new StringBuilder();
            String type = "电";
            if(DeviceReadingTypeEnum.WATER.getCode().equals(branchModel.getBranchType())){
                type = "水";
            }else if(DeviceReadingTypeEnum.GAS.getCode().equals(branchModel.getBranchType())){
                type = "燃气";
            }
            // 条件②：支路能耗 - 下级支路能耗之和 < 0
            if (difference.compareTo(BigDecimal.ZERO) < 0) {
                remindBuilder.append(String.format("%s及下级支路相关抄表数据可能存在异常，建议对最近抄表工作进行反向排查。",
                        branchModel.getBranchName()));
            }
            // 条件①：(支路能耗 - 下级支路能耗之和) / 支路能耗 × 100% ≥ 5%
            else if (branchEnergy.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal lossRate = difference.divide(branchEnergy, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                if (lossRate.compareTo(new BigDecimal("5")) >= 0) {
                    remindBuilder.append(String.format("%s到下级支路的%s量传输损耗≥5%%，建议排查线路是否存在异常。",
                            branchModel.getBranchName(),type));
                }
            }
            if(remindBuilder.length() > 0){
                //生成抄表异常数据
                EnergyAbnormalRemind abnormalRemind = new EnergyAbnormalRemind();
                abnormalRemind.setRemindName(String.format("用%s提醒", type));
                abnormalRemind.setRemindContent(remindBuilder.toString());
                abnormalRemind.setStatus(0);
                abnormalRemind.setTenantId(branchModel.getTenantId());
                abnormalRemind.setCreateTime(new Date());
                abnormalRemind.setUpdateTime(new Date());
                abnormalRemind.setSpaceId(2348L);
                abnormalRemind.setSpaceName("中广核苍南基地");
                abnormalRemind.setRemindType(branchModel.getBranchType());
                energyAbnormalRemindRepository.insert(abnormalRemind);
            }
        }

        // 递归检测子支路
        if (CollectionUtil.isNotEmpty(branchModel.getChildren())) {
            for (EnergyBranchModel child : branchModel.getChildren()) {
                detectEnergyAbnormal(child);
            }
        }
    }

    /**
     * 计算下级支路能耗之和
     * @param children 子支路列表
     * @return 下级支路能耗之和
     */
    private BigDecimal calculateChildrenEnergySum(List<EnergyBranchModel> children) {
        if (CollectionUtil.isEmpty(children)) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (EnergyBranchModel child : children) {
            BigDecimal childEnergy = child.getTotalPower();
            if (childEnergy != null && childEnergy.compareTo(BigDecimal.ZERO) > 0) {
                sum = sum.add(childEnergy);
            }
        }
        return sum;
    }

    private void executeEnergyFlow(EnergyBranchModel energyBranchModel, Date yesterday, Long tenantId) {
        List<BranchDeviceModel> branchDevice = branchDeviceService.getBranchDevice(energyBranchModel.getId());
        if (CollectionUtil.isNotEmpty(branchDevice)){
            List<Long> deviceIds = branchDevice.stream()
                    .map(BranchDeviceModel::getDeviceId)
                    .collect(Collectors.toList());

            BigDecimal totalPower = BigDecimal.ZERO;
            List<MeterAutoRecordCount> autoRecordCounts = meterAutoRecordCountService.list(Wrappers.<MeterAutoRecordCount>lambdaQuery()
                    .in(MeterAutoRecordCount::getDeviceId, deviceIds)
                    .eq(MeterAutoRecordCount::getTenantId, tenantId)
                    .between(MeterAutoRecordCount::getCountTime, DateUtil.beginOfDay(yesterday), DateUtil.endOfDay(yesterday))
                    .orderByDesc(MeterAutoRecordCount::getCountTime));

            if (CollectionUtil.isNotEmpty(autoRecordCounts)) {
                totalPower = autoRecordCounts.stream().map(MeterAutoRecordCount::getEnergyConsumption).filter(ObjectUtil::isNotEmpty).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            energyBranchModel.setTotalPower(totalPower);
        }
        if (CollectionUtil.isNotEmpty(energyBranchModel.getChildren())) {
            for (EnergyBranchModel child : energyBranchModel.getChildren()) {
                executeEnergyFlow(child, yesterday, tenantId);
            }
        }
    }

    private List<EnergyBranchModel> flowHandle(List<EnergyBranchModel> list,BranchEnergyFlowQueryParam param){
        List<EnergyBranchModel> treeList = EnergyBranchServiceImpl.buildTree(list);
        if (CollectionUtil.isEmpty(treeList)) {
            log.info("树形结构为空，直接返回空列表");
            return Collections.emptyList(); // 如果树形结构为空，直接返回空列表
        }

        for (EnergyBranchModel energyBranchModel : treeList) {
            // 获取支路下的能耗
            getEnergyFlow(energyBranchModel,  param);
        }
        //操作树判断能耗是否为0，如果是则从树中移除
        List<EnergyBranchModel> returnTreeList = new ArrayList<>();
        for (EnergyBranchModel energyBranchModel : treeList) {
            EnergyBranchModel energyBranchModel1 = removeZeroReadingNodes(energyBranchModel);
            if (energyBranchModel1 != null){
                returnTreeList.add(energyBranchModel1);
            }
        }
        return returnTreeList;
    }

    public static EnergyBranchModel removeZeroReadingNodes(EnergyBranchModel root) {
        return processNode(root);
    }

    private static EnergyBranchModel processNode(EnergyBranchModel node) {
        if (node == null) {
            return null;
        }

        if (CollectionUtil.isNotEmpty(node.getChildren())){
            // 递归处理所有子节点，并收集非空的处理结果
            List<EnergyBranchModel> filteredChildren = new ArrayList<>();
            for (EnergyBranchModel child : node.getChildren()) {
                EnergyBranchModel processedChild = processNode(child);
                if (processedChild != null) {
                    filteredChildren.add(processedChild);
                }
            }
            // 更新当前节点的子节点列表
            node.setChildren(filteredChildren);
        }

        // 如果当前节点的Value为0，返回null表示删除该节点
        if (node.getTotalPower() == null || node.getTotalPower().compareTo(BigDecimal.ZERO) == 0) {
            return null;
        } else {
            return node;
        }
    }

    private void getEnergyFlow(EnergyBranchModel energyBranchModel,BranchEnergyFlowQueryParam param) {
        List<BranchDeviceModel> branchDevice = branchDeviceService.getBranchDevice(energyBranchModel.getId());
        if (CollectionUtil.isNotEmpty(branchDevice)){
            List<Long> deviceIds = branchDevice.stream()
                    .map(BranchDeviceModel::getDeviceId)
                    .collect(Collectors.toList());

            BigDecimal totalPower = calculateTotalPower(
                    param.getMeterMethod(),
                    deviceIds,
                    energyBranchModel.getTenantId(),
                    param.getStartDate(),
                    param.getEndDate(),
                    param.getQueryType()
            );

            energyBranchModel.setTotalPower(totalPower);
        }
        if (CollectionUtil.isNotEmpty(energyBranchModel.getChildren())) {
            for (EnergyBranchModel child : energyBranchModel.getChildren()) {
                getEnergyFlow(child, param);
            }
        }
    }


    private BigDecimal calculateTotalPower(String branchType, List<Long> deviceIds, Long tenantId, Date startDate, Date endDate, Integer queryType) {
        if (StringUtils.isEmpty(branchType) || CollectionUtil.isEmpty(deviceIds) || tenantId == null || startDate == null || endDate == null) {
            return BigDecimal.ZERO; // 参数不合法时，返回零能耗
        }

        try {
            LocalDate localDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDateTime firstDayOfLastMonth = null;
            LocalDateTime lastDayOfLastMonth = null;
            if (queryType == 1){
                //以月为维度进行统计
                YearMonth lastMonth = YearMonth.from(localDate).minusMonths(1);
                firstDayOfLastMonth = lastMonth.atDay(1).atTime(LocalTime.MIN);
                lastDayOfLastMonth = lastMonth.atEndOfMonth().atTime(23, 59, 59);
            } else if (queryType == 2) {
                //以年为维度进行统计
                firstDayOfLastMonth = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().minusYears(1);
                lastDayOfLastMonth = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().minusYears(1);
            }


            if (DeviceMeterMethodEnum.PERSON.getCode().equals(branchType)) {
                return calculatePersonTotalPower(deviceIds, tenantId, startDate, endDate, firstDayOfLastMonth, lastDayOfLastMonth);
            } else if (DeviceMeterMethodEnum.AUTO.getCode().equals(branchType)) {
                return calculateAutoTotalPower(deviceIds, tenantId, startDate, endDate);
            }
        } catch (Exception e) {
            // 捕获异常并记录日志
            log.error("计算总能耗时发生异常", e);
        }

        return BigDecimal.ZERO; // 异常情况下返回零能耗
    }

    /**
     * 计算人工上报的总能耗
     */
    private BigDecimal calculatePersonTotalPower(List<Long> deviceIds, Long tenantId, Date startDate, Date endDate, LocalDateTime firstDayOfLastMonth, LocalDateTime lastDayOfLastMonth) {
        BigDecimal totalPower = BigDecimal.ZERO;

        // 查询本月能耗记录
        List<MeterPersonRecordCount> personRecordCounts = meterPersonRecordCountService.list(Wrappers.<MeterPersonRecordCount>lambdaQuery()
                .in(MeterPersonRecordCount::getDeviceId, deviceIds)
                .eq(MeterPersonRecordCount::getTenantId, tenantId)
                .between(MeterPersonRecordCount::getCountTime, startDate.toInstant(), endDate.toInstant())
                .orderByDesc(MeterPersonRecordCount::getCountTime));

        if (CollectionUtil.isEmpty(personRecordCounts)) {
            return totalPower;
        }

        // 查询上月能耗记录
        List<MeterPersonRecordCount> lastMonthRecordCounts = meterPersonRecordCountService.list(Wrappers.<MeterPersonRecordCount>lambdaQuery()
                .in(MeterPersonRecordCount::getDeviceId, deviceIds)
                .eq(MeterPersonRecordCount::getTenantId, tenantId)
                .between(MeterPersonRecordCount::getCountTime, firstDayOfLastMonth, lastDayOfLastMonth)
                .orderByDesc(MeterPersonRecordCount::getCountTime));

        if (CollectionUtil.isEmpty(lastMonthRecordCounts)) {
            return totalPower;
        }

        Map<Long, MeterPersonRecordCount> localRecords = groupByDeviceIdAndGetLatest(personRecordCounts);
        Map<Long, MeterPersonRecordCount> lastMonthRecords = groupByDeviceIdAndGetLatest(lastMonthRecordCounts);

        for (Long deviceId : localRecords.keySet()) {
            if (lastMonthRecords.containsKey(deviceId)) {
                MeterPersonRecordCount localRecord = localRecords.get(deviceId);
                MeterPersonRecordCount lastMonthRecord = lastMonthRecords.get(deviceId);

                if (localRecord.getReadingValue().compareTo(lastMonthRecord.getReadingValue()) >= 0) {
                    BigDecimal energyDifference = localRecord.getReadingValue().subtract(lastMonthRecord.getReadingValue()).setScale(2, RoundingMode.HALF_UP);
                    totalPower = totalPower.add(energyDifference);
                }
            }
        }

        return totalPower;
    }

    /**
     * 计算自动上报的总能耗
     */
    private BigDecimal calculateAutoTotalPower(List<Long> deviceIds, Long tenantId, Date startDate, Date endDate) {
        BigDecimal totalPower = BigDecimal.ZERO;

        // 查询本月能耗记录
        List<MeterAutoRecordCount> autoRecordCounts = meterAutoRecordCountService.list(Wrappers.<MeterAutoRecordCount>lambdaQuery()
                .in(MeterAutoRecordCount::getDeviceId, deviceIds)
                .eq(MeterAutoRecordCount::getTenantId, tenantId)
                .between(MeterAutoRecordCount::getCountTime, startDate.toInstant(), endDate.toInstant())
                .orderByDesc(MeterAutoRecordCount::getCountTime));

        if (CollectionUtil.isNotEmpty(autoRecordCounts)) {
            totalPower = calculateEnergyUsage(autoRecordCounts, totalPower);
        }

        return totalPower;
    }

    /**
     * 计算能源使用量
     * <p>
     * 根据自动记录的计数和总功率，计算每个设备的当月能源使用量
     *
     * @param autoRecordCounts 自动记录的计数列表，包含每个设备的读数信息
     * @param totalPower       所有设备的总功率，用于累加计算总能源使用量
     */
    private BigDecimal calculateEnergyUsage(List<MeterAutoRecordCount> autoRecordCounts, BigDecimal totalPower) {
        // 创建一个映射，用于按设备 ID 分组记录
        Map<Long, List<MeterAutoRecordCount>> groupedRecords = new HashMap<>();
        // 按设备 ID 分组
        for (MeterAutoRecordCount record : autoRecordCounts) {
            groupedRecords.computeIfAbsent(record.getDeviceId(), k -> new ArrayList<>()).add(record);
        }

        // 计算每个设备的当月用能
        for (Map.Entry<Long, List<MeterAutoRecordCount>> entry : groupedRecords.entrySet()) {
            List<MeterAutoRecordCount> deviceRecords = entry.getValue();

            totalPower = deviceRecords.stream().map(MeterAutoRecordCount::getEnergyConsumption).filter(ObjectUtil::isNotEmpty).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            // 找出统计时间最大和最小的记录
//            MeterAutoRecordCount minTimeRecord = Collections.min(deviceRecords, Comparator.comparing(MeterAutoRecordCount::getCountTime));
//            MeterAutoRecordCount maxTimeRecord = Collections.max(deviceRecords, Comparator.comparing(MeterAutoRecordCount::getCountTime));
//
//            // 计算当月用能
//            if (maxTimeRecord.getReadingValue().compareTo(minTimeRecord.getReadingValue()) >= 0) {
//                // 计算上月能源使用量，并四舍五入到小数点后两位
//                BigDecimal lastMothEnergy = maxTimeRecord.getReadingValue().subtract(minTimeRecord.getReadingValue()).setScale(2, RoundingMode.HALF_UP);
//                // 累加到总功率中
//                totalPower = totalPower.add(lastMothEnergy);
//            }
        }
        return totalPower;
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
}
