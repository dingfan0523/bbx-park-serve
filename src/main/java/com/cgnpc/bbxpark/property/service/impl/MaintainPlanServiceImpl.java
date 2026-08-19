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
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.IocDeviceListParam;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.property.domain.InventoryPlan;
import com.cgnpc.bbxpark.property.domain.MaintainDevice;
import com.cgnpc.bbxpark.property.domain.MaintainPlan;
import com.cgnpc.bbxpark.property.domain.Material;
import com.cgnpc.bbxpark.property.dto.model.*;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.mapper.MaintainPlanRepository;
import com.cgnpc.bbxpark.property.service.*;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.workorder.domain.WorkMaterial;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkTask;
import com.cgnpc.bbxpark.workorder.domain.WorkTaskItem;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkMaterialParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkTaskItemParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkTaskParam;
import com.cgnpc.bbxpark.workorder.service.IWorkMaterialService;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderService;
import com.cgnpc.bbxpark.workorder.service.IWorkTaskItemService;
import com.cgnpc.bbxpark.workorder.service.IWorkTaskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/***
 * @Description 维保计划管理服务实现
 * @author huangyongtao
 * @date 2025/10/16 15:12
 */
@Slf4j
@Service
public class MaintainPlanServiceImpl extends ServiceImpl<MaintainPlanRepository, MaintainPlan> implements IMaintainPlanService {

	@Autowired
	private IIocDeviceService iocDeviceService;

	@Autowired
	private IUserApiService userApiService;

	@Autowired
	private IWorkOrderService workOrderService;

	@Autowired
	private IPropertyScheduleService propertyScheduleService;

	@Autowired
	private IMaintainDeviceService maintainDeviceService;

	@Autowired
	private IMaintainMaterialService maintainMaterialService;

	@Autowired
	private IMaintainItemService maintainItemService;

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private IWorkMaterialService workMaterialService;

    @Autowired
    private IWorkTaskService workTaskService;

    @Autowired
    private IWorkTaskItemService workTaskItemService;

	/**
	 * 根据维保计划管理标识获得维保计划管理详情信息.
	 * @Param [id] 维保计划管理标识
	 * @Return 维保计划管理详情信息
	 */
	@Override
	public MaintainPlanModel detail(Long id) {
		MaintainPlan maintainPlan = this.getById(id);
		AssertUtils.notNull(maintainPlan, SystemResultCode.RESULT_DATA_NONE.message());
		MaintainPlanModel model = BeanUtils.convertTo(maintainPlan, MaintainPlanModel::new);
		handlePlanDetail(model);
		return model;
	}

	/**
	 * 获取维保计划管理列表(分页).
	 * @Param param 维保计划管理查询条件
	 * @Return 维保计划管理信息列表（分页）
	 */
	@Override
	public IPage<MaintainPlanModel> page(MaintainPlanPageParam param) {
		IPage<MaintainPlan> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<MaintainPlanModel> models = BeanUtils.convertListTo(result.getRecords(), MaintainPlanModel::new);
		handlePlanModel(models);
		return ConvertUtil.pageConvert(result, models);
	}

	/**
	 * 获取维保计划管理列表.
	 * @Param param 维保计划管理查询条件
	 * @Return 维保计划管理信息列表
	 */
	@Override
	@SneakyThrows
	public List<MaintainPlanModel> list(MaintainPlanListParam param) {
		List<MaintainPlan> maintainPlans = this.list(buildQuery(BeanUtils.convertTo(param, MaintainPlanPageParam::new)));
		return BeanUtils.convertListTo(maintainPlans, MaintainPlanModel::new);
	}

