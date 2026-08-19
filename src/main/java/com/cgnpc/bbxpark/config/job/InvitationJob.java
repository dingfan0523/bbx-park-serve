package com.cgnpc.bbxpark.config.job;

import com.cgnpc.bbxpark.invitation.service.IInvitationService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huangyongtao
 * @Description 邀约定时任务
 * @date 2025/8/5 14:01
 */
@Slf4j
@Component
public class InvitationJob {


    @Autowired
    private IInvitationService invitationService;


    /***
     * @Description 邀约状态更新任务(每分钟执行一次)
     * @author huangyongtao
     * @date 2025/8/5 14:04
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    @XxlJob("UpdateInviteStatusTask")
    public void updateInviteStatusTask(){
        //更新邀约状态
        invitationService.updateInviteStatusTask();
    }
}
