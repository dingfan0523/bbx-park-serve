package com.cgnpc.bbxpark.ioc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.common.enums.MaterialTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.property.domain.Material;
import com.cgnpc.bbxpark.property.mapper.MaterialRepository;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.service.IMaterialScreenService;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 大屏材料统计接口实现
 */
@Service
public class MaterialScreenServiceImpl implements IMaterialScreenService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private IParkSpaceService parkSpaceService;

    @Autowired
    private WorkOrderRepository workOrderRepository;


    @Override
    public MaterialInventoryAnalysisModel getMaterialInventory(Long type) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        MaterialInventoryAnalysisModel model = new MaterialInventoryAnalysisModel();
        List<MaterialInventoryTypeModel> typeModels = materialRepository.getMaterialTypeCount(tenantId);
        if(CollectionUtil.isEmpty(typeModels)){
            return model;
        }
        model.setInventoryTypeModels(typeModels);
        model.setTotalNum(typeModels.stream().mapToInt(MaterialInventoryTypeModel::getTotalNum).sum());
        if(ObjectUtil.isEmpty(type)){
            return model;
        }
        List<Material> materials = materialRepository.selectList(Wrappers.<Material>lambdaQuery()
                .eq(Material::getMaterialType, type)
                .eq(Material::getDeleted, Status.enabled.getKey())
                .eq(ObjectUtil.isNotEmpty(tenantId), Material::getTenantId, tenantId)
                .orderByDesc(Material::getStockQuantity));
        List<MaterialInventoryTypeModel> nameModels = materials.stream().map(item->{
            MaterialInventoryTypeModel name = new MaterialInventoryTypeModel();
            name.setName(item.getMaterialName());
            name.setStatus(item.getStockStatus());
            name.setTotalNum(item.getStockQuantity());
            return name;
        }).collect(Collectors.toList());
        model.setInventoryTypeModels(nameModels);
        return model;
    }

    @Override
    public List<MaterialInventoryHealthModel> getMaterialInventoryHealth() {
        List<String> names = Arrays.asList("库存充足","库存不足","库存缺货");
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        Integer total = materialRepository.selectCount(Wrappers.<Material>lambdaQuery()
                .eq(Material::getDeleted, Status.enabled.getKey())
                .eq(ObjectUtil.isNotEmpty(tenantId), Material::getTenantId, tenantId));
        List<MaterialInventoryHealthModel> materials = materialRepository.getMaterialInventoryHealth(tenantId);
        Map<String, Integer> materialMap = CollectionUtil.isEmpty(materials) ?
                new HashMap<>() : materials.stream().collect(Collectors.toMap(MaterialInventoryHealthModel::getName, MaterialInventoryHealthModel::getTotalNum));
        return  names.stream().map(name->{
            MaterialInventoryHealthModel model = new MaterialInventoryHealthModel();
            model.setName(name);
            model.setTotalNum(materialMap.getOrDefault(name, 0));
            if(total == null || total == 0){
                model.setNumRate(0d);
            }else{
                model.setNumRate(Math.round((double) model.getTotalNum() / total * 10000) / 100.0);
            }
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MaterialTurnoverModel> getMaterialTurnover() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<Material> materials = materialRepository.selectList(Wrappers.<Material>lambdaQuery()
                .eq(Material::getDeleted, Status.enabled.getKey())
                .in(Material::getMaterialType, Arrays.asList(2,3))
                .eq(ObjectUtil.isNotEmpty(tenantId), Material::getTenantId, tenantId));
        if(CollectionUtil.isEmpty(materials)){
            return Collections.emptyList();
        }
        return materials.stream().map(item->{
            MaterialTurnoverModel model = new MaterialTurnoverModel();
            model.setName(item.getMaterialName());
            model.setTotalNum(item.getStockQuantity());
            model.setOutCount(item.getOutbound());
            if(model.getTotalNum() == 0 || item.getOutboundQuantity() == 0){
                model.setTurnoverRate(0d);
            }else{
                model.setTurnoverRate(Math.round((double) item.getOutboundQuantity() / model.getTotalNum() * 10000) / 100.0);
            }
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MaterialWorkUseModel> getMaterialWorkUse() {
        List<MaterialWorkUseModel> materialWorkUseModels = workOrderRepository.getMaterialWorkUse(WebFrameworkUtils.getHeaderTenantId());
        Map<String, Integer> useMap = CollectionUtil.isEmpty(materialWorkUseModels) ?
                new HashMap<>() : materialWorkUseModels.stream().collect(Collectors.toMap(MaterialWorkUseModel::getName, MaterialWorkUseModel::getTotalNum));
        List<String> typeNames = Arrays.asList("合同计划","报事报修","设备告警","业主任务");
        return  typeNames.stream().map(name->{
            MaterialWorkUseModel use = new MaterialWorkUseModel();
            use.setName(name);
            use.setTotalNum(useMap.getOrDefault(name, 0));
            return use;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MaterialSmartSuggestModel> getMaterialSmartSuggest() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<Material> materials = materialRepository.selectList(Wrappers.<Material>lambdaQuery()
                .eq(Material::getDeleted, Status.enabled.getKey())
                .in(Material::getMaterialType, Arrays.asList(2,3))
                .eq(ObjectUtil.isNotEmpty(tenantId), Material::getTenantId, tenantId)
                .orderByDesc(Material::getCreateTime));
        if(CollectionUtil.isEmpty(materials)){
            return Collections.emptyList();
        }
        return materials.stream().map(item->{
            MaterialSmartSuggestModel model = new MaterialSmartSuggestModel();
            model.setName(item.getMaterialName());
            model.setQuantityNum(item.getStockQuantity());
            model.setWarningNum(item.getStockWarning());
            if(model.getQuantityNum() == 0 || item.getOutboundQuantity() == 0){
                model.setTurnoverDay(0);
            }else{
                double rate = Math.round((double) item.getOutboundQuantity() / model.getQuantityNum() * 10000) / 10000.0;
                if(rate <= 0d){
                    model.setTurnoverDay(0);
                }else{
                    model.setTurnoverDay((int) Math.round((double)365 / rate));
                }
            }
            if( model.getTurnoverDay() == 0){
                model.setSuggest("--");
            }else if( model.getTurnoverDay() <= 10){
                model.setSuggest("增加采购");
            }else if(model.getTurnoverDay() > 40){
                model.setSuggest("减少采购");
            }else{
                model.setSuggest("维持现状");
            }
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MaterialTurnoverRankModel> getMaterialTurnoverRank(Long type) {
        if(ObjectUtil.isEmpty(type)) {
            type = 1L;
        }
        return materialRepository.getMaterialTurnoverRank(WebFrameworkUtils.getHeaderTenantId(), type);
    }

    @Override
    public List<MaterialUsageRankModel> getMaterialUsageRank(Long type) {
        if(ObjectUtil.isEmpty(type)) {
            type = 1L;
        }
        return materialRepository.getMaterialUsageRank(WebFrameworkUtils.getHeaderTenantId(), type);
    }

    @Override
    public List<MaterialSpaceCountModel> getMaterialSpaceCount(String sslcCode) {
        AssertUtils.isNotEmpty(sslcCode, "楼层编码不能为空");
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<MaterialSpaceCountModel> validSpaceList = new ArrayList<>();
        Long targetParentId = this.getRootSpaceIdByCode(tenantId, sslcCode);
        if (ObjectUtil.isNull(targetParentId)) {
            return validSpaceList;
        }
        List<ParkSpace> childSpaceList = parkSpaceService.list(new LambdaQueryWrapper<ParkSpace>()
                .eq(ObjectUtil.isNotEmpty(tenantId), ParkSpace::getTenantId, tenantId)
                .eq(ParkSpace::getParentSpaceId, targetParentId));
        if (CollectionUtil.isEmpty(childSpaceList)) {
            return validSpaceList;
        }
        List<Long> childSpaceIds = childSpaceList.stream().map(ParkSpace::getId).collect(Collectors.toList());
        List<MaterialSpaceCountModel> spaceAnalysisModels = materialRepository.getMaterialSpaceCount(tenantId);
        if (CollectionUtil.isEmpty(spaceAnalysisModels)) {
            return validSpaceList;
        }
        Map<Long, List<MaterialSpaceCountModel>> spaceIdToAnalysisModels = spaceAnalysisModels.stream()
                .collect(Collectors.groupingBy(MaterialSpaceCountModel::getId));
        List<Long> allSpaceIds = new ArrayList<>(spaceIdToAnalysisModels.keySet());
        childSpaceIds.addAll(allSpaceIds);
        Map<Long, ParkSpaceFullModel> spaceFullMap = parkSpaceService.findFullSpaceMap(childSpaceIds, tenantId);
        for (ParkSpace childSpace : childSpaceList) {
            Long spaceId = childSpace.getId();
            int totalNum = 0;
            for (Map.Entry<Long, List<MaterialSpaceCountModel>> entry : spaceIdToAnalysisModels.entrySet()) {
                Long analysisSpaceId = entry.getKey();
                String fullPath = ObjectUtil.isEmpty(spaceFullMap.get(analysisSpaceId)) ? "" : spaceFullMap.get(analysisSpaceId).getIdFullPath();
                List<Long> spaceIds = pathToLongList(fullPath);
                if (spaceIds.contains(spaceId)) {
                    totalNum = totalNum + entry.getValue().stream()
                            .mapToInt(MaterialSpaceCountModel::getTotalNum)
                            .sum();
                }
            }
            if (totalNum > 0) {
                MaterialSpaceCountModel spaceModel = new MaterialSpaceCountModel();
                spaceModel.setId(spaceId);
                spaceModel.setName(ObjectUtil.isEmpty(spaceFullMap.get(spaceId)) ? "" : spaceFullMap.get(spaceId).getFullPath());
                spaceModel.setSslcCode(childSpace.getSslcCode());
                spaceModel.setTotalNum(totalNum);
                validSpaceList.add(spaceModel); // 修复原代码：添加对象到结果集
            }
        }
        return validSpaceList;
    }

    @Override
    public List<SpaceViewModel> getMaterialSpaceView(String sslcCode) {
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
        List<Long> allSpaceIds = materialRepository.getSpaceIdList(tenantId);
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
                validSpaceList.add(spaceModel);
            }
        }
        return validSpaceList;
    }

    @Override
    public Double getMaterialAlarmRate() {
        return materialRepository.getMaterialAlarmRate(WebFrameworkUtils.getHeaderTenantId());
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
                        (k1, k2) -> k1
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
