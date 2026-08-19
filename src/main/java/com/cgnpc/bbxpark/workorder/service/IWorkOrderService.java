
package com.cgnpc.bbxpark.workorder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanModel;
import com.cgnpc.bbxpark.workorder.dto.param.*;

import javax.servlet.http.HttpServletResponse;


/**
 * 工单主服务接口
 */
public interface IWorkOrderService extends IService<WorkOrder> {

	/**
	 * 根据工单主标识获得工单主详情信息.
	 * @Param [id] 工单主标识
	 * @Return 工单主详情信息
	 */
	WorkOrderModel detail(Long id);

	/**
	 * 获取工单主列表(分页).
	 * @Param param 工单主查询条件
	 * @Return 工单主信息列表（分页）
	 */
	IPage<WorkOrderModel> page(WorkOrderPageParam param);

	/**
	 * 分配
	 * @Param param 工单主信息
	 * @Return 分配工单主是否成功
	 */
	Boolean allot(WorkOrderAllotParam param);

	/**
	 * 抢单
	 * @Param param 工单主信息
	 * @Return 抢单是否成功
	 */
	Boolean grab(WorkOrderHandleParam param);

	/**
	 * 转派处理
	 * @Param param 工单主信息
	 * @Return 转派处理工单主是否成功
	 */
	Boolean transferHandle(WorkOrderAllotParam param);

	/**
	 * 转派审核
	 * @Param param 工单主信息
	 * @Return 转派审核工单主是否成功
	 */
	Boolean transferAudit(WorkOrderAllotParam param);

	/**
	 * 关闭.
	 * @Param param 工单主信息
	 * @Return 关闭工单主是否成功
	 */
	Boolean close(WorkOrderCloseParam param);

	/**
	 * 工单处理.
	 * @Param param 工单主信息
	 * @Return 工单处理主是否成功
	 */
	Boolean handle(WorkOrderHandleParam param);

	/**
	 * 工单审核
	 * @Param param 工单主信息
	 * @Return 工单审核是否成功
	 */
	Boolean audit(WorkOrderAuditParam param);

	/**
	 * 删除工单主.
	 * @Param id 工单主标识
	 * @Return 删除工单主是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 工单退回.
	 * @Param param 工单主信息
	 * @Return 工单退回是否成功
	 */
	Boolean fallback(WorkOrderBackParam param);


	/**
	 * 获取处理工单主列表(分页).
	 * @Param param 工单主查询条件
	 * @Return 处理工单主列表（分页）
	 */
	IPage<WorkOrderModel> handlePage(WorkOrderPageParam param);

	WorkOrderModel report(WorkOrderParam param);

	WorkOrderModel appDetail(Long id);

    IPage<WorkOrderModel> historyPage(WorkOrderPageParam param);

    Boolean receive(WorkOrderHandleParam param);

	Boolean evaluation(WorkOrderEvaluationParam param);

	void downloadExcel(WorkOrderPageParam param , HttpServletResponse response);

	void executeWorkOrderOutTime();

	/**
	 * 通过业务id和类型查询工单id
	 * @param param
	 * @return
	 */
	Long queryByBusinessIdAndType(WorkOrderPageParam param);

	/**
	 * 获取工单列表(分页).
	 * @Param param 工单主查询条件
	 * @Return 工单主信息列表（分页）
	 */
	IPage<WorkOrderModel> businessPage(WorkOrderPageParam param);

	/**
	 * 获取工单数量.
	 * @Param source 工单来源
	 * @Return 工单数量
	 */
	Long queryWorkOrderCount(String source, Long tenantId);

	/**
	 * 根据计划创建工单.
	 * @Param plan 工单计划
	 * @Param spaceName 工单空间名称
	 * @Return 工单
	 */
	WorkOrder addPlanWordOrder(WorkPlanModel plan, String spaceName);

    /**
     * 查询超时状态
     * @param id
     * @return
     */
    Integer findOutStatus(Long id);
}
