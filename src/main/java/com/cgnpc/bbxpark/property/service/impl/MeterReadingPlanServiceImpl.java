
package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.constant.WorkOrderCodePrefixConstant;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.enums.WorkOrderSourceEnum;
import com.cgnpc.bbxpark.common.enums.WorkOrderStatusEnum;
import com.cgnpc.bbxpark.common.enums.WorkOrderTypeEnum;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.IocDeviceListParam;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.property.domain.MaintainPlan;
import com.cgnpc.bbxpark.property.domain.MeterReadingPlan;
import com.cgnpc.bbxpark.property.dto.model.MeterReadingPlanModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.mapper.MeterReadingPlanRepository;
import com.cgnpc.bbxpark.property.service.IMeterReadingPlanService;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleService;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceTreeModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderDevice;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderDeviceService;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/***
 * @Description 抄表计划管理服务实现
 * @author huangyongtao
 * @date 2025/3/25 17:19
 */
@Slf4j
@Service("meterReadingPlanService")
public class MeterReadingPlanServiceImpl extends BaseServiceImpl<MeterReadingPlanRepository, MeterReadingPlan> implements IMeterReadingPlanService {

	@Autowired
	private IIocDeviceService iocDeviceService;

	@Autowired
	private IParkSpaceService parkSpaceService;

    @Resource
    private IUserApiService userApiService;

//	@Autowired
//	private IUserApiService userApiService;
//
	@Autowired
	private IWorkOrderService workOrderService;

	@Autowired
	private IWorkOrderDeviceService workOrderDeviceService;

	@Autowired
	private IPropertyScheduleService propertyScheduleService;

	@Autowired
	@Qualifier("asyncEventBusExecutor")
	private Executor busExecutorService;
	/**
	 * 根据抄表计划管理标识获得抄表计划管理详情信息.
	 * @Param [id] 抄表计划管理标识
	 * @Return 抄表计划管理详情信息
	 */
	@Override
	public MeterReadingPlanModel detail(Long id) {
		MeterReadingPlan meterReadingPlan = this.getById(id);
		AssertUtils.notNull(meterReadingPlan, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(meterReadingPlan, MeterReadingPlanModel::new);
	}

	/**
	 * 获取抄表计划管理列表(分页).
	 * @Param param 抄表计划管理查询条件
	 * @Return 抄表计划管理信息列表（分页）
	 */
	@Override
	public IPage<MeterReadingPlanModel> page(MeterReadingPlanPageParam param) {
		IPage<MeterReadingPlan> page = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(BeanUtils.convertTo(param, MeterReadingPlanParam::new)));
		if (CollectionUtil.isEmpty(page.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<MeterReadingPlanModel> models = BeanUtils.convertListTo(page.getRecords(), MeterReadingPlanModel::new);
		//处理空间名称和设备数量
		handlePlanModel(models);
		//处理是否包含未完成的工单
//		handleWorkOrder(models);
		return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), models);
	}


	/**
	 * 获取抄表计划管理列表.
	 * @Param param 抄表计划管理查询条件
	 * @Return 抄表计划管理信息列表
	 */
	@Override
	@SneakyThrows
	public List<MeterReadingPlanModel> list(MeterReadingPlanListParam param) {
		List<MeterReadingPlan> models = this.list(buildQuery(BeanUtils.convertTo(param, MeterReadingPlanParam::new)));
		if (CollectionUtil.isEmpty(models)) {
			return Collections.emptyList();
		}
		return BeanUtils.convertListTo(models, MeterReadingPlanModel::new);
	}


	/**
	 * 新增抄表计划管理.
	 * @Param param 抄表计划管理信息
	 * @Return 新增抄表计划管理是否成功
	 */
	@Override
	public Boolean add(MeterReadingPlanParam param) {
		MeterReadingPlan meterReadingPlan = BeanUtils.convertTo(param, MeterReadingPlan::new);
		meterReadingPlan.setId(null);
//		UserInfoModel user = getUser(WebFrameworkUtils.getHeaderUserId());
		meterReadingPlan.setCreateBy(userApiService.getCurrentStaffName());
		return this.save(meterReadingPlan);
	}

