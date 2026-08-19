package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomDailyStatistics;

public interface IMeetingRoomDailyStatisticsService extends IService<MeetingRoomDailyStatistics> {
    void calculateYesterdayStatistics();
}