	/**
	 * 新增维保计划管理.
	 * @Param param 维保计划管理信息
	 * @Return 新增维保计划管理是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean add(MaintainPlanParam param) {
		checkParam(param);
		MaintainPlan maintainPlan = BeanUtils.convertTo(param, MaintainPlan::new);
		maintainPlan.setId(null);
		UserInfoModel user = userApiService.getCurrentUserInfo();
		maintainPlan.setCreatorId(WebFrameworkUtils.getHeaderUserId());
        maintainPlan.setCreateBy(user.getUserName());
		if(ObjectUtil.isNotEmpty(param.getAuditUid())){
			UserInfoModel auditUser = userApiService.getByStaffNo(param.getAuditUid());
			maintainPlan.setAuditUid(param.getAuditUid());
			maintainPlan.setAuditUname(auditUser.getUserName());
			maintainPlan.setAuditStaffid(auditUser.getStaffid());
		}
		this.save(maintainPlan);
		//处理材料
		handleMaterial(param, maintainPlan.getId());
		//处理任务组
		handleTaskGroup(param, maintainPlan.getId());
		return true;
	}

	/**
	 * 删除维保计划管理.
	 * @Param id 维保计划管理标识
	 * @Return 删除维保计划管理是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean remove(Long id) {
		MaintainPlan maintainPlan = this.getById(id);
		AssertUtils.notNull(maintainPlan, SystemResultCode.RESULT_DATA_NONE.message());
		MaintainPlan plan = new MaintainPlan();
		plan.setId(id);
		plan.setDeleted(Delete.DELETED.getKey());
		this.updateById(plan);
		maintainItemService.remove(id);
		maintainMaterialService.remove(id);
		maintainDeviceService.remove(id);
		return true;
	}


	/**
	 * 编辑维保计划管理信息.
	 * @Param param 维保计划管理信息
	 * @Return 编辑维保计划管理是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean edit(MaintainPlanParam param) {
		checkParam(param);
		MaintainPlan maintainPlan = this.getById(param.getId());
		AssertUtils.notNull(maintainPlan, SystemResultCode.RESULT_DATA_NONE.message());
		MaintainPlan editParam = BeanUtils.convertTo(param, MaintainPlan::new);
		this.updateById(editParam);
		maintainItemService.remove(param.getId());
		maintainMaterialService.remove(param.getId());
		maintainDeviceService.remove(param.getId());
		//处理材料
		handleMaterial(param, maintainPlan.getId());
		//处理任务组
		handleTaskGroup(param, maintainPlan.getId());
		return true;
	}

	/**
	 * 编辑启用状态维保计划管理.
	 * @Param id 维保计划管理标识
	 * @Return 启用维保计划管理是否成功
	 */
	@Override
	public Boolean statusEdit(MaintainPlanStatusParam param) {
		AssertUtils.notNull(param.getStatus(), "状态不能为空");
		MaintainPlan plan = this.getById(param.getId());
		AssertUtils.notNull(plan, SystemResultCode.RESULT_DATA_NONE.message());
		MaintainPlan maintainPlan = new MaintainPlan();
		maintainPlan.setId(param.getId());
		maintainPlan.setStatus(param.getStatus());
		return this.updateById(maintainPlan);
	}

	@Override
	public List<IocDeviceModel> findDeviceList(MaintainPlanDeviceParam param) {
		IocDeviceListParam deviceParam = new IocDeviceListParam();
		if(ObjectUtil.isEmpty(param.getId())){
			if(CollectionUtil.isEmpty(param.getDeviceIds())){
				return new ArrayList<>();
			}
			deviceParam.setDeviceIdList(param.getDeviceIds());
		}else{
			MaintainPlan plan = this.getById(param.getId());
			AssertUtils.notNull(plan, SystemResultCode.RESULT_DATA_NONE.message());
			List<MaintainDeviceModel> maintainDevices = maintainDeviceService.list(plan.getId());
			if(CollectionUtil.isEmpty(maintainDevices)){
				return new ArrayList<>();
			}
			deviceParam.setDeviceIdList(maintainDevices.stream().map(MaintainDeviceModel::getDeviceId).distinct().collect(Collectors.toList()));
		}
		return findDeviceList(deviceParam);
	}

