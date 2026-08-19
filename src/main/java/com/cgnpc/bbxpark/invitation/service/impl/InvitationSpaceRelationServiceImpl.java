
package com.cgnpc.bbxpark.invitation.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.invitation.domain.InvitationSpaceRelation;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationSpaceRelationModel;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationSpaceRelationListParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationSpaceRelationParam;
import com.cgnpc.bbxpark.invitation.mapper.InvitationSpaceRelationRepository;
import com.cgnpc.bbxpark.invitation.service.IInvitationSpaceRelationService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/***
 * @Description 邀约空间关联服务实现
 * @author huangyongtao
 * @date 2025/8/1 14:44
 */
@Service("invitationSpaceRelationService")
public class InvitationSpaceRelationServiceImpl extends ServiceImpl<InvitationSpaceRelationRepository, InvitationSpaceRelation> implements IInvitationSpaceRelationService {


	/**
	 * 获取邀约空间关联列表.
	 * @Param param 邀约空间关联查询条件
	 * @Return 邀约空间关联信息列表
	 */
	@Override
	@SneakyThrows
	public List<InvitationSpaceRelationModel> list(InvitationSpaceRelationListParam param) {
		List<InvitationSpaceRelation> invitationSpaceRelations = this.list(Wrappers.<InvitationSpaceRelation>lambdaQuery()
				.eq(ObjectUtil.isNotEmpty(param.getInviteId()), InvitationSpaceRelation::getInviteId, param.getInviteId())
				.in(CollectionUtil.isNotEmpty(param.getInviteIdList()), InvitationSpaceRelation::getInviteId, param.getInviteIdList())
				.orderByDesc(InvitationSpaceRelation::getCreateTime));
		return BeanUtils.convertListTo(invitationSpaceRelations, InvitationSpaceRelationModel::new);
	}



	/**
	 * 批量新增邀约空间关联.
	 * @Param params 邀约空间关联信息列表
	 * @Return 批量新增邀约空间关联是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<InvitationSpaceRelationParam> params) {
		List<InvitationSpaceRelation> invitationSpaceRelations = BeanUtils.convertListTo(params, InvitationSpaceRelation::new);
		return this.saveBatch(invitationSpaceRelations);
	}

}
