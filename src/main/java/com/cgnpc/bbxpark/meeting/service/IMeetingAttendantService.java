
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendant;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantModel;
import com.cgnpc.bbxpark.meeting.dto.model.SimpleMeetingAttendantModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantRoomParam;

import java.util.List;

/**
 * 会服人员服务接口
 */
public interface IMeetingAttendantService extends IService<MeetingAttendant> {
    List<MeetingAttendantModel> findList();

    List<MeetingAttendantModel> findList(String userName);
	/**
	 * 获取会服人员列表(分页).
	 * @Param param 会服人员查询条件
	 * @Return 会服人员信息列表（分页）
	 */
	IPage<MeetingAttendantModel> page(MeetingAttendantPageParam param);


	/**
	 * 根据会议室标识获取会服人员集合
	 * @param roomId 会议室id
	 * @return 会服人员集合
	 */
	List<SimpleMeetingAttendantModel> listByRoomId(Long roomId);

	/***
	 * PC端-关联会议室
	 */
	Boolean bindRoom(MeetingAttendantRoomParam param);

	/**
	 * 根据会服人员标识获得会服人员详情信息.
	 * @Param [id] 会服人员标识
	 * @Return 会服人员详情信息
	 */
	MeetingAttendantDetailModel detail(String id);

	/**
	 * 会服任务更改任务
	 * @return true/false
	 */
	Boolean updateTask();

	List<String> findUnCompleteTask(String userId);

	/**
	 * 获取登录的会服人员信息.
	 * @Return 会服人员详情信息
	 */
	MeetingAttendantModel detailByLogin();

}
