package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantService;
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
public class MeetingAttendantJob {

    @Autowired
    private IMeetingAttendantService meetingAttendantService;

    /**
     * 会服人员及关联会议室变更
     */
    @XxlJob("UpdateAttendantTask")
    public void UpdateAttendantTask(){
        meetingAttendantService.updateTask();
    }
}
