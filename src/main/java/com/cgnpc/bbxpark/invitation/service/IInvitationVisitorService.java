
package com.cgnpc.bbxpark.invitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.invitation.domain.InvitationVisitor;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationVisitorModel;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationVisitorListParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationVisitorPageParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationVisitorParam;

import java.util.List;

/***
 * @Description 邀约访客服务接口
 * @author huangyongtao
 * @date 2025/8/1 14:26
 */
public interface IInvitationVisitorService extends IService<InvitationVisitor> {

	/**
	 * 获取邀约访客列表(分页).
	 * @Param param 邀约访客查询条件
	 * @Return 邀约访客信息列表（分页）
	 */
	IPage<InvitationVisitorModel> page(InvitationVisitorPageParam param);

	/**
	 * 获取邀约访客列表.
	 * @Param param 邀约访客查询条件
	 * @Return 邀约访客信息列表
	 */
	List<InvitationVisitorModel> list(InvitationVisitorListParam param);


	/**
	 * 批量新增邀约访客.
	 * @Param params 邀约访客信息列表
	 * @Return 批量新增邀约访客是否成功
	 */
	Boolean addBatch(List<InvitationVisitorParam> params);

}
