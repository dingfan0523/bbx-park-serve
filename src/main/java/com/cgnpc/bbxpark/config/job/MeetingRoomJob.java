package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.meeting.service.IMeetingRoomDailyStatisticsService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * @Description 物联设备定时任务
 * @author huangyongtao
 * @date 2025/4/23 14:28
 */
@Slf4j
@Component
public class MeetingRoomJob {

    @Autowired
    private IMeetingRoomDailyStatisticsService meetingRoomDailyStatisticsService;

  /***
   * @Description 会议室利率用统计任务（凌晨3点执行一次）
   */
    @XxlJob("MeetingRoomDailyStatisticsJob")
    public void executeMeetingRoomDailyStatistics(){
         log.info("会议室利率用统计任务开始执行");
        meetingRoomDailyStatisticsService.calculateYesterdayStatistics();
    }
}
