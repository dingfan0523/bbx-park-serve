
package com.cgnpc.bbxpark.invitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.invitation.domain.InvitationSpaceRelation;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationSpaceRelationModel;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationSpaceRelationListParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationSpaceRelationParam;

import java.util.List;

/***
 * @Description 邀约空间关联服务接口
 * @author huangyongtao
 * @date 2025/8/1 14:23
 */
public interface IInvitationSpaceRelationService extends IService<InvitationSpaceRelation> {


	/**
	 * 获取邀约空间关联列表.
	 * @Param param 邀约空间关联查询条件
	 * @Return 邀约空间关联信息列表
	 */
	List<InvitationSpaceRelationModel> list(InvitationSpaceRelationListParam param);

	/**
	 * 批量新增邀约空间关联.
	 * @Param params 邀约空间关联信息列表
	 * @Return 批量新增邀约空间关联是否成功
	 */
	Boolean addBatch(List<InvitationSpaceRelationParam> params);
}
