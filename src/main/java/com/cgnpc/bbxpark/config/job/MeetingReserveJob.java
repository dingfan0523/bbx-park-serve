package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.meeting.service.IMeetingReserveService;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huangyongtao
 * @Description 包间预约的定时任务
 * @date 2024/8/2 14:01
 */
@Slf4j
@Component
public class MeetingReserveJob {

    @Autowired
    private IMeetingReserveService meetingReserveService;

    @Autowired
    private IMeetingRoomService meetingRoomService;

    /***
     * @Description 会议的开始，结束，无效状态更新任务(每分钟执行一次)
     * @author huangyongtao
     * @date 2024/9/24 14:04
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    @XxlJob("UpdateReserveTask")
    public void updateReserveTask(){
        //更新进行中的会议
        meetingReserveService.realStartTimeTask();
        //更新结束的会议
        meetingReserveService.realEndTimeTask();
        //更新会议是否有效
        meetingReserveService.invalidTask();
        //更新会议室的使用状态
        meetingRoomService.updateUsedTask();
    }

    /***
     * @Description 会议预约人签到通知任务(每5分钟执行一次)
     * @author huangyongtao
     * @date 2024/8/30 14:04
     */
//    @Scheduled(cron = "0 0/5 * * * ?")
    @XxlJob("SignNoticeTaskJob")
    public void signNoticeTask(){
        meetingReserveService.signNoticeTask();
    }

    /**
     * 会服任务未确认提醒
     */
    @XxlJob("WaitConfirmWarnTask")
    public void waitConfirmWarnTask(){
        meetingReserveService.waitingConfirmNoticeTask();
    }

    /**
     * 会议设备告警任务
     */
    @XxlJob("DeviceWarnTask")
    public void meetingDeviceWarnTask(){meetingReserveService.meetingDeviceWarnTask();}

    /**
     * 会议延时提醒任务
     */
    @XxlJob("DelayTask")
    public void delayTask(){meetingReserveService.meetingDelayTask();}
}
