package com.cgnpc.bbxpark.ioc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.enums.WorkOrderSourceEnum;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.energy.domain.BranchDevice;
import com.cgnpc.bbxpark.energy.domain.EnergyBranch;
import com.cgnpc.bbxpark.energy.mapper.BranchDeviceRepository;
import com.cgnpc.bbxpark.energy.mapper.EnergyBranchRepository;
import com.cgnpc.bbxpark.ioc.dto.param.WorkOrderPageSimpleParam;
import com.cgnpc.bbxpark.ioc.dto.param.WorkUnsatisfiedPageParam;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.dto.model.WorkEvaluateModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.service.IWorkOrderScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 大屏工单统计接口实现
 */
@Service
public class WorkOrderScreenServiceImpl implements IWorkOrderScreenService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private IParkSpaceService parkSpaceService;
    @Autowired
    private EnergyBranchRepository energyBranchRepository;
    @Autowired
    private BranchDeviceRepository branchDeviceRepository;

    @Override
    public WorkOrderKeyMetricsModel getWorkOrderKeyMetrics() {
        return workOrderRepository.getWorkOrderKeyMetrics(WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<WorkOrderSourceAnalysisModel> getWorkOrderSourceAnalysis() {
        return workOrderRepository.getWorkOrderSourceAnalysis(WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<WorkOrderDepartmentAnalysisModel> getWorkOrderDepartmentAnalysis(Long type) {
        if(ObjectUtil.isEmpty(type)) {
            type = 1L;
        }
        return workOrderRepository.getWorkOrderDepartmentAnalysis(WebFrameworkUtils.getHeaderTenantId(), type);
    }

    @Override
    public List<WorkOrderScreenSpaceAnalysisModel> getWorkOrderSpaceAnalysis(Long id) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<WorkOrderScreenSpaceAnalysisModel> resultList = new ArrayList<>();
        List<WorkOrderScreenSpaceAnalysisModel> originModels = workOrderRepository.getWorkOrderSpaceAnalysis(tenantId);
        if (CollectionUtil.isEmpty(originModels)) {
            return resultList;
        }
        Map<Long, List<WorkOrderScreenSpaceAnalysisModel>> spaceIdToAnalysisModels = originModels.stream()
                .collect(Collectors.groupingBy(WorkOrderScreenSpaceAnalysisModel::getId));
        List<Long> allSpaceIds = new ArrayList<>(spaceIdToAnalysisModels.keySet());
        Map<Long, String> spaceIdToFullPath = this.buildSpaceIdToFullPathMap(allSpaceIds, tenantId);
        Long targetParentId = Optional.ofNullable(id).orElseGet(() -> this.getRootSpaceId(tenantId));
        if (ObjectUtil.isNull(targetParentId)) {
            return resultList;
        }
        List<ParkSpace> childSpaceList = parkSpaceService.list(new LambdaQueryWrapper<ParkSpace>()
                .eq(ObjectUtil.isNotEmpty(tenantId), ParkSpace::getTenantId, tenantId)
                .eq(ParkSpace::getParentSpaceId, targetParentId));
        if (CollectionUtil.isEmpty(childSpaceList)) {
            return resultList;
        }
        for (ParkSpace childSpace : childSpaceList) {
            Long spaceId = childSpace.getId();
            String spaceName = childSpace.getSpaceName();
            boolean hasChildren = false;
            Map<String, Integer> sourceMap = new HashMap<>();
            for (Map.Entry<Long, List<WorkOrderScreenSpaceAnalysisModel>> entry : spaceIdToAnalysisModels.entrySet()) {
                Long analysisSpaceId = entry.getKey();
                String fullPath = spaceIdToFullPath.getOrDefault(analysisSpaceId, "");
                List<Long> spaceIds = pathToLongList(fullPath);
                // 全路径包含当前空间ID，才进行数据统计
                if (spaceIds.contains(spaceId)) {
                    List<WorkOrderScreenSpaceAnalysisModel> currentAnalysisModels = entry.getValue();
                    this.buildSourceAnalysisModels(sourceMap, currentAnalysisModels);
                    if (!spaceIds.get(spaceIds.size() - 1).equals(spaceId)) {
                        hasChildren = true;
                    }
                }
            }
            if (ObjectUtil.isNotEmpty(sourceMap)) {
                List<WorkOrderSourceAnalysisModel> allSourceModels = sourceMap.entrySet().stream().map(entry->{
                    WorkOrderSourceAnalysisModel model = new WorkOrderSourceAnalysisModel();
                    model.setName(entry.getKey());
                    model.setTotalNum(entry.getValue());
                    return model;
                }).collect(Collectors.toList());
                WorkOrderScreenSpaceAnalysisModel spaceModel = new WorkOrderScreenSpaceAnalysisModel();
                spaceModel.setId(spaceId);
                spaceModel.setName(spaceName);
                spaceModel.setSourceModels(allSourceModels);
                spaceModel.setHasChildren(hasChildren);
                spaceModel.setTotalNum(allSourceModels.stream().mapToInt(WorkOrderSourceAnalysisModel::getTotalNum).sum());
                resultList.add(spaceModel);
            }
        }
        return resultList;
    }



    @Override
    public List<WorkOrderHandleDateTrendModel> getWorkOrderHandleDateTrend() {
        List<WorkOrderHandleDateTrendModel> modelList = new ArrayList<>();
        List<WorkOrderHandleDateTrendModel>  completeList =  workOrderRepository.getCompleteTrend(WebFrameworkUtils.getHeaderTenantId());
        Map<String, WorkOrderHandleDateTrendModel> completeMap = CollectionUtil.isEmpty(completeList) ? new HashMap<>() : completeList.stream().collect(Collectors.toMap(WorkOrderHandleDateTrendModel::getTime, p->p));
        List<WorkOrderHandleDateTrendModel>  evaluateList = workOrderRepository.getEvaluateTrend(WebFrameworkUtils.getHeaderTenantId());
        Map<String, WorkOrderHandleDateTrendModel> evaluateMap = CollectionUtil.isEmpty(evaluateList) ? new HashMap<>() : evaluateList.stream().collect(Collectors.toMap(WorkOrderHandleDateTrendModel::getTime, p->p));

        Date currentDate  = DateUtil.date();
        // 循环生成近30天的日期（从当前日-29天 到 当前日，共30天）
        for (int i = 29; i >= 0; i--) {
            // 偏移日期：currentDate - i天
            Date offsetDate = DateUtil.offsetDay(currentDate, -i);
            // 格式化为MM-dd（比如01-28）
            String formatDate = DateUtil.format(offsetDate, "MM-dd");
            WorkOrderHandleDateTrendModel model = new WorkOrderHandleDateTrendModel();
            model.setTime(formatDate);
            model.setTotalNum(ObjectUtil.isEmpty(completeMap.get(formatDate)) ? 0 : completeMap.get(formatDate).getTotalNum());
            model.setAvgScore(ObjectUtil.isEmpty(evaluateMap.get(formatDate)) ? 0d : evaluateMap.get(formatDate).getAvgScore());
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public WorkOrderScreenUnsatisfiedTraceModel getWorkOrderUnsatisfiedTrace() {
        WorkOrderScreenUnsatisfiedTraceModel model = new WorkOrderScreenUnsatisfiedTraceModel();
        List<WorkOrderSourceModel> sourceAnalysisModels = new ArrayList<>();
        List<WorkOrderUnsatisfiedModel> unsatisfiedModels = workOrderRepository.getWorkOrderUnsatisfied(WebFrameworkUtils.getHeaderTenantId());
        if(CollectionUtil.isEmpty(unsatisfiedModels)){
            return model;
        }
        model.setTotalNum((int) unsatisfiedModels.stream().mapToLong(WorkOrderUnsatisfiedModel::getTotalNum).sum());
        Map<String, List<WorkOrderUnsatisfiedModel>> sourceMap = unsatisfiedModels.stream().collect(Collectors.groupingBy(WorkOrderUnsatisfiedModel::getName));
        for(Map.Entry<String, List<WorkOrderUnsatisfiedModel>> entry : sourceMap.entrySet()){
            List<WorkOrderUnsatisfiedModel> sourceList = entry.getValue();
            WorkOrderSourceModel sourceModel = new WorkOrderSourceModel();
            sourceModel.setName(entry.getKey());
            sourceModel.setSource(sourceList.get(0).getSource());
            sourceModel.setTotalNum((int) sourceList.stream().mapToLong(WorkOrderUnsatisfiedModel::getTotalNum).sum());
            List<WorkOrderDepartmentAnalysisModel> departmentAnalysisModels = new ArrayList<>();
            Map<String, List<WorkOrderUnsatisfiedModel>> deptMap = sourceList.stream().collect(Collectors.groupingBy(WorkOrderUnsatisfiedModel::getDepartmentName));
            for(Map.Entry<String, List<WorkOrderUnsatisfiedModel>> en : deptMap.entrySet()){
                WorkOrderDepartmentAnalysisModel deptModel = new WorkOrderDepartmentAnalysisModel();
                deptModel.setName(en.getKey());
                deptModel.setId(en.getValue().get(0).getDepartmentId());
                deptModel.setTotalNum(en.getValue().size());
                departmentAnalysisModels.add(deptModel);
            }
            sourceModel.setDepartmentAnalysisModels(departmentAnalysisModels);
            sourceAnalysisModels.add(sourceModel);
        }
        model.setSourceAnalysisModels(sourceAnalysisModels);
        return model;
    }

    @Override
    public List<WorkOrderContractEffectModel> getWorkOrderContractEffect() {
        return workOrderRepository.getWorkOrderContractEffect(WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<WorkOrderSpaceCountModel> getWorkOrderSpaceCount(String sslcCode) {
        AssertUtils.isNotEmpty(sslcCode, "楼层编码不能为空");
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<WorkOrderSpaceCountModel> validSpaceList = new ArrayList<>();
        Long targetParentId = this.getRootSpaceIdByCode(tenantId, sslcCode);
        if (ObjectUtil.isNull(targetParentId)) {
            return validSpaceList;
        }
        List<ParkSpace> childSpaceList = parkSpaceService.list(new LambdaQueryWrapper<ParkSpace>()
                .eq(ObjectUtil.isNotEmpty(tenantId),ParkSpace::getTenantId, tenantId)
                .eq(ParkSpace::getParentSpaceId, targetParentId));
        if (CollectionUtil.isEmpty(childSpaceList)) {
            return validSpaceList;
        }
        List<WorkOrderScreenSpaceAnalysisModel> spaceAnalysisModels = workOrderRepository.getWorkOrderSpaceAnalysis(tenantId);
        if (CollectionUtil.isEmpty(spaceAnalysisModels)) {
            return validSpaceList;
        }
        Map<Long, List<WorkOrderScreenSpaceAnalysisModel>> spaceIdToAnalysisModels = spaceAnalysisModels.stream()
                .collect(Collectors.groupingBy(WorkOrderScreenSpaceAnalysisModel::getId));
        List<Long> allSpaceIds = spaceIdToAnalysisModels.keySet().stream().collect(Collectors.toList());
        Map<Long, String> spaceIdToFullPath = this.buildSpaceIdToFullPathMap(allSpaceIds, tenantId);
        for (ParkSpace childSpace : childSpaceList) {
            Long spaceId = childSpace.getId();
            int totalNum = 0;
            for (Map.Entry<Long, List<WorkOrderScreenSpaceAnalysisModel>> entry : spaceIdToAnalysisModels.entrySet()) {
                Long analysisSpaceId = entry.getKey();
                String fullPath = spaceIdToFullPath.getOrDefault(analysisSpaceId, "");
                List<Long> spaceIds = pathToLongList(fullPath);
                if (spaceIds.contains(spaceId)) {
                    totalNum = totalNum + entry.getValue().stream()
                            .mapToInt(WorkOrderScreenSpaceAnalysisModel::getTotalNum)
                            .sum();
                }
            }
            if (totalNum > 0) {
                WorkOrderSpaceCountModel spaceModel = new WorkOrderSpaceCountModel();
                spaceModel.setId(spaceId);
                spaceModel.setName(childSpace.getSpaceName());
                spaceModel.setSslcCode(childSpace.getSslcCode());
                spaceModel.setTotalNum(totalNum);
                validSpaceList.add(spaceModel); // 修复原代码：添加对象到结果集
            }
        }
        return validSpaceList;
    }

    @Override
    public List<SpaceViewModel> getWorkOrderSpaceView(String sslcCode) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<SpaceViewModel> validSpaceList = new ArrayList<>();
        List<Long> targetParentIds;
        if(ObjectUtil.isEmpty(sslcCode)){
            targetParentIds = Arrays.asList(2L,163L,2360L);
        }else{
            Long targetParentId = this.getRootSpaceIdByCode(tenantId, sslcCode);
            targetParentIds = Collections.singletonList(targetParentId);
        }
        if (CollectionUtil.isEmpty(targetParentIds)) {
            return validSpaceList;
        }
        List<ParkSpace> childSpaceList = parkSpaceService.list(new LambdaQueryWrapper<ParkSpace>()
                .eq(ObjectUtil.isNotEmpty(tenantId), ParkSpace::getTenantId, tenantId)
                .in(ParkSpace::getParentSpaceId, targetParentIds));
        if (CollectionUtil.isEmpty(childSpaceList)) {
            return validSpaceList;
        }
        List<Long> allSpaceIds = workOrderRepository.getSpaceIdList(tenantId);
        if (CollectionUtil.isEmpty(allSpaceIds)) {
            return validSpaceList; // 无工单统计数据，直接返回空
        }
        Map<Long, String> spaceIdToFullPath = this.buildSpaceIdToFullPathMap(allSpaceIds, tenantId);
        for (ParkSpace childSpace : childSpaceList) {
            Long spaceId = childSpace.getId();
            boolean flag = false;
            for (Long space : allSpaceIds) {
                String fullPath = spaceIdToFullPath.getOrDefault(space, "");
                List<Long> spaceIds = pathToLongList(fullPath);
                if (spaceIds.contains(spaceId)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                SpaceViewModel spaceModel = new SpaceViewModel();
                spaceModel.setId(spaceId);
                spaceModel.setName(childSpace.getSpaceName());
                spaceModel.setSslcCode(childSpace.getSslcCode());
                validSpaceList.add(spaceModel); // 修复原代码：添加对象到结果集
            }
        }
        return validSpaceList;
    }

    @Override
    public IPage<WorkOrderSimpleModel> page(WorkOrderPageSimpleParam param) {
        IPage<WorkOrder> page = new Page<>(param.getCurrent(), param.getSize());
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        if(param.getDeviceIdList() == null){
            param.setDeviceIdList(new ArrayList<>());
        }
        if(param.getDeviceId() != null){
            param.getDeviceIdList().add(param.getDeviceId());
        }
        if(StringUtils.isNotEmpty(param.getBranchType())){
            //根据分支类型查出分支信息
            List<EnergyBranch> branches = energyBranchRepository.selectList(Wrappers.<EnergyBranch>lambdaQuery()
                    .eq(EnergyBranch::getBranchType,param.getBranchType()).eq(EnergyBranch::getStatus, Status.enabled.getKey())
                    .eq(EnergyBranch::getTenantId,WebFrameworkUtils.getHeaderTenantId()).eq(EnergyBranch::getDeleted, Delete.NORMAL.getKey()).select(EnergyBranch::getId));
            List<Long> branchIds = branches.stream().map(EnergyBranch::getId).collect(Collectors.toList());
            if(!CollectionUtil.isEmpty(branchIds)){
                List<BranchDevice> branchDevices = branchDeviceRepository.selectList(Wrappers.<BranchDevice>lambdaQuery()
                        .in(BranchDevice::getBranchId,branchIds).eq(BranchDevice::getDeleted,Delete.NORMAL.getKey()).select(BranchDevice::getDeviceId));
                List<Long> deviceIds = branchDevices.stream().map(BranchDevice::getDeviceId).distinct().collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(deviceIds)){
                    param.getDeviceIdList().addAll(deviceIds);
                }
            }
        }
        IPage<WorkOrder> pageDate =  workOrderRepository.selectPageByParam(page, BeanUtils.convertTo(param, WorkOrderPageParam::new));
        List<WorkOrderSimpleModel> workOrderModels = BeanUtils.convertListTo(pageDate.getRecords(), WorkOrderSimpleModel::new);
        return ConvertUtil.pageConvert(pageDate.getCurrent(), pageDate.getTotal(), pageDate.getSize(), workOrderModels);
    }

    @Override
    public IPage<WorkEvaluateModel> pageUnsatisfied(WorkUnsatisfiedPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<String> sourceList = Arrays.asList("meterPlan", "maintainPlan", "inspectionPlan", "patrolPlan", "inventoryPlan");
        if(sourceList.contains(param.getSource())){
            param.setSourceList(sourceList);
            param.setSource(null);
        }
        IPage<WorkEvaluateModel> page = new Page<>(param.getCurrent(), param.getSize());
        return workOrderRepository.pageEvaluate(page,param);
    }

    @Override
    public Double getWorkCompleteRate() {
        return workOrderRepository.getWorkCompleteRate(WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<Long> getProblemDeviceIds() {
        return workOrderRepository.getProblemDeviceIds(WebFrameworkUtils.getHeaderTenantId());
    }

    /**
     * 辅助方法：构建来源分析模型列表（修复原逻辑BUG，基于当前空间数据分组）
     * @param analysisModels 单个空间的分析模型列表
     * @return 来源分析模型列表
     */
    private void buildSourceAnalysisModels(Map<String, Integer> sourceMap, List<WorkOrderScreenSpaceAnalysisModel> analysisModels) {
        if (CollectionUtil.isEmpty(analysisModels)) {
            return;
        }
        // 按来源名称分组（基于当前空间数据，原代码错误使用全局models）
        Map<String, List<WorkOrderScreenSpaceAnalysisModel>> sourceNameToModels = analysisModels.stream()
                .collect(Collectors.groupingBy(WorkOrderScreenSpaceAnalysisModel::getName));
        // 转换为来源分析模型
        for(Map.Entry<String, List<WorkOrderScreenSpaceAnalysisModel>> entry : sourceNameToModels.entrySet()){
            // 累加当前来源的总数
            int total =  sourceMap.getOrDefault(entry.getKey(), 0) + entry.getValue().stream()
                    .mapToInt(WorkOrderScreenSpaceAnalysisModel::getTotalNum)
                    .sum();

            sourceMap.put(entry.getKey(), total);
        }
//         sourceNameToModels.entrySet().stream()
//                .map(entry -> {
//                    WorkOrderSourceAnalysisModel sourceModel = new WorkOrderSourceAnalysisModel();
//                    sourceModel.setName(entry.getKey());
//                    // 累加当前来源的总数
//                    int total =  sourceMap.getOrDefault(entry.getKey(), 0) + entry.getValue().stream()
//                            .mapToInt(WorkOrderScreenSpaceAnalysisModel::getTotalNum)
//                            .sum();
//
//                    sourceModel.setTotalNum(total);
//                    sourceMap.put(entry.getKey(), total);
//                    return sourceModel;
//                });
    }

    /**
     * 辅助方法：获取根空间ID（parentSpaceId=0的第一个空间）
     * @param tenantId 租户ID
     * @return 根空间ID/NULL
     */
    private Long getRootSpaceId(Long tenantId) {
        List<ParkSpace> rootSpaceList = parkSpaceService.list(new LambdaQueryWrapper<ParkSpace>()
                .eq(ObjectUtil.isNotEmpty(tenantId), ParkSpace::getTenantId, tenantId)
                .eq(ParkSpace::getParentSpaceId, 0));
        return CollectionUtil.isNotEmpty(rootSpaceList) ? rootSpaceList.get(0).getId() : null;
    }

    /**
     * 辅助方法：获取根空间ID（parentSpaceId=0的第一个空间）
     * @param tenantId 租户ID
     * @return 根空间ID/NULL
     */
    private Long getRootSpaceIdByCode(Long tenantId, String sslcCode) {
        List<ParkSpace> rootSpaceList = parkSpaceService.list(new LambdaQueryWrapper<ParkSpace>()
                .eq(ObjectUtil.isNotEmpty(tenantId), ParkSpace::getTenantId, tenantId)
                .eq(ParkSpace::getSslcCode, sslcCode));
        return CollectionUtil.isNotEmpty(rootSpaceList) ? rootSpaceList.get(0).getId() : null;
    }
    /**
     * 辅助方法：构建空间ID->全路径的映射（避免主方法冗余）
     * @param spaceIds 空间ID列表
     * @param tenantId 租户ID
     * @return 空间ID:全路径
     */
    private Map<Long, String> buildSpaceIdToFullPathMap(List<Long> spaceIds, Long tenantId) {
        if (CollectionUtil.isEmpty(spaceIds)) {
            return new HashMap<>();
        }
        Map<Long, ParkSpaceFullModel> spaceFullMap = parkSpaceService.findFullSpaceMap(spaceIds, tenantId);
        if (CollectionUtil.isEmpty(spaceFullMap)) {
            return new HashMap<>();
        }
        // 转换为空间ID->全路径，自动过滤空值
        return spaceFullMap.entrySet().stream()
                .filter(e -> ObjectUtil.isNotEmpty(e.getValue()) && ObjectUtil.isNotEmpty(e.getValue().getIdFullPath()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getIdFullPath(),
                        (k1, k2) -> k1 // 重复ID取第一个，避免冲突
                ));
    }

    private List<Long> pathToLongList(String path) {
        if (StrUtil.isBlank(path)) {
            return new ArrayList<>();
        }
        return  Arrays.stream(StrUtil.split(path, "-"))
                .filter(StrUtil::isNotBlank)
                .map(str -> {
                    try {
                        return Long.parseLong(str.trim());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }
}
