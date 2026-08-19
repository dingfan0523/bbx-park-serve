
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomDeviceRel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingRoomDeviceRelModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingRoomDeviceRelListParam;

import java.util.List;

/***
 * @Description 会议室设备关联服务接口
 * @author huangyongtao
 * @date 2024/8/23 15:42
 */
public interface IMeetingRoomDeviceRelService extends IService<MeetingRoomDeviceRel> {

	/**
	 * 获取会议室设备关联列表.
	 * @Param param 会议室设备关联查询条件
	 * @Return 会议室设备关联信息列表
	 */
	List<MeetingRoomDeviceRelModel> list(MeetingRoomDeviceRelListParam param);

	/**
	 * 新增会议室设备关联.
	 * @Param param 会议室设备关联信息
	 * @Return 新增会议室设备关联是否成功
	 */
	Boolean add(List<MeetingRoomDeviceRel> meetingRoomDeviceRels);


	/**
	 * 删除会议室设备关联.
	 * @Param id 会议室id
	 * @Return 删除会议室设备关联是否成功
	 */
	Boolean remove(Long roomId);

}