	/**
	 * 删除抄表计划管理.
	 * @Param id 抄表计划管理标识
	 * @Return 删除抄表计划管理是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		MeterReadingPlan meterReadingPlan = this.getById(id);
		AssertUtils.notNull(meterReadingPlan, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.isFalse(Integer.valueOf(Status.enabled.getKey()).equals(meterReadingPlan.getStatus()), "请将抄表计划先禁用再删除！");
		return this.removeById(id);
	}

	/**
	 * 编辑抄表计划管理信息.
	 * @Param param 抄表计划管理信息
	 * @Return 编辑抄表计划管理是否成功
	 */
	@Override
	public Boolean edit(MeterReadingPlanParam param) {
		MeterReadingPlan meterReadingPlan = this.getById(param.getId());
		AssertUtils.notNull(meterReadingPlan, SystemResultCode.RESULT_DATA_NONE.message());
		MeterReadingPlan editParam = BeanUtils.convertTo(param, MeterReadingPlan::new);
		return this.updateById(editParam);
	}

	/**
	 * 编辑启用状态抄表计划管理.
	 * @Param id 抄表计划管理标识
	 * @Return 启用抄表计划管理是否成功
	 */
	@Override
	public Boolean statusEdit(MeterReadingPlanStatusParam param) {
		AssertUtils.notNull(param.getStatus(), "状态不能为空");
		MeterReadingPlan plan = this.getById(param.getId());
		AssertUtils.notNull(plan, SystemResultCode.RESULT_DATA_NONE.message());
		MeterReadingPlan meterReadingPlan = new MeterReadingPlan();
	 	meterReadingPlan.setId(param.getId());
		meterReadingPlan.setStatus(param.getStatus());
		return this.updateById(meterReadingPlan);
	}

	@Override
	public List<IocDeviceModel> findDeviceList(MeterReadingPlanDeviceParam param) {
		MeterReadingPlan plan;
		if(ObjectUtil.isEmpty(param.getId())){
			AssertUtils.isFalse(ObjectUtil.isEmpty(param.getSpaceId()) || ObjectUtil.isEmpty(param.getReadingType()), "空间位置或者抄表类型不能为空");
			plan = new MeterReadingPlan();
			plan.setSpaceId(param.getSpaceId());
			plan.setReadingType(param.getReadingType());
            plan.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		}else{
			plan = this.getById(param.getId());
			AssertUtils.notNull(plan, SystemResultCode.RESULT_DATA_NONE.message());

		}
		IocDeviceListParam deviceParam = new IocDeviceListParam();
		deviceParam.setSpaceIds(parkSpaceService.findChildrenIdList(Arrays.stream(plan.getSpaceId().split(",")).map(Long::valueOf).collect(Collectors.toList()),plan.getTenantId()));
		deviceParam.setReadingTypes(Arrays.stream(plan.getReadingType().split(",")).collect(Collectors.toList()));
		return findDeviceList(deviceParam);
	}

