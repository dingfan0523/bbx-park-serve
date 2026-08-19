
package com.cgnpc.bbxpark.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.workorder.domain.WorkPlanDetail;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanDetailModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPlanHandleParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkPlanDetailParam;

/***
 * @Description 工单计划详细信息服务接口
 * @author huangyongtao
 * @date 2025/3/25 16:40
 */
public interface IWorkPlanDetailService extends IService<WorkPlanDetail> {

	/**
	 * 根据工单计划详细信息标识获得工单计划详细信息详情信息.
	 * @Param [id] 工单计划详细信息标识
	 * @Return 工单计划详细信息详情信息
	 */
	WorkPlanDetailModel detail(WorkPlanDetailParam param);

	/***
	 * @Description 查询抄表设备列表
	 * @author huangyongtao
	 * @date 2025/3/27 17:14
	 * @param param
	 */
	WorkOrderModel findWorkOrderDeviceList(WorkOrderPlanHandleParam param);

	/***
	 * @Description 保存工单抄表计划
	 * @author huangyongtao
	 * @date 2025/3/28 9:53
	 * @param params
	 */
	Boolean saveWorkPlan(WorkOrderPlanHandleParam params);

	/***
	 * @Description 确认工单抄表计划
	 * @author huangyongtao
	 * @date 2025/3/28 9:53
	 * @param params
	 */
	Boolean submitWorkPlan(WorkOrderPlanHandleParam params);

}
