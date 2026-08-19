
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantRoom;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantRoomModel;
import com.cgnpc.bbxpark.meeting.dto.model.SimpleMeetingAttendantModel;

import java.util.List;


/**
 * 会服人员-会议室服务接口
 */
public interface IMeetingAttendantRoomService extends IService<MeetingAttendantRoom> {
    List<MeetingAttendantRoomModel> listByUserIdIn(List<String> userIds);

    List<MeetingAttendantRoomModel> listAllByUserIdIn(List<String> userIds);
    /**
     * 查询会服人员对应的已生效的会议室信息
     *
     * @param userId 用户id
     * @return 集合
     */
    List<MeetingAttendantRoom> listByUserId(String userId);

    /**
     * 查询会服人员对应的全部会议室信息
     *
     * @param userId 用户id
     * @return 集合
     */
    List<MeetingAttendantRoom> listAllByUserId(String userId);

    List<SimpleMeetingAttendantModel> listByRoomId(Long roomId);
}