	@Override
	public IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param) {
		param.setSource(WorkOrderSourceEnum.METERPLAN.getCode());
		return workOrderService.businessPage(param);
	}


	@Override
	public Boolean executePlan(Map<Long, String> scheduleMap) {
		MeterReadingPlanParam param = new MeterReadingPlanParam();
		param.setStatus(Status.enabled.getKey());
		List<MeterReadingPlan> plans = this.list(buildQuery(param));
		if(CollectionUtil.isEmpty(plans)){
			return false;
		}
		Date currentDate = DateUtil.date();
        Map<Long, List<MeterReadingPlan>> planMap = plans.stream().collect(Collectors.groupingBy(MeterReadingPlan::getTenantId));
        for(Map.Entry<Long,List<MeterReadingPlan>> entry : planMap.entrySet()){
            //获取工单数量
            Long number = workOrderService.queryWorkOrderCount(WorkOrderSourceEnum.METERPLAN.getCode(), entry.getKey());
            for (MeterReadingPlan plan : entry.getValue()) {
//			busExecutorService.execute(() -> {
                log.info("抄表计划信息{}", JSON.toJSONString(plan));
                // 判断当前日期是否满足计划的周期条件
                if (!PlanDateUtil.isDateValid(currentDate, plan.getPeriodType(), plan.getPeriodSign(), plan.getPeriodStartTime(), plan.getPlanPeriod())) {
                    log.info("抄表计划不符合生成工单规则,计划id：{}", plan.getId());
                    continue; // 如果不满足条件，跳过该计划
                }
                IocDeviceListParam deviceParam = new IocDeviceListParam();
                List<Long> spaceIds = Arrays.stream(plan.getSpaceId().split(",")).map(Long::valueOf).collect(Collectors.toList());
                deviceParam.setSpaceIds(parkSpaceService.findChildrenIdList(spaceIds,plan.getTenantId()));
                deviceParam.setReadingTypes(Arrays.stream(plan.getReadingType().split(",")).collect(Collectors.toList()));
                deviceParam.setReadingDevice(Status.enabled.getKey());
                List<IocDeviceModel> deviceModels = iocDeviceService.findListByJob(deviceParam);
                if(CollectionUtil.isEmpty(deviceModels)){
                    continue;
                }
                Map<Long, ParkSpaceFullModel> fullSpaceMap = CollectionUtil.isEmpty(spaceIds) ? new HashMap<>() : parkSpaceService.findFullSpaceMap(spaceIds, null);
                try {
                    number++;
                    WorkPlanModel planModel = BeanUtils.convertTo(plan, WorkPlanModel::new);
                    planModel.setPlanType(WorkOrderTypeEnum.METERPLAN.getCode());
                    planModel.setPlanSource(WorkOrderSourceEnum.METERPLAN.getCode());
                    planModel.setPlanCode(PlanDateUtil.getCode(WorkOrderCodePrefixConstant.CB,  number));
                    planModel.setScheduleName(ObjectUtil.isNotEmpty(scheduleMap) ? scheduleMap.get(plan.getScheduleId()) : null);
                    //创建计划工单
                    WorkOrder workOrder = workOrderService.addPlanWordOrder(planModel, handleSpaceName(spaceIds, fullSpaceMap));
                    //工单设备关系
                    handleWorkOrderDevice(deviceModels, workOrder);
                } catch (Exception e) {
                    log.error("抄表计划生成工单失败", e);
                }
//			});
            }
        }
		return true;
	}


	private LambdaQueryWrapper<MeterReadingPlan> buildQuery(MeterReadingPlanParam param) {
		LambdaQueryWrapper<MeterReadingPlan> query = new LambdaQueryWrapper<>();
		query.like(ObjectUtil.isNotEmpty(param.getPlanName()), MeterReadingPlan::getPlanName, param.getPlanName());
		query.eq(ObjectUtil.isNotEmpty(param.getStatus()), MeterReadingPlan::getStatus, param.getStatus());
		query.eq(ObjectUtil.isNotEmpty(param.getPlanPeriod()), MeterReadingPlan::getPlanPeriod, param.getPlanPeriod());
		query.like(ObjectUtil.isNotEmpty(param.getReadingType()), MeterReadingPlan::getReadingType, param.getReadingType());
		query.eq(ObjectUtil.isNotEmpty(param.getScheduleId()), MeterReadingPlan::getScheduleId, param.getScheduleId());
		query.eq(ObjectUtil.isNotEmpty(param.getDispatchType()), MeterReadingPlan::getDispatchType, param.getDispatchType());
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MeterReadingPlan::getTenantId, WebFrameworkUtils.getHeaderTenantId());
        // 查询未删除的记录
        query.eq(MeterReadingPlan::getDeleted,  Status.enabled.getKey());
		query.apply(ObjectUtil.isNotEmpty(param.getSpaceId()),"FIND_IN_SET({0}, space_id) > 0", param.getSpaceId());
		query.orderByDesc(MeterReadingPlan::getCreateTime);
		return query;
	}

	private void handlePlanModel(List<MeterReadingPlanModel> models){
		if(CollectionUtil.isEmpty(models)){
			return;
		}
		Date currentDate = DateUtil.date();
		List<Long> spaceIds = models.stream()
				.filter(model -> ObjectUtil.isNotEmpty(model.getSpaceId())) // 过滤 spaceId 不为空的 model
				.flatMap(model -> Arrays.stream(model.getSpaceId().split(","))) // 将 spaceId 按逗号分割为字符串数组，并扁平化
				.map(Long::valueOf) // 将字符串转换为 Long
				.collect(Collectors.toList()); // 收集为 List<Long>
		Map<Long, ParkSpaceFullModel> fullSpaceMap = CollectionUtil.isEmpty(spaceIds) ? new HashMap<>() : parkSpaceService.findFullSpaceMap(spaceIds, null);
		List<IocDeviceModel> deviceModels = findDeviceList(new IocDeviceListParam());
		List<ParkSpaceTreeModel> treeModels = parkSpaceService.buildTree(WebFrameworkUtils.getHeaderTenantId());
		List<Long> scheduleIds = models.stream().map(MeterReadingPlanModel::getScheduleId).collect(Collectors.toList());
		Map<Long, String> scheduleMap =propertyScheduleService.getScheduleNameMap(scheduleIds);
		models.forEach(model -> {
			//空间名称
			if(ObjectUtil.isNotEmpty(model.getSpaceId())){
				List<Long> modelSpaceIds = Arrays.stream(model.getSpaceId().split(",")).map(Long::valueOf).collect(Collectors.toList());
				model.setSpaceName(handleSpaceName(modelSpaceIds, fullSpaceMap));
				List<String> readingTypes = Arrays.stream(model.getReadingType().split(",")).collect(Collectors.toList());
				List<Long> spaceAllIds = parkSpaceService.findChildrenIdList(Arrays.stream(model.getSpaceId().split(",")).map(Long::valueOf).collect(Collectors.toList()), treeModels);
				if(CollectionUtil.isNotEmpty(deviceModels)){
					long deviceCount = deviceModels.stream().filter(device -> spaceAllIds.contains(device.getSpaceId()) && readingTypes.contains(device.getReadingType())).count();
					model.setDeviceCount(deviceCount);
				}
			}
			if(ObjectUtil.isNotEmpty(scheduleMap.get(model.getScheduleId()))){
				model.setScheduleName(scheduleMap.get(model.getScheduleId()));
			}
			if(Integer.valueOf(Status.enabled.getKey()).equals(model.getStatus())){
				model.setNextTime(PlanDateUtil.getNextDate(currentDate, model.getPeriodType(), model.getPeriodSign(), model.getPeriodStartTime(), model.getPlanPeriod()));
			}
		});
	}

	private List<IocDeviceModel> findDeviceList(IocDeviceListParam param){
		param.setAuth(false);
		param.setReadingDevice((int) Status.enabled.getKey());
		return iocDeviceService.findList(param);
	}

	/***
	 * @Description 获取用户信息
	 * @author huangyongtao
	 * @date 2024/8/2 10:34
	 * @param
	 */
