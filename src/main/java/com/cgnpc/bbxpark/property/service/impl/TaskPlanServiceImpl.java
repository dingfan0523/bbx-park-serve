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
import com.cgnpc.bbxpark.property.domain.PatrolPlan;
import com.cgnpc.bbxpark.property.domain.TaskItem;
import com.cgnpc.bbxpark.property.domain.TaskPlan;
import com.cgnpc.bbxpark.property.dto.model.TaskItemModel;
import com.cgnpc.bbxpark.property.dto.model.TaskPlanListModel;
import com.cgnpc.bbxpark.property.dto.model.TaskPlanModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.mapper.TaskPlanRepository;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleService;
import com.cgnpc.bbxpark.property.service.ITaskItemService;
import com.cgnpc.bbxpark.property.service.ITaskPlanService;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务计划管理服务实现
 */
@Slf4j
@Service
public class TaskPlanServiceImpl extends ServiceImpl<TaskPlanRepository, TaskPlan> implements ITaskPlanService {
	@Resource
	private ITaskItemService taskItemService;
	@Resource
	private IPropertyScheduleService propertyScheduleService;
	@Resource
	private IWorkOrderService workOrderService;
	@Resource
	private IParkSpaceService parkSpaceService;
	@Resource
	private IUserApiService userApiService;
    @Resource
    private IWorkTaskService workTaskService;


	/**
	 * 根据任务计划管理标识获得任务计划管理详情信息.
	 * @Param [id] 任务计划管理标识
	 * @Return 任务计划管理详情信息
	 */
	@Override
	public TaskPlanModel detail(Long id) {
		TaskPlan taskPlan = this.getById(id);
		AssertUtils.notNull(taskPlan, SystemResultCode.RESULT_DATA_NONE.message());
		TaskPlanModel model = BeanUtils.convertTo(taskPlan, TaskPlanModel::new);
		model.setItems(findItemList(id));
		return model;
	}

