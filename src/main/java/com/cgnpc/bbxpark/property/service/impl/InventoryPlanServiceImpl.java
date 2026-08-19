package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.constant.WorkOrderCodePrefixConstant;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.IocDeviceListParam;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.property.domain.InspectionPlan;
import com.cgnpc.bbxpark.property.domain.InventoryItem;
import com.cgnpc.bbxpark.property.domain.InventoryPlan;
import com.cgnpc.bbxpark.property.domain.Material;
import com.cgnpc.bbxpark.property.dto.model.*;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.mapper.InventoryPlanRepository;
import com.cgnpc.bbxpark.property.service.IInventoryItemService;
import com.cgnpc.bbxpark.property.service.IInventoryPlanService;
import com.cgnpc.bbxpark.property.service.IMaterialService;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleService;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkTask;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkTaskParam;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderService;
import com.cgnpc.bbxpark.workorder.service.IWorkTaskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 盘点计划管理服务实现
 */
@Slf4j
@Service
public class InventoryPlanServiceImpl extends ServiceImpl<InventoryPlanRepository, InventoryPlan> implements IInventoryPlanService {
	@Resource
	private IInventoryItemService inventoryItemService;
	@Resource
	private IPropertyScheduleService propertyScheduleService;
	@Resource
	private IWorkOrderService workOrderService;
//	@Resource
//	private IUserInfoCommonService userInfoCommonService;
    @Autowired
    private IUserApiService userApiService;
	@Resource
	private IMaterialService materialService;
	@Resource
	private IIocDeviceService iocDeviceService;

    @Resource
    private IWorkTaskService workTaskService;

    @Autowired
    private IParkSpaceService parkSpaceService;

	/**
	 * 根据盘点计划管理标识获得盘点计划管理详情信息.
	 * @Param [id] 盘点计划管理标识
	 * @Return 盘点计划管理详情信息
	 */
	@Override
	public InventoryPlanModel detail(Long id) {
		InventoryPlan inventoryPlan = this.getById(id);
		AssertUtils.notNull(inventoryPlan, SystemResultCode.RESULT_DATA_NONE.message());
		InventoryPlanModel model = BeanUtils.convertTo(inventoryPlan, InventoryPlanModel::new);
		List<InventoryItem> itemList = inventoryItemService.list(Wrappers.<InventoryItem>lambdaQuery().eq(InventoryItem::getInventoryId,id));
		//查询所有相关联的材料信息
		InventoryPlanItemParam param = new InventoryPlanItemParam();
		param.setId(id);
		List<MaterialItemModel> materialList = findMaterialList(param);
		List<DeviceItemModel> deviceList = findDeviceList(param);
		Map<Long, MaterialItemModel> materialMap = materialList.stream().collect(Collectors.toMap(MaterialItemModel::getId, item->item));
		Map<Long, DeviceItemModel> deviceMap = deviceList.stream().collect(Collectors.toMap(DeviceItemModel::getId, item->item));
		//数据组装
		List<InventoryItemModel> list = itemList.stream().map(item->{
			InventoryItemModel itemModel = BeanUtils.convertTo(item, InventoryItemModel::new);
			//查询相关联的资产信息
			if(StringUtils.isNotEmpty(item.getRelatedId())){
				List<Long> ids = Arrays.stream(item.getRelatedId().split(",")).map(Long::parseLong).collect(Collectors.toList());
				if(item.getType() == 1){
					itemModel.setRelatedList(ids.stream().map(relatedId->{
						ItemRelated itemRelated = new ItemRelated();
						itemRelated.setRelatedId(relatedId);
						itemRelated.setRelatedName(materialMap.get(relatedId).getMaterialName());
						return itemRelated;
					}).collect(Collectors.toList()));
				}else {
					itemModel.setRelatedList(ids.stream().map(relatedId->{
						ItemRelated itemRelated = new ItemRelated();
						itemRelated.setRelatedId(relatedId);
						itemRelated.setRelatedName(deviceMap.get(relatedId).getDeviceName());
						return itemRelated;
					}).collect(Collectors.toList()));
				}
			}
			return itemModel;
		}).collect(Collectors.toList());
		model.setItems(list);
//		Optional.ofNullable(itemList).filter(list -> !list.isEmpty()).ifPresent(list -> model.setItems(BeanUtils.convertListTo(list, InventoryItemModel::new)));
		return model;
	}

