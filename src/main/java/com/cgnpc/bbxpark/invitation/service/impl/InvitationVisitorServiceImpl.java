
package com.cgnpc.bbxpark.invitation.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.invitation.domain.InvitationVisitor;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationVisitorModel;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationVisitorListParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationVisitorPageParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationVisitorParam;
import com.cgnpc.bbxpark.invitation.mapper.InvitationVisitorRepository;
import com.cgnpc.bbxpark.invitation.service.IInvitationVisitorService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/***
 * @Description 邀约访客服务实现
 * @author huangyongtao
 * @date 2025/8/1 14:46
 */
@Service("invitationVisitorService")
public class InvitationVisitorServiceImpl extends ServiceImpl<InvitationVisitorRepository, InvitationVisitor> implements IInvitationVisitorService {

    @Autowired
    private InvitationVisitorRepository invitationVisitorRepository;

	/**
	 * 获取邀约访客列表(分页).
	 * @Param param 邀约访客查询条件
	 * @Return 邀约访客信息列表（分页）
	 */
	@Override
	public IPage<InvitationVisitorModel> page(InvitationVisitorPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        param.setUserId(WebFrameworkUtils.getHeaderUserId());
		// 执行连表分页查询
		IPage<InvitationVisitorModel> result = invitationVisitorRepository.page(new Page<>(param.getCurrent(), param.getSize()), param);
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<InvitationVisitorModel> models = result.getRecords();
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(), models);
	}

	/**
	 * 获取邀约访客列表.
	 * @Param param 邀约访客查询条件
	 * @Return 邀约访客信息列表
	 */
	@Override
	@SneakyThrows
	public List<InvitationVisitorModel> list(InvitationVisitorListParam param) {
		List<InvitationVisitor> invitationVisitors = this.list(Wrappers.<InvitationVisitor>lambdaQuery()
				.eq(ObjectUtil.isNotEmpty(param.getInviteId()), InvitationVisitor::getInviteId, param.getInviteId())
				.in(CollectionUtil.isNotEmpty(param.getInviteIdList()), InvitationVisitor::getInviteId, param.getInviteIdList())
				.orderByDesc(InvitationVisitor::getCreateTime));
		return BeanUtils.convertListTo(invitationVisitors, InvitationVisitorModel::new);
	}


	/**
	 * 批量新增邀约访客.
	 * @Param params 邀约访客信息列表
	 * @Return 批量新增邀约访客是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<InvitationVisitorParam> params) {
		List<InvitationVisitor> invitationVisitors = BeanUtils.convertListTo(params, InvitationVisitor::new);
		return this.saveBatch(invitationVisitors);
	}

}