	/**
	 * 获取任务计划管理列表(分页).
	 * @Param param 任务计划管理查询条件
	 * @Return 任务计划管理信息列表（分页）
	 */
	@Override
	public IPage<TaskPlanListModel> page(TaskPlanPageParam param) {
		IPage<TaskPlan> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(BeanUtils.convertTo(param, TaskPlanListParam::new)));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(result,convert(result.getRecords()));
	}

	/**
	 * 获取任务计划管理列表.
	 * @Param param 任务计划管理查询条件
	 * @Return 任务计划管理信息列表
	 */
	@Override
	@SneakyThrows
	public List<TaskPlanListModel> list(TaskPlanListParam param) {
		List<TaskPlan> plans = this.list(buildQuery(param));
		return BeanUtils.convertListTo(plans, TaskPlanListModel::new);
	}

	/**
	 * 新增任务计划管理.
	 * @Param param 任务计划管理信息
	 * @Return 新增任务计划管理是否成功
	 */
	@Override
	public Boolean add(TaskPlanParam param) {
		TaskPlan taskPlan = BeanUtils.convertTo(param, TaskPlan::new);
		//创建人
		UserInfoModel userInfo = userApiService.getCurrentUserInfo();
        taskPlan.setCreateBy(userInfo.getUserName());
		processUserInfo(taskPlan);
		taskPlan.setId(null);
		if(save(taskPlan)){
			return processItem(taskPlan.getId(),param.getTaskItemParams());
		}
		return false;
	}

	/**
	 * 编辑任务计划管理信息.
	 * @Param param 任务计划管理信息
	 * @Return 编辑任务计划管理是否成功
	 */
	@Override
	public Boolean edit(TaskPlanParam param) {
		TaskPlan taskPlan = this.getById(param.getId());
		AssertUtils.notNull(taskPlan, SystemResultCode.RESULT_DATA_NONE.message());
		BeanUtils.copyProperties(param,taskPlan);
		processUserInfo(taskPlan);
		if(updateById(taskPlan)){
			//先删除关联的信息
			taskItemService.remove(taskPlan.getId());
			return processItem(taskPlan.getId(),param.getTaskItemParams());
		}
		return false;
	}

	/**
	 * 删除任务计划管理.
	 * @Param id 任务计划管理标识
	 * @Return 删除任务计划管理是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		TaskPlan taskPlan = this.getById(id);
		AssertUtils.notNull(taskPlan, SystemResultCode.RESULT_DATA_NONE.message());
        taskPlan.setDeleted(Delete.DELETED.getKey());
		return updateById(taskPlan);
	}


	@Override
	public Boolean statusEdit(InspectionPointStatusParam param) {
		TaskPlan plan = this.getById(param.getId());
		AssertUtils.notNull(plan, SystemResultCode.RESULT_DATA_NONE);
		plan.setStatus(param.getStatus());
		return updateById(plan);
	}

	@Override
	public List<TaskItemModel> findItemList(Long taskId) {
		List<TaskItem> items = taskItemService.list(Wrappers.<TaskItem>lambdaQuery().eq(TaskItem::getTaskId, taskId));
		//空间名称
		if(CollectionUtil.isNotEmpty(items)){
			Set<Long> spaceIds = items.stream().map(TaskItem::getSpaceId).collect(Collectors.toSet());
			Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(new ArrayList<>(spaceIds), WebFrameworkUtils.getHeaderTenantId());
			return items.stream().map(item -> {
				TaskItemModel itemModel = BeanUtils.convertTo(item, TaskItemModel::new);
				itemModel.setSpaceName(spaceMap.getOrDefault(item.getSpaceId(), new ParkSpaceFullModel()).getFullPath());
				return itemModel;
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param) {
		param.setSource(WorkOrderSourceEnum.TASKPLAN.getCode());
		return workOrderService.businessPage(param);
	}

	@Override
	public Boolean executePlan(Map<Long, String> scheduleMap) {
		List<TaskPlan> plans = this.getBaseMapper().selectListBySql();
		if(CollectionUtil.isEmpty(plans)){
			return false;
		}
		Date currentDate = DateUtil.date();

        Map<Long, List<TaskPlan>> planMap = plans.stream().collect(Collectors.groupingBy(TaskPlan::getTenantId));
        for(Map.Entry<Long,List<TaskPlan>> entry : planMap.entrySet()){
            //获取工单数量
            Long number = workOrderService.queryWorkOrderCount(WorkOrderSourceEnum.TASKPLAN.getCode(), entry.getKey());
            for (TaskPlan plan : entry.getValue()) {
                log.info("任务计划信息{}", JSON.toJSONString(plan));
                // 判断当前日期是否满足计划的周期条件
                if (!PlanDateUtil.isDateValid(currentDate, plan.getPeriodType(), plan.getPeriodSign(), plan.getPeriodStartTime(), plan.getPlanPeriod())) {
                    log.info("任务计划不符合生成工单规则,计划id：{}", plan.getId());
                    continue; // 如果不满足条件，跳过该计划
                }
                try {
                    number++;
                    WorkPlanModel planModel = BeanUtils.convertTo(plan, WorkPlanModel::new);
                    planModel.setPlanType(WorkOrderTypeEnum.TASKPLAN.getCode());
                    planModel.setPlanSource(WorkOrderSourceEnum.TASKPLAN.getCode());
                    planModel.setPlanCode(PlanDateUtil.getCode(WorkOrderCodePrefixConstant.RW,  number));
                    planModel.setScheduleName(ObjectUtil.isNotEmpty(scheduleMap) ? scheduleMap.get(plan.getScheduleId()) : null);
                    //创建计划工单
                    WorkOrder workOrder = workOrderService.addPlanWordOrder(planModel, null);
                    //保存工单任务
                    addWorkTask(workOrder, planModel);
                } catch (Exception e) {
                    log.error("任务计划生成工单失败", e);
                }
            }
        }
		return true;
	}

    private void addWorkTask(WorkOrder workOrder, WorkPlanModel planModel){
        List<TaskItemModel> taskItemModel = findItemList(planModel.getId());
        if (CollectionUtil.isEmpty(taskItemModel)) {
            return;
        }
        List<WorkTask> params = taskItemModel.stream().map(taskItem -> {
            WorkTask param = new WorkTask();
            param.setWorkId(workOrder.getId());
            param.setBusinessType(WorkTaskTypeEnum.TASK.getCode());
            param.setBusinessId(taskItem.getId());
            param.setName(taskItem.getName());
            param.setSpaceId(taskItem.getSpaceId());
            param.setSpaceFullPath(taskItem.getSpaceName());
            param.setRemark(taskItem.getContent());
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
    }

	/**
	 * 处理用户信息.
	 * @param plan 任务计划管理信息
	 */
	private void processUserInfo(TaskPlan plan){
		if(ObjectUtil.isNotEmpty(plan.getAuditUid())){
			UserInfoModel auditUser = userApiService.getByStaffNo(plan.getAuditUid());
			plan.setAuditUname(auditUser.getUserName());
			plan.setAuditStaffid(auditUser.getStaffid());
		}
	}

	/**
	 * 批量处理任务计划明细信息.
	 * @Param id 任务计划管理标识
	 * @Param items 任务计划划明细信息列表
	 * @Return 批量处理任务计划划明细信息是否成功
	 */
	private boolean processItem(Long id, List<TaskItemParam> items){
		if(!CollectionUtils.isEmpty(items)){
			items.forEach(item -> item.setTaskId(id));
			return taskItemService.addBatch(items);
		}
		return true;
	}

	private LambdaQueryWrapper<TaskPlan> buildQuery(TaskPlanListParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		return Wrappers.<TaskPlan>lambdaQuery().like(ObjectUtil.isNotEmpty(param.getPlanName()), TaskPlan::getPlanName, param.getPlanName())
				.eq(param.getPlanPeriod() != null, TaskPlan::getPlanPeriod, param.getPlanPeriod())
				.eq(param.getScheduleId() != null, TaskPlan::getScheduleId, param.getScheduleId())
				.eq(param.getDispatchType() != null, TaskPlan::getDispatchType, param.getDispatchType())
				.eq(param.getStatus() != null, TaskPlan::getStatus, param.getStatus())
				.eq(tenantId != null,TaskPlan::getTenantId, tenantId)
				.eq(TaskPlan::getDeleted, Delete.NORMAL.getKey())
				.orderByDesc(TaskPlan::getCreateTime);
	}

	/**
	 * 批量转换返回模型
	 * @param plans 任务计划管理模型列表
	 * @return 任务计划管理模型列表
	 */
	private List<TaskPlanListModel> convert(List<TaskPlan> plans){
		if(CollectionUtil.isEmpty(plans)){
			return Collections.emptyList();
		}
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		Date currentDate = DateUtil.date();
		//任务明细列表
		List<Long> ids = plans.stream().map(TaskPlan::getId).collect(Collectors.toList());
		List<TaskItem> items = taskItemService.list(Wrappers.<TaskItem>lambdaQuery().in(TaskItem::getTaskId, ids).eq(tenantId != null,TaskItem::getTenantId, tenantId));
		Map<Long, Integer> itemCountMap = items.stream().collect(Collectors.groupingBy(TaskItem::getTaskId, Collectors.collectingAndThen(Collectors.toList(), List::size)));

		//物业分组信息
		List<Long> scheduleIds = plans.stream().map(TaskPlan::getScheduleId).filter(Objects::nonNull).collect(Collectors.toList());

		Map<Long, String> scheduleMap = Collections.emptyMap();
		if (!scheduleIds.isEmpty()) {
			scheduleMap = propertyScheduleService.getScheduleNameMap(scheduleIds);
		}

		List<Long> planIds = plans.stream().map(TaskPlan::getId).collect(Collectors.toList());
		List<WorkOrder> workOrders = workOrderService.list(Wrappers.<WorkOrder>lambdaQuery().ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode())
				.eq(WorkOrder::getSource, WorkOrderSourceEnum.PATROLPLAN.getCode())
				.in(WorkOrder::getBusinessId, planIds));
		Set<Long> businessIds = CollectionUtil.isEmpty(workOrders) ? Collections.emptySet() : workOrders.stream().map(WorkOrder::getBusinessId).collect(Collectors.toSet());

		//数据组装
		Map<Long, String> finalScheduleMap = scheduleMap;
		return plans.stream().map(plan -> {
			TaskPlanListModel model = BeanUtils.convertTo(plan, TaskPlanListModel::new);
			model.setTaskCount(itemCountMap.getOrDefault(plan.getId(), 0));
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