	/**
	 * 获取盘点计划管理列表(分页).
	 * @Param param 盘点计划管理查询条件
	 * @Return 盘点计划管理信息列表（分页）
	 */
	@Override
	public IPage<InventoryPlanListModel> page(InventoryPlanPageParam param) {
		IPage<InventoryPlan> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(BeanUtils.convertTo(param, InventoryPlanListParam::new)));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(result.getPages(),result.getTotal(),result.getSize(),convert(result.getRecords()));
	}

	/**
	 * 获取盘点计划管理列表.
	 * @Param param 盘点计划管理查询条件
	 * @Return 盘点计划管理信息列表
	 */
	@Override
	@SneakyThrows
	public List<InventoryPlanListModel> list(InventoryPlanListParam param) {
		List<InventoryPlan> plans = this.list(buildQuery(param));
		return BeanUtils.convertListTo(plans, InventoryPlanListModel::new);
	}

	/**
	 * 新增盘点计划管理.
	 * @Param param 盘点计划管理信息
	 * @Return 新增盘点计划管理是否成功
	 */
	@Override
	public Boolean add(InventoryPlanParam param) {
		// 校验盘点计划明细参数
		validateInventoryItems(param.getInventoryItemParams());
		
		InventoryPlan inventoryPlan = BeanUtils.convertTo(param, InventoryPlan::new);
		//创建人
		UserInfoModel userInfo = userApiService.getCurrentUserInfo();
        inventoryPlan.setCreateBy(userInfo.getUserName());
		processUserInfo(inventoryPlan);
		inventoryPlan.setId(null);
		if(save(inventoryPlan)){
			return processItem(inventoryPlan.getId(),param.getInventoryItemParams());
		}
		return false;
	}

	/**
	 * 编辑盘点计划管理信息.
	 * @Param param 盘点计划管理信息
	 * @Return 编辑盘点计划管理是否成功
	 */
	@Override
	public Boolean edit(Long id, InventoryPlanParam param) {
		// 校验盘点计划明细参数
		validateInventoryItems(param.getInventoryItemParams());
		
		InventoryPlan inventoryPlan = this.getById(id);
		AssertUtils.notNull(inventoryPlan, SystemResultCode.RESULT_DATA_NONE.message());
		BeanUtils.copyProperties(param,inventoryPlan);
		processUserInfo(inventoryPlan);
		if(updateById(inventoryPlan)){
			//先删除关联的信息
			inventoryItemService.remove(inventoryPlan.getId());
			return processItem(inventoryPlan.getId(),param.getInventoryItemParams());
		}
		return false;
	}

	/**
	 * 删除盘点计划管理.
	 * @Param id 盘点计划管理标识
	 * @Return 删除盘点计划管理是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		InventoryPlan inventoryPlan = this.getById(id);
		AssertUtils.notNull(inventoryPlan, SystemResultCode.RESULT_DATA_NONE.message());
		inventoryPlan.setDeleted(Delete.DELETED.getKey());
		return updateById(inventoryPlan);
	}

	@Override
	public Boolean statusEdit(InspectionPointStatusParam param) {
		InventoryPlan patrolPlan = this.getById(param.getId());
		AssertUtils.notNull(patrolPlan, SystemResultCode.RESULT_DATA_NONE);
		patrolPlan.setStatus(param.getStatus());
		return updateById(patrolPlan);
	}

	@Override
	public IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param) {
		param.setSource(WorkOrderSourceEnum.INVENTORYPLAN.getCode());
		return workOrderService.businessPage(param);
	}

    @Override
    public Boolean executePlan(Map<Long, String> scheduleMap) {
        List<InventoryPlan> plans = this.getBaseMapper().selectListBySql();
        if(CollectionUtil.isEmpty(plans)){
            return false;
        }
        Date currentDate = DateUtil.date();
        Map<Long, List<InventoryPlan>> planMap = plans.stream().collect(Collectors.groupingBy(InventoryPlan::getTenantId));
        for(Map.Entry<Long,List<InventoryPlan>> entry : planMap.entrySet()){
            //获取工单数量
            Long number = workOrderService.queryWorkOrderCount(WorkOrderSourceEnum.INVENTORYPLAN.getCode(), entry.getKey());
            for (InventoryPlan plan : entry.getValue()) {
                log.info("盘点计划信息{}", JSON.toJSONString(plan));
                // 判断当前日期是否满足计划的周期条件
                if (!PlanDateUtil.isDateValid(currentDate, plan.getPeriodType(), plan.getPeriodSign(), plan.getPeriodStartTime(), plan.getPlanPeriod())) {
                    log.info("盘点计划不符合生成工单规则,计划id：{}", plan.getId());
                    continue; // 如果不满足条件，跳过该计划
                }
                try {
                    number++;
                    WorkPlanModel planModel = BeanUtils.convertTo(plan, WorkPlanModel::new);
                    planModel.setPlanType(WorkOrderTypeEnum.INVENTORYPLAN.getCode());
                    planModel.setPlanSource(WorkOrderSourceEnum.INVENTORYPLAN.getCode());
                    planModel.setPlanCode(PlanDateUtil.getCode(WorkOrderCodePrefixConstant.PD,  number));
                    planModel.setScheduleName(ObjectUtil.isNotEmpty(scheduleMap) ? scheduleMap.get(plan.getScheduleId()) : null);
                    //创建计划工单
                    WorkOrder workOrder = workOrderService.addPlanWordOrder(planModel, null);
                    //保存工单任务
                    addWorkTask(workOrder, planModel);
                } catch (Exception e) {
                    log.error("盘点计划生成工单失败", e);
                }
            }
        }
        return true;
    }

    private void addWorkTask(WorkOrder workOrder, WorkPlanModel planModel){
        List<InventoryItem> items = inventoryItemService.list(Wrappers.<InventoryItem>lambdaQuery().eq(InventoryItem::getInventoryId, planModel.getId()));
        if (CollectionUtil.isEmpty(items)) {
            return;
        }
        // 获取设备ID集合
        List<Long> deviceIds =  items.stream().filter(item -> Integer.valueOf(2).equals( item.getType()) && StringUtils.isNotEmpty(item.getRelatedId()))
                .flatMap(item -> Arrays.stream(item.getRelatedId().split(",")))
                .map(Long::valueOf).collect(Collectors.toList());
        // 获取材料ID集合
        List<Long> materialIds =  items.stream().filter(item -> Integer.valueOf(1).equals( item.getType()) && StringUtils.isNotEmpty(item.getRelatedId()))
                .flatMap(item -> Arrays.stream(item.getRelatedId().split(",")))
                .map(Long::valueOf).collect(Collectors.toList());

        Map<Long, IocDeviceModel> deviceMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(deviceIds)){
            IocDeviceListParam deviceParam = new IocDeviceListParam();
            deviceParam.setDeviceIdList(deviceIds);
            List<IocDeviceModel> deviceModels = findDeviceList(deviceParam);
            deviceMap = CollectionUtil.isEmpty(deviceModels) ? new HashMap<>() : deviceModels.stream().collect(Collectors.toMap(IocDeviceModel::getId, v -> v));
        }
        Map<Long, Material> materialMap = new HashMap<>();
        Map<Long, ParkSpaceFullModel> materialFullSpaceMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(materialIds)){
            List<Material> materials = materialService.list(Wrappers.<Material>lambdaQuery().in(Material::getId, materialIds).eq(Material::getDeleted, Status.enabled.getKey()));
            materialMap = CollectionUtil.isEmpty(materials)?new HashMap<>(): materials.stream().collect(Collectors.toMap(Material::getId, material -> material));
            List<Long> spaceIds = materials.stream().map(Material::getSpaceId).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            materialFullSpaceMap = CollectionUtil.isEmpty(spaceIds) ? new HashMap<>() : parkSpaceService.findFullSpaceMap(spaceIds, null);
        }
        int groupSort = 0;
        List<WorkTask> params = new ArrayList<>();
        for(InventoryItem item : items){
            List<Long> relatedIdList = Arrays.stream(item.getRelatedId().split(",")).map(Long::parseLong).collect(Collectors.toList());
            for(Long relatedId : relatedIdList){
                WorkTask param = new WorkTask();
                param.setWorkId(workOrder.getId());
                param.setTenantId(planModel.getTenantId());
                param.setTaskGroup(groupSort);
                param.setBusinessId(relatedId);
                if(Integer.valueOf(2).equals(item.getType())){
                    param.setBusinessType(WorkTaskTypeEnum.INVENTORY_DEVICE.getCode());
                    if(ObjectUtil.isNotEmpty(deviceMap.get(relatedId))) {
                        IocDeviceModel device = deviceMap.get(relatedId);
                        param.setName(device.getDeviceName());
                        param.setCode(device.getDeviceCode());
                        param.setCategory(device.getDeviceCategory());
                        param.setSpaceId(device.getSpaceId());
                        param.setSpaceFullPath(device.getSpaceName());

                    }
                }else{
                    param.setBusinessType(WorkTaskTypeEnum.INVENTORY_MATERIAL.getCode());
                    if(ObjectUtil.isNotEmpty(materialMap.get(relatedId))) {
                        Material material = materialMap.get(relatedId);
                        param.setName(material.getMaterialName());
                        param.setCode(material.getMaterialCode());
                        param.setCategory(material.getMaterialType());
                        param.setRedundancyOne(String.valueOf(material.getStockQuantity()));
                        param.setSpaceId(material.getSpaceId());
                        param.setSpaceFullPath(ObjectUtil.isNotEmpty(materialFullSpaceMap.get(material.getSpaceId())) ? null : materialFullSpaceMap.get(material.getSpaceId()).getFullPath());
                    }
                }
                param.setRemark(item.getContent());
                param.setTenantId(workOrder.getTenantId());
                param.setCreateTime(new Date());
                param.setCreateBy(workOrder.getCreateBy());
                param.setCreatorId(workOrder.getCreatorId());
                param.setUpdateBy(workOrder.getCreateBy());
                param.setUpdatorId(workOrder.getCreatorId());
                param.setUpdateTime(new Date());
                params.add(param);
            }
            groupSort++;
        }
        workTaskService.saveBatch(params);
    }

    private List<IocDeviceModel> findDeviceList(IocDeviceListParam param){
        param.setAuth(false);
        List<IocDeviceModel> iocDevices = iocDeviceService.findList(param);
        Map<Long,IocDeviceModel> iocDeviceMap =CollectionUtil.isEmpty(iocDevices)?new HashMap<>(): iocDevices.stream().collect(Collectors.toMap(IocDeviceModel::getId, iocDevice -> iocDevice));
        List<IocDeviceModel> iocDeviceList = new ArrayList<>();
        // 对deviceIdList进行去重处理
        param.getDeviceIdList().forEach(deviceId->{
            IocDeviceModel iocDevice = iocDeviceMap.get(deviceId);
            if(iocDevice != null){
                iocDeviceList.add(iocDevice);
            }else{
                IocDeviceModel device = new IocDeviceModel();
                device.setId(deviceId);
                iocDeviceList.add(device);
            }
        });
        return iocDeviceList;
    }
	/**
	 * 查询盘点计划下材料列表
	 * @param params 查询参数
	 * @return 材料列表
	 */
	@Override
	public List<MaterialItemModel> findMaterialList(InventoryPlanItemParam params) {
		//获取关联ID集合
		List<Long> relatedIds = CollectionUtils.isEmpty(params.getRelatedIdList()) ? findItemIds(params.getId(), 1) : params.getRelatedIdList();
		// 根据关联ID查询材料列表
		List<Material> materials = CollectionUtil.isEmpty(relatedIds) ? Collections.emptyList() : materialService.list(Wrappers.<Material>lambdaQuery().in(Material::getId, relatedIds).eq(Material::getDeleted, Status.enabled.getKey()));
		// 创建一个Map方便查找
		Map<Long, Material> materialMap = materials.stream().collect(Collectors.toMap(Material::getId, material -> material));

		// 按照relatedIds顺序构建返回列表
		return relatedIds.stream().map(id -> {
			Material material = materialMap.get(id);
			if(material != null){
				return BeanUtils.convertTo(material, MaterialItemModel::new);
			}
			//已经被删除的只返回基本字段
			MaterialItemModel deletedMaterial = new MaterialItemModel();
			deletedMaterial.setId(id);
			deletedMaterial.setDeleted(Status.disabled.getKey());
			return deletedMaterial;
		}).collect(Collectors.toList());
	}

	/**
	 * 查询盘点计划下设备列表
	 * @param params 查询参数
	 * @return 设备列表
	 */
	@Override
	public List<DeviceItemModel> findDeviceList(InventoryPlanItemParam params) {
		//获取关联ID集合
		List<Long> relatedIds = CollectionUtils.isEmpty(params.getRelatedIdList()) ? findItemIds(params.getId(), 2) : params.getRelatedIdList();
		// 根据关联ID查询设备列表
		IocDeviceListParam param = new IocDeviceListParam();
		param.setDeviceIdList(relatedIds);
		param.setDeleted(Delete.NORMAL.getKey());
		List<IocDeviceModel> devices = iocDeviceService.findList(param);
		// 创建一个Map方便查找
		Map<Long, IocDeviceModel> deviceMap = devices.stream().collect(Collectors.toMap(IocDeviceModel::getId, device -> device));

		// 按照relatedIds顺序构建返回列表
		return relatedIds.stream().map(id -> {
			IocDeviceModel device = deviceMap.get(id);
			if(device != null){
				return BeanUtils.convertTo(device, DeviceItemModel::new);
			}
			//已经被删除的只返回基本字段
			DeviceItemModel deletedDevice = new DeviceItemModel();
			deletedDevice.setId(id);
			deletedDevice.setDeleted(Delete.DELETED.getKey());
			return deletedDevice;
		}).collect(Collectors.toList());
	}

	/**
	 * 根据盘点计划ID和类型查询关联ID集合
	 * @param id 盘点计划ID
	 * @param type  类型
	 * @return 关联ID集合
	 */
	private List<Long> findItemIds(Long id, Integer type) {
		List<InventoryItem> items = inventoryItemService.list(Wrappers.<InventoryItem>lambdaQuery()
				.eq(InventoryItem::getInventoryId, id).eq(type != null,InventoryItem::getType, type));
		if (CollectionUtil.isEmpty(items)) {
			return Collections.emptyList();
		}
		// 获取关联ID集合
		return items.stream().filter(item -> StringUtils.isNotEmpty(item.getRelatedId()))
				.flatMap(item -> Arrays.stream(item.getRelatedId().split(",")))
				.map(Long::valueOf).collect(Collectors.toList());
	}

	/**
	 * 校验盘点计划明细参数
	 * @param items 盘点计划明细参数列表
	 */
	private void validateInventoryItems(List<InventoryItemParam> items) {
		if (CollectionUtils.isEmpty(items)) {
			return;
		}
		// 用于记录每个type下已经出现的relatedId
		Map<Integer, Set<String>> typeRelatedIdsMap = new HashMap<>(4);

		for (InventoryItemParam item : items) {
			if (item.getRelatedId() == null || item.getRelatedId().isEmpty()) {
				continue;
			}
			// 检查当前item的relatedId中是否有重复
			String[] relatedIds = item.getRelatedId().split(",");
			Set<String> relatedIdSet = new HashSet<>(Arrays.asList(relatedIds));
			// 如果转换为Set后大小不一致，说明有重复
			AssertUtils.isFalse(relatedIdSet.size() != relatedIds.length,"请勿重复选择资产");
			// 检查相同type下是否有重复的relatedId
			Integer type = item.getType();
			if (type != null) {
				Set<String> existingRelatedIds = typeRelatedIdsMap.computeIfAbsent(type, k -> new HashSet<>());
				for (String relatedId : relatedIds) {
					AssertUtils.isFalse(existingRelatedIds.contains(relatedId),"请勿重复选择资产");
					existingRelatedIds.add(relatedId);
				}
			}
		}
	}

	/**
	 * 处理用户信息.
	 * @param inventoryPlan 盘点计划管理信息
	 */
	private void processUserInfo(InventoryPlan inventoryPlan){
		if(ObjectUtil.isNotEmpty(inventoryPlan.getAuditUid())){
			UserInfoModel auditUser = userApiService.getByStaffNo(inventoryPlan.getAuditUid());
			inventoryPlan.setAuditUname(auditUser.getUserName());
			inventoryPlan.setAuditStaffid(auditUser.getStaffid());
		}
	}

	/**
	 * 批量处理盘点计划明细信息.
	 * @Param id 盘点计划管理标识
	 * @Param items 盘点计划划明细信息列表
	 * @Return 批量处理盘点计划划明细信息是否成功
	 */
	private boolean processItem(Long id, List<InventoryItemParam> items){
		if(!CollectionUtils.isEmpty(items)){
			items.forEach(item -> item.setInventoryId(id));
			return inventoryItemService.addBatch(items);
		}
		return true;
	}
	
	private LambdaQueryWrapper<InventoryPlan> buildQuery(InventoryPlanListParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		return Wrappers.<InventoryPlan>lambdaQuery().like(ObjectUtil.isNotEmpty(param.getPlanName()), InventoryPlan::getPlanName, param.getPlanName())
				.eq(param.getPlanPeriod() != null, InventoryPlan::getPlanPeriod, param.getPlanPeriod())
				.eq(param.getScheduleId() != null, InventoryPlan::getScheduleId, param.getScheduleId())
				.eq(param.getDispatchType() != null, InventoryPlan::getDispatchType, param.getDispatchType())
				.eq(param.getStatus() != null, InventoryPlan::getStatus, param.getStatus())
				.eq(tenantId != null,InventoryPlan::getTenantId, tenantId)
				.eq(InventoryPlan::getDeleted, Status.enabled.getKey())
				.orderByDesc(InventoryPlan::getCreateTime);
	}

	/**
	 * 批量转换返回模型
	 * @param plans 盘点计划管理模型列表
	 * @return 盘点计划管理模型列表
	 */
	private List<InventoryPlanListModel> convert(List<InventoryPlan> plans){
		if(CollectionUtil.isEmpty(plans)){
			return Collections.emptyList();
		}
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		Date currentDate = DateUtil.date();
		//盘点资产明细列表
		List<Long> ids = plans.stream().map(InventoryPlan::getId).collect(Collectors.toList());
		List<InventoryItem> items = inventoryItemService.list(Wrappers.<InventoryItem>lambdaQuery().in(InventoryItem::getInventoryId, ids).eq(tenantId != null,InventoryItem::getTenantId, tenantId));
		// 按inventoryId分组，并计算每个分组中relatedId按逗号分割后的数组长度之和
		Map<Long, Integer> itemCountMap = items.stream()
				.collect(Collectors.groupingBy(
						InventoryItem::getInventoryId,
						Collectors.collectingAndThen(
								Collectors.toList(),
								list -> list.stream().mapToInt(item -> {
											if (item.getRelatedId() != null && !item.getRelatedId().isEmpty()) {
												return item.getRelatedId().split(",").length;
											}
											return 0;
										}).sum()
						)
				));

		//物业分组信息
		List<Long> scheduleIds = plans.stream().map(InventoryPlan::getScheduleId).filter(Objects::nonNull).collect(Collectors.toList());

		Map<Long, String> scheduleMap = Collections.emptyMap();
		if (!scheduleIds.isEmpty()) {
			scheduleMap = propertyScheduleService.getScheduleNameMap(scheduleIds);
		}

		List<Long> planIds = plans.stream().map(InventoryPlan::getId).collect(Collectors.toList());
		List<WorkOrder> workOrders = workOrderService.list(Wrappers.<WorkOrder>lambdaQuery().ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode())
				.eq(WorkOrder::getSource, WorkOrderSourceEnum.PATROLPLAN.getCode())
				.in(WorkOrder::getBusinessId, planIds));
		Set<Long> businessIds = CollectionUtil.isEmpty(workOrders) ? Collections.emptySet() : workOrders.stream().map(WorkOrder::getBusinessId).collect(Collectors.toSet());

		//数据组装
		Map<Long, String> finalScheduleMap = scheduleMap;
		return plans.stream().map(plan -> {
			InventoryPlanListModel model = BeanUtils.convertTo(plan, InventoryPlanListModel::new);
			model.setMaterialCount(itemCountMap.getOrDefault(plan.getId(), 0));
			if(ObjectUtil.isNotEmpty(finalScheduleMap.get(model.getScheduleId()))){
				model.setScheduleName(finalScheduleMap.get(model.getScheduleId()));
			}
			if(Status.enabled.getKey().equals(model.getStatus())){
				model.setNextTime(PlanDateUtil.getNextDate(currentDate, model.getPeriodType(), model.getPeriodSign(),
						model.getPeriodStartTime(), model.getPlanPeriod()));
			}
			if (businessIds.contains(model.getId())){
				model.setWorkOrderFlag(true);
			}
			return model;
		}).collect(Collectors.toList());
	}
}