	@Override
	public IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param) {
		param.setSource(WorkOrderSourceEnum.MAINTAINPLAN.getCode());
		return workOrderService.businessPage(param);
	}

	@Override
	public Boolean executePlan(Map<Long, String> scheduleMap) {
		List<MaintainPlan> plans = this.getBaseMapper().selectListBySql();
		if(CollectionUtil.isEmpty(plans)){
			return false;
		}
		Date currentDate = DateUtil.date();
        Map<Long, List<MaintainPlan>> planMap = plans.stream().collect(Collectors.groupingBy(MaintainPlan::getTenantId));
        for(Map.Entry<Long,List<MaintainPlan>> entry : planMap.entrySet()){
            Long number = workOrderService.queryWorkOrderCount(WorkOrderSourceEnum.MAINTAINPLAN.getCode(), entry.getKey());
            for (MaintainPlan plan : entry.getValue()) {
                log.info("维保计划信息{}", JSON.toJSONString(plan));
                // 判断当前日期是否满足计划的周期条件
                if (!PlanDateUtil.isDateValid(currentDate, plan.getPeriodType(), plan.getPeriodSign(), plan.getPeriodStartTime(), plan.getPlanPeriod())) {
                    log.info("维保计划不符合生成工单规则,计划id：{}", plan.getId());
                    continue; // 如果不满足条件，跳过该计划
                }
                try {
                    number++;
                    WorkPlanModel planModel = BeanUtils.convertTo(plan, WorkPlanModel::new);
                    planModel.setPlanType(WorkOrderTypeEnum.MAINTAINPLAN.getCode());
                    planModel.setPlanSource(WorkOrderSourceEnum.MAINTAINPLAN.getCode());
                    planModel.setPlanCode(PlanDateUtil.getCode(WorkOrderCodePrefixConstant.WB,  number));
                    planModel.setScheduleName(ObjectUtil.isNotEmpty(scheduleMap) ? scheduleMap.get(plan.getScheduleId()) : null);
                    //创建工单
                    WorkOrder workOrder = workOrderService.addPlanWordOrder(planModel, null);
                    //保存工单材料
                    addWorkMaterial(workOrder, planModel);
                    //保存工单任务
                    addWorkTask(workOrder, planModel);
                } catch (Exception e) {
                    log.error("维保计划生成工单失败", e);
                }
            }
        }
		return true;
	}


    private void addWorkTask(WorkOrder workOrder, WorkPlanModel planModel){
        //维保任务
        List<MaintainDeviceModel> maintainDevices =  maintainDeviceService.list(planModel.getId());
        if(CollectionUtil.isEmpty(maintainDevices)) {
            return;
        }
        List<MaintainItemModel> maintainItems = maintainItemService.list(planModel.getId());
        if(CollectionUtil.isEmpty(maintainItems)) {
            return;
        }
        MaintainPlanDeviceParam deviceParam = new MaintainPlanDeviceParam();
        deviceParam.setDeviceIds(maintainDevices.stream().map(MaintainDeviceModel::getDeviceId).distinct().collect(Collectors.toList()));
        List<IocDeviceModel> deviceModels = findDeviceList(deviceParam);
        Map<Long, IocDeviceModel> deviceMap = CollectionUtil.isEmpty(deviceModels) ? new HashMap<>() : deviceModels.stream().collect(Collectors.toMap(IocDeviceModel::getId, v -> v));
        List<WorkTask> params = maintainDevices.stream().map(maintainDevice -> {
            WorkTask param = new WorkTask();
            param.setWorkId(workOrder.getId());
            param.setBusinessType(WorkTaskTypeEnum.MAINTAIN_DEVICE.getCode());
            param.setTaskGroup(maintainDevice.getTaskGroup());
            param.setBusinessId(maintainDevice.getDeviceId());
            if(ObjectUtil.isNotEmpty(deviceMap.get(maintainDevice.getDeviceId()))) {
                IocDeviceModel device = deviceMap.get(maintainDevice.getDeviceId());
                param.setName(device.getDeviceName());
                param.setCode(device.getDeviceCode());
                param.setCategory(device.getDeviceCategory());
                param.setSpaceId(device.getSpaceId());
                param.setSpaceFullPath(device.getSpaceName());
                param.setRedundancyTimeOne(device.getSecureDate());
            }
            param.setTenantId(workOrder.getTenantId());
            param.setCreateTime(new Date());
            param.setCreateBy(workOrder.getCreateBy());
            param.setCreatorId(workOrder.getCreatorId());
            param.setUpdateBy(workOrder.getCreateBy());
            param.setUpdatorId(workOrder.getCreatorId());
            param.setUpdateTime(new Date());
            return param;
        }).collect(Collectors.toList());
        workTaskService.saveBatch(params);
        List<WorkTaskItem> itemParams = maintainItems.stream().map(maintainItem -> {
            WorkTaskItem param = new WorkTaskItem();
            param.setWorkId(workOrder.getId());
            param.setBusinessType(WorkTaskItemTypeEnum.MAINTAIN_ITEM.getCode());
            param.setTaskGroup(maintainItem.getTaskGroup());
            param.setName(maintainItem.getName());
            param.setRemark(maintainItem.getContent());
            param.setTenantId(workOrder.getTenantId());
            param.setCreateTime(new Date());
            param.setCreateBy(workOrder.getCreateBy());
            param.setCreatorId(workOrder.getCreatorId());
            param.setUpdateBy(workOrder.getCreateBy());
            param.setUpdatorId(workOrder.getCreatorId());
            param.setUpdateTime(new Date());
            return param;
        }).collect(Collectors.toList());
        workTaskItemService.saveBatch(itemParams);
    }

    private void addWorkMaterial(WorkOrder workOrder, WorkPlanModel planModel){
        //维保材料
        List<MaintainMaterialModel> maintainMaterials = maintainMaterialService.list(planModel.getId());
        if(CollectionUtil.isEmpty(maintainMaterials)){
            return;
        }
        List<Long> materialIds = maintainMaterials.stream().map(MaintainMaterialModel::getMaterialId).collect(Collectors.toList());
        List<Material> materials = materialService.list(Wrappers.<Material>lambdaQuery().in(Material::getId, materialIds).eq(Material::getDeleted,  Status.enabled.getKey()));
        Map<Long, Material> materialMap = CollectionUtil.isEmpty(materials)?new HashMap<>(): materials.stream().collect(Collectors.toMap(Material::getId, material -> material));
        List<WorkMaterial> params = maintainMaterials.stream().map(material -> {
            WorkMaterial param = new WorkMaterial();
            if(materialMap.get(material.getMaterialId()) != null){
                BeanUtils.copyProperties(materialMap.get(material.getMaterialId()), param);
            }
            param.setMaterialId(material.getMaterialId());
            param.setMaterialNum(material.getMaterialNum());
            param.setWorkId(workOrder.getId());
            param.setId(null);
            param.setTenantId(workOrder.getTenantId());
            param.setCreateTime(new Date());
            param.setCreateBy(workOrder.getCreateBy());
            param.setCreatorId(workOrder.getCreatorId());
            param.setUpdateBy(workOrder.getCreateBy());
            param.setUpdatorId(workOrder.getCreatorId());
            param.setUpdateTime(new Date());
            return param;
        }).collect(Collectors.toList());
        workMaterialService.saveBatch(params);
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

	private void handlePlanModel(List<MaintainPlanModel> models){
		if(CollectionUtil.isEmpty(models)){
			return;
		}
		Date currentDate = DateUtil.date();
		List<Long> ids = models.stream().map(MaintainPlanModel::getId).collect(Collectors.toList());
		List<Long> scheduleIds = models.stream().map(MaintainPlanModel::getScheduleId).collect(Collectors.toList());
		List<MaintainDevice> maintainDevices = maintainDeviceService.list(Wrappers.<MaintainDevice>lambdaQuery().in(MaintainDevice::getMaintainId, ids));
		Map<Long, String> scheduleMap = propertyScheduleService.getScheduleNameMap(scheduleIds);
		Map<Long, List<MaintainDevice>> maintainDeviceMap = CollectionUtil.isEmpty(maintainDevices) ? new HashMap<>() : maintainDevices.stream().collect(Collectors.groupingBy(MaintainDevice::getMaintainId, LinkedHashMap::new, Collectors.toList()));
		List<WorkOrder> workOrders = workOrderService.list(Wrappers.<WorkOrder>lambdaQuery().ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode())
				.eq(WorkOrder::getSource, WorkOrderSourceEnum.MAINTAINPLAN.getCode())
				.in(WorkOrder::getBusinessId, ids));
		Set<Long> businessIds = CollectionUtil.isEmpty(workOrders) ? new HashSet<>() : workOrders.stream().map(WorkOrder::getBusinessId).collect(Collectors.toSet());
		models.forEach(model -> {
			if(CollectionUtil.isNotEmpty(maintainDeviceMap.get(model.getId()))){
				model.setDeviceCount(maintainDeviceMap.get(model.getId()).stream().map(MaintainDevice::getDeviceId).distinct().count());
			}
			if(ObjectUtil.isNotEmpty(scheduleMap.get(model.getScheduleId()))){
				model.setScheduleName(scheduleMap.get(model.getScheduleId()));
			}
			if(Integer.valueOf(Status.enabled.getKey()).equals(model.getStatus())){
				model.setNextTime(PlanDateUtil.getNextDate(currentDate, model.getPeriodType(), model.getPeriodSign(), model.getPeriodStartTime(), model.getPlanPeriod()));
			}
			if (businessIds.contains(model.getId())){
				model.setWorkOrderFlag(true);
			}
		});
	}

	private void handlePlanDetail(MaintainPlanModel model) {
		//维保材料
		List<MaintainMaterialModel> maintainMaterials = maintainMaterialService.list(model.getId());
		model.setMaterialModels(maintainMaterials);
		//维保任务
		List<MaintainDeviceModel> deviceModels =  maintainDeviceService.list(model.getId());
		if(CollectionUtil.isEmpty(deviceModels)) {
			return;
		}
		List<MaintainItemModel> maintainItems = maintainItemService.list(model.getId());
		if(CollectionUtil.isEmpty(maintainItems)) {
			return;
		}
		List<Long> deviceIds = deviceModels.stream().map(MaintainDeviceModel::getDeviceId).collect(Collectors.toList());
		List<IocDevice> iocDevices = iocDeviceService.list(Wrappers.<IocDevice>lambdaQuery().select(IocDevice::getId, IocDevice::getDeviceName).in(IocDevice::getId, deviceIds).eq(IocDevice::getDeleted, (int) Status.enabled.getKey()));
		Map<Long, String> deviceMap = CollectionUtil.isEmpty(iocDevices) ? new HashMap<>() :  iocDevices.stream().collect(Collectors.toMap(IocDevice::getId, IocDevice::getDeviceName));
		Map<Integer, List<MaintainDeviceModel>> deviceGroupMap = deviceModels.stream().collect(Collectors.groupingBy(MaintainDeviceModel::getTaskGroup, LinkedHashMap::new, Collectors.toList()));
		Map<Integer, List<MaintainItemModel>> itemGroupMap = maintainItems.stream()
				.collect(Collectors.groupingBy(MaintainItemModel::getTaskGroup, LinkedHashMap::new, Collectors.toList()));
		List<MaintainPlanTaskGroupModel> taskGroupModels = new ArrayList<>();
		for (Map.Entry<Integer, List<MaintainDeviceModel>> entry : deviceGroupMap.entrySet()) {
			MaintainPlanTaskGroupModel taskGroupModel = new MaintainPlanTaskGroupModel();
			taskGroupModel.setTaskGroup(entry.getKey());
			List<MaintainDeviceModel> taskDevices = entry.getValue();
			taskDevices.forEach(deviceModel -> {
				deviceModel.setDeviceName(deviceMap.get(deviceModel.getDeviceId()));
			});
			taskGroupModel.setDeviceModels(taskDevices);
			taskGroupModel.setItemModels(itemGroupMap.get(entry.getKey()));
			taskGroupModel.setDeviceCount((long) taskDevices.size());
			taskGroupModels.add(taskGroupModel);
		}
		model.setTaskGroupModels(taskGroupModels);
	}

	private void handleMaterial(MaintainPlanParam param, Long id) {
		if (CollectionUtil.isNotEmpty(param.getMaterialParams())) {
			param.getMaterialParams().forEach(materialParam -> {
				materialParam.setId(null);
				materialParam.setMaintainId(id);

			});
			maintainMaterialService.addBatch(param.getMaterialParams());
		}
	}

	private void handleTaskGroup(MaintainPlanParam param, Long id) {
		if (CollectionUtil.isNotEmpty(param.getTaskGroupParams())) {
			List<MaintainDeviceParam> deviceParams = new ArrayList<>();
			List<MaintainItemParam> itemParams = new ArrayList<>();
			param.getTaskGroupParams().forEach(taskGroupParam -> {
				taskGroupParam.getDeviceParams().forEach(deviceParam -> {
					deviceParam.setId(null);
					deviceParam.setMaintainId(id);
					deviceParam.setTaskGroup(taskGroupParam.getTaskGroup());
				});
				deviceParams.addAll(taskGroupParam.getDeviceParams());
				taskGroupParam.getItemParams().forEach(itemParam -> {
					itemParam.setId(null);
					itemParam.setMaintainId(id);
					itemParam.setTaskGroup(taskGroupParam.getTaskGroup());
				});
				itemParams.addAll(taskGroupParam.getItemParams());
			});
			if (CollectionUtil.isNotEmpty(deviceParams)) {
				maintainDeviceService.addBatch(deviceParams);
			}
			if (CollectionUtil.isNotEmpty(itemParams)) {
				maintainItemService.addBatch(itemParams);
			}
		}
	}

	private void checkParam(MaintainPlanParam param) {
        AssertUtils.isNotEmpty(param.getTaskGroupParams(),"任务内容不能为空");

		param.getTaskGroupParams().forEach(taskGroupParam -> {
            AssertUtils.isNotEmpty(taskGroupParam.getDeviceParams(),"维保对象不能为空");
            AssertUtils.isNotEmpty(taskGroupParam.getItemParams(),"维保项目不能为空");
		});

	}
	
	private LambdaQueryWrapper<MaintainPlan> buildQuery(MaintainPlanPageParam param) {
		LambdaQueryWrapper<MaintainPlan> query = new LambdaQueryWrapper<>();
		// 根据计划名称筛选
		query.like(ObjectUtil.isNotEmpty(param.getPlanName()), MaintainPlan::getPlanName, param.getPlanName());
		// 根据启用状态筛选
		query.eq(ObjectUtil.isNotEmpty(param.getStatus()), MaintainPlan::getStatus, param.getStatus());
		// 根据计划周期筛选
		query.eq(ObjectUtil.isNotEmpty(param.getPlanPeriod()), MaintainPlan::getPlanPeriod, param.getPlanPeriod());
		// 根据物业分组筛选
		query.eq(ObjectUtil.isNotEmpty(param.getScheduleId()), MaintainPlan::getScheduleId, param.getScheduleId());
		// 计划调度类型
		query.eq(ObjectUtil.isNotEmpty(param.getDispatchType()), MaintainPlan::getDispatchType, param.getDispatchType());
        // 租户
        query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MaintainPlan::getTenantId, WebFrameworkUtils.getHeaderTenantId());
        // 查询未删除的记录
		query.eq(MaintainPlan::getDeleted,  Status.enabled.getKey());
		query.orderByDesc(MaintainPlan::getCreateTime);
		return query;
	}
}