
package com.cgnpc.bbxpark.invitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.invitation.domain.ApprovalRecord;
import com.cgnpc.bbxpark.invitation.dto.model.ApprovalRecordModel;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalRecordListParam;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalRecordPageParam;

import java.util.List;


public interface IApprovalRecordService extends IService<ApprovalRecord> {

	/**
	 * 获取审批记录列表(分页).
	 * @Param param 审批记录查询条件
	 * @Return 审批记录信息列表（分页）
	 */
	IPage<Long> page(ApprovalRecordPageParam param);

	/**
	 * 根据业务id获取审批记录列表
	 */
	List<ApprovalRecordModel> list(ApprovalRecordListParam param);
}
