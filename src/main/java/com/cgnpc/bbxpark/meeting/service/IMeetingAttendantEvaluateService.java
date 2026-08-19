
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantEvaluate;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantEvaluateModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantEvaluateListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantEvaluatePageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantEvaluateParam;

import java.util.List;

/***
 * @Description 会议服务评价服务接口
 * @author huangyongtao
 * @date 2024/12/23 16:06
 */
public interface IMeetingAttendantEvaluateService extends IService<MeetingAttendantEvaluate> {


	/**
	 * 获取会议服务评价列表(分页).
	 * @Param param 会议服务评价查询条件
	 * @Return 会议服务评价信息列表（分页）
	 */
	IPage<MeetingAttendantEvaluateModel> page(MeetingAttendantEvaluatePageParam param);

	/**
	 * 获取会议服务评价列表.
	 * @Param param 会议服务评价查询条件
	 * @Return 会议服务评价信息列表
	 */
	List<MeetingAttendantEvaluateModel> list(MeetingAttendantEvaluateListParam param);

	/**
	 * 新增会议服务评价.
	 * @Param param 会议服务评价信息
	 * @Return 新增会议服务评价是否成功
	 */
	Boolean add(MeetingAttendantEvaluateParam param);



	/**
	 * 编辑会议服务评价信息.
	 * @Param param 会议服务评价信息
	 * @Return 编辑会议服务评价是否成功
	 */
	Boolean edit(MeetingAttendantEvaluateParam param);



}