//	private UserInfoModel getUser(String userId) {
//		return Objects.requireNonNull(userInfoFeignClient.detail(userId));
//	}

	private String handleSpaceName(List<Long> spaceIds, Map<Long, ParkSpaceFullModel> fullSpaceMap){
		return spaceIds.stream()
				.map(spaceId -> Optional.ofNullable(fullSpaceMap.get(spaceId))
						.map(ParkSpaceFullModel::getFullPath).orElse(""))
				.filter(name -> ObjectUtil.isNotEmpty(name))
				.collect(Collectors.joining("、"));
	}

	private void handleWorkOrderDevice(List<IocDeviceModel> deviceModels, WorkOrder workOrder) {
		List<WorkOrderDevice> workOrderDevices = deviceModels.stream().map(device -> {
			WorkOrderDevice workOrderDevice = new WorkOrderDevice();
			workOrderDevice.setWorkOrderId(workOrder.getId());
			workOrderDevice.setDeviceId(device.getId());
			workOrderDevice.setDeviceName(device.getDeviceName());
			workOrderDevice.setSpaceId(device.getSpaceId());
			workOrderDevice.setSpaceFullPath(device.getSpaceName());
			workOrderDevice.setReadingType(device.getReadingType());
			workOrderDevice.setReadingCode(device.getReadingCode());
			workOrderDevice.setReadingRate(device.getReadingRate());
            workOrderDevice.setTenantId(workOrder.getTenantId());
            workOrderDevice.setCreateTime(new Date());
            workOrderDevice.setCreateBy(workOrder.getCreateBy());
            workOrderDevice.setCreatorId(workOrder.getCreatorId());
            workOrderDevice.setUpdateBy(workOrder.getCreateBy());
            workOrderDevice.setUpdatorId(workOrder.getCreatorId());
            workOrderDevice.setUpdateTime(new Date());
			return workOrderDevice;
		}).collect(Collectors.toList());
		if(CollectionUtil.isNotEmpty(workOrderDevices)){
			workOrderDeviceService.saveBatch(workOrderDevices);
		}
	}
	private void handleWorkOrder(List<MeterReadingPlanModel> models){
		List<Long> planIds = models.stream().map(MeterReadingPlanModel::getId).collect(Collectors.toList());
		List<WorkOrder> workOrders = workOrderService.list(Wrappers.<WorkOrder>lambdaQuery().ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode())
				.eq(WorkOrder::getSource, WorkOrderSourceEnum.METERPLAN.getCode())
				.in(WorkOrder::getBusinessId, planIds));
		if (CollectionUtil.isEmpty(workOrders)){
			return;
		}
		Set<Long> businessIds = workOrders.stream().map(WorkOrder::getBusinessId).collect(Collectors.toSet());
		models.forEach(model -> {
			if (businessIds.contains(model.getId())){
				model.setWorkOrderFlag(true);
			}
		});
	}
}
