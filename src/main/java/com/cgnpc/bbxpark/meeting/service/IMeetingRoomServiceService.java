package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomService;

import java.util.List;

/**
 * 会议室-会服服务接口
 * @author dingfan
 * @date 2024-10-09
 */
public interface IMeetingRoomServiceService extends IService<MeetingRoomService> {
    List<MeetingRoomService> listByRoomIds(List<Long> roomIds);

    boolean removeByRoomId(Long roomId);
}
