
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantTaskDetail;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskDetailModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskDetailListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskDetailParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantTaskDetailRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantTaskDetailService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
/***
 * @Description 会服任务详情服务实现
 * @author huangyongtao
 * @date 2024/12/23 17:31
 */
@Service("meetingAttendantTaskDetailService")
public class MeetingAttendantTaskDetailServiceImpl extends ServiceImpl<MeetingAttendantTaskDetailRepository, MeetingAttendantTaskDetail> implements IMeetingAttendantTaskDetailService {

	/**
	 * 根据会服任务详情标识获得会服任务详情详情信息.
	 * @Param [id] 会服任务详情标识
	 * @Return 会服任务详情详情信息
	 */
	@Override
	public MeetingAttendantTaskDetailModel detail(MeetingAttendantTaskDetailParam param) {
		MeetingAttendantTaskDetail meetingAttendantTaskDetail = this.getById(param.getId());
		AssertUtils.notNull(meetingAttendantTaskDetail, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(meetingAttendantTaskDetail, MeetingAttendantTaskDetailModel::new);
	}


	/**
	 * 获取会服任务详情列表.
	 * @Param param 会服任务详情查询条件
	 * @Return 会服任务详情信息列表
	 */
	@Override
	@SneakyThrows
	public List<MeetingAttendantTaskDetailModel> list(MeetingAttendantTaskDetailListParam param) {
		List<MeetingAttendantTaskDetail> details =  this.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery()
				.eq(ObjectUtil.isNotEmpty(param.getServiceAttribute()),MeetingAttendantTaskDetail::getServiceAttribute, param.getServiceAttribute())
				.eq(MeetingAttendantTaskDetail::getTaskId, param.getTaskId())
				.orderByAsc(MeetingAttendantTaskDetail::getCreateTime));
		return CollectionUtil.isEmpty(details) ? new ArrayList<>() : BeanUtils.convertListTo(details, MeetingAttendantTaskDetailModel::new);
	}

	/**
	 * 新增会服任务详情.
	 * @Param param 会服任务详情信息
	 * @Return 新增会服任务详情是否成功
	 */
	@Override
	public Boolean add(MeetingAttendantTaskDetail param) {
		MeetingAttendantTaskDetail meetingAttendantTaskDetail = BeanUtils.convertTo(param, MeetingAttendantTaskDetail::new);
		meetingAttendantTaskDetail.setId(null);
		return this.save(param);
	}

	/**
	 /**
	 * 批量新增会服人员任务.
	 * @Param params 会服人员任务信息列表
	 * @Return 批量新增会服人员任务是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<MeetingAttendantTaskDetail> params) {
		return this.saveBatch(params);
	}

	@Override
	public Boolean removeByTaskIds(List<Long> taskIds) {
		return remove(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery().in(MeetingAttendantTaskDetail::getTaskId, taskIds));
	}

	@Override
	public List<MeetingAttendantTaskDetail> findByTaskId(Long taskId) {
		List<MeetingAttendantTaskDetail> details =  this.list(Wrappers.<MeetingAttendantTaskDetail>lambdaQuery()
				.eq(MeetingAttendantTaskDetail::getTaskId, taskId)
				.orderByAsc(MeetingAttendantTaskDetail::getCreateTime));
		return CollectionUtil.isEmpty(details) ? new ArrayList<>() : details;
	}


}
