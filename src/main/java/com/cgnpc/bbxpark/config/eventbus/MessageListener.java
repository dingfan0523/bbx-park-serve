package com.cgnpc.bbxpark.config.eventbus;

import com.cgnpc.bbxpark.invitation.service.IApprovalTaskService;
import com.cgnpc.bbxpark.invitation.service.IInvitationService;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomService;
import com.cgnpc.bbxpark.message.handler.HandlerHolder;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 消息监听器
 *
 * @author dingfan
 */
@Slf4j
@Component
public class MessageListener {
    @Autowired
    private AsyncEventBus asyncEventBus;
    @Autowired
    private HandlerHolder holder;

    @Autowired
    private IMeetingRoomService meetingRoomService;

    @Autowired
    IInvitationService invitationService;
    @Autowired
    private IApprovalTaskService approvalTaskService;

    @Subscribe
    public void consume(MessageEvent event) {
        log.info("接收消息事件>>>>>>>>>>>>>>{}", event);
        holder.route(event.getChannel()).handler(event);
    }

    @Subscribe
    public void consumeMeetingTask(AttendantTaskEvent event) {
        log.info("接收会服事件>>>>>>>>>>>>>>{}", event);
        meetingRoomService.handleTaskEvent(event);
        log.info("完成会服事件>>>>>>>>>>>>>>{}", event);
    }

    @Subscribe
    public void consumeMeetingDel(MeetingServeDelEvent event) {
        log.info("接收会服删除事件>>>>>>>>>>>>>>{}", event);
        meetingRoomService.handleServeDel(event);
        log.info("完成会服删除事件>>>>>>>>>>>>>>{}", event);
    }

    @Subscribe
    public void consumeInviteEnd(InviteEndEvent event) {
        log.info("接收邀约结束事件>>>>>>>>>>>>>>{}", event);
        approvalTaskService.cancelTask(event.getInviteIdList());
        log.info("完成邀约结束事件>>>>>>>>>>>>>>{}", event);
    }

    @Subscribe
    public void consumeApprovalFinish(ApprovalCompletedEvent event) {
        log.info("接收审批结束事件>>>>>>>>>>>>>>{}", event);
        invitationService.approve(event.getBusinessId(), event.getApproved(), event.getRemark());
        log.info("完成审批结束事件>>>>>>>>>>>>>>{}", event);
    }

    @PostConstruct
    public void register() {
        log.info("注册监听器>>>>>>>>>>>>>>{}");
        asyncEventBus.register(this);
    }
}
