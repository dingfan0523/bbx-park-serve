
package com.cgnpc.bbxpark.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.workorder.domain.WorkTask;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanDetailModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkTaskModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPlanHandleParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkPlanDetailParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkTaskParam;

import java.util.List;

/***
 * @Description 工单任务服务接口
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
public interface IWorkTaskService extends IService<WorkTask> {

	/**
	 * 获取工单任务列表.
	 * @Param workId 工单标识
	 * @Return 工单任务信息列表
	 */
	List<WorkTaskModel> list(Long workId);

	/**
	 * 批量新增工单任务.
	 * @Param params 工单任务信息列表
	 * @Return 批量新增工单任务是否成功
	 */
	Boolean addBatch(List<WorkTaskParam> params);

	/**
	 * 根据工单计划详细信息标识获得工单计划详细信息详情信息.
	 * @Param [id] 工单计划详细信息标识
	 * @Return 工单计划详细信息详情信息
	 */
	WorkPlanDetailModel detail(WorkPlanDetailParam param);

	/***
	 * @Description 查询计划
	 * @author huangyongtao
	 * @date 2025/11/12 17:14
	 * @param param
	 */
	WorkOrderModel findWorkPlan(WorkOrderPlanHandleParam param);

	/***
	 * @Description 保存工单计划
	 * @author huangyongtao
	 * @date 2025/11/12 9:53
	 * @param params
	 */
	Boolean saveWorkPlan(WorkOrderPlanHandleParam params);

	/***
	 * @Description 确认工单计划
	 * @author huangyongtao
	 * @date 2025/11/12 9:53
	 * @param params
	 */
	Boolean submitWorkPlan(WorkOrderPlanHandleParam params);
}
