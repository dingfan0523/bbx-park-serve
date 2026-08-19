
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.DateUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingTempReserve;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingTempReserveModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingReserveParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingTempReserveListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingTempReserveParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingTempReserveRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingTempReserveService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/***
 * @Description 会议临时预约服务实现
 * @author huangyongtao
 * @date 2025/1/6 16:09
 */
@Service("meetingTempReserveService")
public class MeetingTempReserveServiceImpl extends ServiceImpl<MeetingTempReserveRepository, MeetingTempReserve> implements IMeetingTempReserveService {

	@Autowired
	private IUserApiService userApiService;

	@Autowired
	private MeetingReserveRepository meetingReserveRepository;

	@Autowired
	private IMessageCommonService messageCommonService;


	/**
	 * 根据会议临时预约标识获得会议临时预约详情信息.
	 * @Param [id] 会议临时预约标识
	 * @Return 会议临时预约详情信息
	 */
	@Override
	public MeetingTempReserveModel detail(Long id) {
		MeetingTempReserve meetingTempReserve = this.getById(id);
		AssertUtils.notNull(meetingTempReserve, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(meetingTempReserve, MeetingTempReserveModel::new);
	}


	/**
	 * 获取会议临时预约列表.
	 * @Param param 会议临时预约查询条件
	 * @Return 会议临时预约信息列表
	 */
	@Override
	@SneakyThrows
	public List<MeetingTempReserveModel> list(MeetingTempReserveListParam param) {
		List<MeetingTempReserve> list = list(Wrappers.<MeetingTempReserve>lambdaQuery().eq(ObjectUtil.isNotEmpty(param.getRoomId()), MeetingTempReserve::getRoomId, param.getRoomId())
				.gt(ObjectUtil.isNotEmpty(param.getStartTime()), MeetingTempReserve::getStartTime, param.getStartTime())
				.lt(ObjectUtil.isNotEmpty(param.getEndTime()), MeetingTempReserve::getEndTime, param.getEndTime())
				.gt(ObjectUtil.isNotEmpty(param.getLocalTime()), MeetingTempReserve::getEndTime, param.getLocalTime())
				.eq(ObjectUtil.isNotEmpty(param.getTenantId()), MeetingTempReserve::getTenantId, param.getTenantId())
				.in(ObjectUtil.isNotEmpty(param.getRoomIdList()), MeetingTempReserve::getRoomId, param.getRoomIdList())
				.eq(ObjectUtil.isNotEmpty(param.getRoomId()), MeetingTempReserve::getRoomId, param.getRoomId())
				.orderByAsc(MeetingTempReserve::getCreateTime));
		return BeanUtils.convertListTo(list, MeetingTempReserveModel::new);
	}

	/**
	 * 新增会议临时预约.
	 * @Param param 会议临时预约信息
	 * @Return 新增会议临时预约是否成功
	 */
	@Override
	public Boolean add(MeetingTempReserveParam param) {
		AssertUtils.notNull(param.getStartTime(), "临时占用开始时间不能为空");
		AssertUtils.notNull(param.getEndTime(), "临时占用结束时间不能为空");
		AssertUtils.notNull(param.getRoomId(), "会议室信息不能为空");
		AssertUtils.notNull(param.getRoomName(), "会议室信息不能为空");
		MeetingTempReserve meetingTempReserve = BeanUtils.convertTo(param, MeetingTempReserve::new);
		meetingTempReserve.setId(null);
		//用户信息
		String userId = WebFrameworkUtils.getHeaderUserId();
		UserInfoModel user = getUser(userId);
		meetingTempReserve.setReserveUname(user.getUserName());
		meetingTempReserve.setReserveStaffid(user.getStaffid());
		meetingTempReserve.setReserveUid(userId);
		MeetingReserveParam timeParam = new MeetingReserveParam();
		timeParam.setStartTime(meetingTempReserve.getStartTime());
		timeParam.setEndTime(meetingTempReserve.getEndTime());
		timeParam.setRoomId(meetingTempReserve.getRoomId());
		meetingReserveRepository.checkTime(timeParam);
		checkTempTime(param);
		if(this.save(meetingTempReserve)){
			//临时会议预约成功通知
			Map<String, String> variables = new HashMap<>(4);
			variables.put("createDate", DateUtils.format(new Date()));
			variables.put("date", DateUtils.format(meetingTempReserve.getStartTime(), "yyyy-MM-dd HH:mm") + "-" + DateUtils.format(meetingTempReserve.getEndTime(), "HH:mm"));
			variables.put("roomName",meetingTempReserve.getRoomName());
			messageCommonService.sendMessage(MessageConstant.TEMP_MEETING_RESERVE,meetingTempReserve.getTenantId(),null,meetingTempReserve.getReserveUid(),variables);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean checkTempTime(MeetingTempReserveParam param) {
        int count = this.count(Wrappers.<MeetingTempReserve>lambdaQuery().eq(MeetingTempReserve::getRoomId, param.getRoomId())
                .lt(MeetingTempReserve::getStartTime, param.getEndTime())
                .gt(MeetingTempReserve::getEndTime, param.getStartTime())
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MeetingTempReserve::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        AssertUtils.isFalse(count>0,"会议室该预约时间段已被临时占用，请重新预约");
		return true;
	}

	/***
	 * @Description 获取用户信息
	 * @author huangyongtao
	 * @date 2024/8/2 10:34
	 * @param userId
	 */
	private UserInfoModel getUser(String userId) {
		return Objects.requireNonNull(userApiService.detail(userId));
	}

}
