package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.TaskPlan;
import com.cgnpc.bbxpark.property.dto.model.TaskItemModel;
import com.cgnpc.bbxpark.property.dto.model.TaskPlanListModel;
import com.cgnpc.bbxpark.property.dto.model.TaskPlanModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.property.dto.param.TaskPlanListParam;
import com.cgnpc.bbxpark.property.dto.param.TaskPlanPageParam;
import com.cgnpc.bbxpark.property.dto.param.TaskPlanParam;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;

import java.util.List;
import java.util.Map;

/**
 * 任务计划管理服务接口
 */
public interface ITaskPlanService extends IService<TaskPlan> {

	/**
	 * 根据任务计划管理标识获得任务计划管理详情信息.
	 * @Param [id] 任务计划管理标识
	 * @Return 任务计划管理详情信息
	 */
	TaskPlanModel detail(Long id);

	/**
	 * 获取任务计划管理列表(分页).
	 * @Param param 任务计划管理查询条件
	 * @Return 任务计划管理信息列表（分页）
	 */
	IPage<TaskPlanListModel> page(TaskPlanPageParam param);

	/**
	 * 获取任务计划管理列表.
	 * @Param param 任务计划管理查询条件
	 * @Return 任务计划管理信息列表
	 */
	List<TaskPlanListModel> list(TaskPlanListParam param);

	/**
	 * 新增任务计划管理.
	 * @Param param 任务计划管理信息
	 * @Return 新增任务计划管理是否成功
	 */
	Boolean add(TaskPlanParam param);

	/**
	 * 编辑任务计划管理信息.
	 * @Return 编辑任务计划管理是否成功
	 */
	Boolean edit(TaskPlanParam param);

	/**
	 * 删除任务计划管理.
	 * @Param id 任务计划管理标识
	 * @Return 删除任务计划管理是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 修改任务计划状态
	 * @param param 修改任务计划状态参数
	 * @return 修改成功
	 */
	Boolean statusEdit(InspectionPointStatusParam param);

	/**
	 * 查询任务计划下任务列表
	 * @param taskId 任务计划ID
	 * @return 任务列表
	 */
	List<TaskItemModel> findItemList(Long taskId);

	/**
	 * 获取工单主列表(分页).
	 * @Param param 工单主查询条件
	 * @Return 工单主信息列表（分页）
	 */
	IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param);

	/***
	 * @Description 执行任务计划
	 * @author huangyongtao
	 * @date 2025/3/26 14:31
	 */
	Boolean executePlan(Map<Long, String> scheduleMap);
}
