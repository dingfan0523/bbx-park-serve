
package com.cgnpc.bbxpark.invitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.cgnpc.bbxpark.invitation.domain.ApprovalTask;
import com.cgnpc.bbxpark.invitation.dto.model.ApprovalTaskListModel;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalActionParam;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalTaskParam;

import java.util.List;


public interface IApprovalTaskService extends IService<ApprovalTask> {
	/**
	 * 创建审批任务.
	 * @param param 审批任务信息
	 * @return 创建审批任务是否成功
	 */
	Boolean createTask(ApprovalTaskParam param);

	/**
	 * 获取待审批任务列表(分页).
	 * @param param 审批任务查询条件
	 * @return 审批任务信息列表（分页）
	 */
	IPage<Long> pendingApprovalPage(CudPageDto param);

	/**
	 * 获取已审批任务列表(分页).
	 * @param param 审批任务查询条件
	 * @return 审批任务信息列表（分页）
	 */
	IPage<Long> approvedPage(CudPageDto param);

	/**
	 * 获取指定范围内用户能审批的业务id集合
	 */
	List<Long> getApproveBusinessIds(List<Long> businessIds);

	/**
	 * 获取指定业务id的审批任务(包括审批人集合)集合
	 */
	List<ApprovalTaskListModel> getApprovalTaskList(Long businessId);

	/**
	 * 根据业务进行审批任务审批.
	 * @param param 审批任务审批信息
	 * @return 审批任务审批是否成功
	 */
	Boolean businessApprove(ApprovalActionParam param);

	/**
	 * 取消审批任务.
	 * @param businessId 业务标识
	 * @return 取消审批任务是否成功
	 */
	Boolean cancelTask(List<Long> businessIds);



//	/**
//	 * 根据审批任务标识获得审批任务详情信息.
//	 * @Param [id] 审批任务标识
//	 * @Return 审批任务详情信息
//	 */
//	ApprovalTaskModel detail(Long id);
//
//	/**
//	 * 获取审批任务列表(分页).
//	 * @Param param 审批任务查询条件
//	 * @Return 审批任务信息列表（分页）
//	 */
//	IPage<ApprovalTaskModel> page(ApprovalTaskPageParam param);
//
//	/**
//	 * 获取审批任务列表.
//	 * @Param param 审批任务查询条件
//	 * @Return 审批任务信息列表
//	 */
//	List<ApprovalTaskModel> list(ApprovalTaskListParam param);
//
//	/**
//	 * 新增审批任务.
//	 * @Param param 审批任务信息
//	 * @Return 新增审批任务是否成功
//	 */
//	Boolean add(ApprovalTaskParam param);
//
//	/**
//	 * 批量新增审批任务.
//	 * @Param params 审批任务信息列表
//	 * @Return 批量新增审批任务是否成功
//	 */
//	Boolean addBatch(List<ApprovalTaskParam> params);
//
//	/**
//	 * 删除审批任务.
//	 * @Param id 审批任务标识
//	 * @Return 删除审批任务是否成功
//	 */
//	Boolean remove(Long id);
//
//	/**
//	 * 批量删除审批任务.
//	 * @Param ids 审批任务标识列表
//	 * @Return 批量删除审批任务是否成功
//	 */
//	Boolean removeBatch(List<Long> ids);
//
//	/**
//	 * 编辑审批任务信息.
//	 * @Param param 审批任务信息
//	 * @Return 编辑审批任务是否成功
//	 */
//	Boolean edit(Long id, ApprovalTaskParam param);
//
//	/**
//	 * 批量编辑审批任务信息.
//	 * @Param params 审批任务信息列表
//	 * @Return 批量编辑审批任务是否成功
//	 */
//	Boolean editBatch(List<ApprovalTaskParam> params);
//
//	/**
//	 * 启用审批任务.
//	 * @Param id 审批任务标识
//	 * @Return 启用审批任务是否成功
//	 */
//	Boolean enable(Long id);
//
//	/**
//	 * 批量启用审批任务信息.
//	 * @Param ids 审批任务标识列表
//	 * @Return 批量启用审批任务是否成功
//	 */
//	Boolean enableBatch(List<Long> ids);
//
//	/**
//	 * 禁用审批任务.
//	 * @Param id 审批任务标识
//	 * @Return 禁用审批任务是否成功
//	 */
//	Boolean disable(Long id);
//
//	/**
//	 * 批量禁用审批任务信息.
//	 * @Param ids 审批任务标识列表
//	 * @Return 批量禁用审批任务是否成功
//	 */
//	Boolean disableBatch(List<Long> ids);
}
