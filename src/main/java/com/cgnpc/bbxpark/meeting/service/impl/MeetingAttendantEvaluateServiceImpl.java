
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.service.IAttentionManageService;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantEvaluate;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantEvaluateModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantEvaluateListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantEvaluatePageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantEvaluateParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantEvaluateRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantEvaluateService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/***
 * @Description 会议服务评价服务实现
 * @author huangyongtao
 * @date 2024/12/23 16:33
 */
@Service("meetingAttendantEvaluateService")
public class MeetingAttendantEvaluateServiceImpl extends ServiceImpl<MeetingAttendantEvaluateRepository, MeetingAttendantEvaluate> implements IMeetingAttendantEvaluateService {


	@Autowired
	private IUserApiService userApiService;

	@Autowired
	IAttentionManageService attentionManageService;

	@Autowired
	private IMessageCommonService messageCommonService;

	/**
	 * 获取会议服务评价列表(分页).
	 * @Param param 会议服务评价查询条件
	 * @Return 会议服务评价信息列表（分页）
	 */
	@Override
	public IPage<MeetingAttendantEvaluateModel> page(MeetingAttendantEvaluatePageParam param) {
		IPage<MeetingAttendantEvaluate> page =  this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtils.isEmpty(page.getRecords())) {
			return ConvertUtil.pageEmptyConvert(page.getCurrent(),page.getSize());
		}
		return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(), BeanUtils.convertListTo(page.getRecords(), MeetingAttendantEvaluateModel::new));
	}

	/**
	 * 获取会议服务评价列表.
	 * @Param param 会议服务评价查询条件
	 * @Return 会议服务评价信息列表
	 */
	@Override
	@SneakyThrows
	public List<MeetingAttendantEvaluateModel> list(MeetingAttendantEvaluateListParam param) {
		MeetingAttendantEvaluatePageParam pageParam = BeanUtils.convertTo(param, MeetingAttendantEvaluatePageParam::new);
		List<MeetingAttendantEvaluate> list = this.list(buildQuery(pageParam));
		return CollectionUtil.isEmpty(list) ? new ArrayList<>(): BeanUtils.convertListTo(list, MeetingAttendantEvaluateModel::new);
	}

	private LambdaQueryWrapper<MeetingAttendantEvaluate> buildQuery(MeetingAttendantEvaluatePageParam param) {
		LambdaQueryWrapper<MeetingAttendantEvaluate> query = new LambdaQueryWrapper<>();
		query.eq(ObjectUtil.isNotEmpty(param.getReserveId()), MeetingAttendantEvaluate::getReserveId, param.getReserveId());
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MeetingAttendantEvaluate::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByDesc(MeetingAttendantEvaluate::getCreateTime);
		return query;
	}

	/**
	 * 新增会议服务评价.
	 * @Param param 会议服务评价信息
	 * @Return 新增会议服务评价是否成功
	 */
	@Override
	public Boolean add(MeetingAttendantEvaluateParam param) {
		MeetingAttendantEvaluate meetingAttendantEvaluate = BeanUtils.convertTo(param, MeetingAttendantEvaluate::new);
		meetingAttendantEvaluate.setId(null);
		String userId = WebFrameworkUtils.getHeaderUserId();
		UserInfoModel user = userApiService.detail(userId);
		meetingAttendantEvaluate.setOperateStaffid(user.getStaffid());
		meetingAttendantEvaluate.setOperateUid(userId);
		meetingAttendantEvaluate.setOperateUname(user.getUserName());
		this.save(meetingAttendantEvaluate);
		sendAttentionMessage(meetingAttendantEvaluate, user);
		return true;
	}

	/**
	 * 编辑会议服务评价信息.
	 * @Param param 会议服务评价信息
	 * @Return 编辑会议服务评价是否成功
	 */
	@Override
	public Boolean edit(MeetingAttendantEvaluateParam param) {
		MeetingAttendantEvaluate meetingAttendantEvaluate = this.getById(param.getId());
		AssertUtils.notNull(meetingAttendantEvaluate, SystemResultCode.RESULT_DATA_NONE.message());
		MeetingAttendantEvaluate editParam = BeanUtils.convertTo(param, MeetingAttendantEvaluate::new);
		return this.updateById(editParam);
	}


	/***
	 * @Description 关注人消息
	 * @author huangyongtao
	 * @date 2025/3/31 16:02
	 * @param evaluate
	 */
	private void sendAttentionMessage(MeetingAttendantEvaluate evaluate, UserInfoModel userInfo) {
		if(!attentionManageService.checkAttention(userInfo.getId())){
			return;
		}
		Map<String, String> variables = new HashMap<>(4);
		variables.put("type", "会服");
		variables.put("attentionName", userInfo.getUserName());
		variables.put("content", evaluate.getContent());
		variables.put("score", evaluate.getScore() + "分");
		messageCommonService.sendMessage(MessageConstant.ATTENTION_EVALUATE_NOTICE, evaluate.getTenantId(), evaluate.getId(), new HashSet<>(), variables);
	}
}
