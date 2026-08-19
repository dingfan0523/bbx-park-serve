package com.cgnpc.bbxpark.message.sender.impl;

import com.cgnpc.bbxpark.common.enums.MessagePushChannelEnum;
import com.cgnpc.bbxpark.common.enums.MessageSourceEnum;
import com.cgnpc.bbxpark.message.dto.resp.CommonUser;
import com.cgnpc.bbxpark.message.dto.resp.DingDingWorkNoticeExtModel;
import com.cgnpc.bbxpark.message.sender.BaseSender;
import com.cgnpc.bbxpark.config.eventbus.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 钉钉工作通知 消息发送策略实现类
 *
 * @author 3y
 */
@Slf4j
@Service
public class DingDingWorkNoticeSender extends BaseSender {
    @Value("${message.restaurant.path:http://10.15.15.58:10002/?redirectUrl=/pages/room-reservation/reservation-detail}")
    private String restaurantPath;
    @Value("${message.order.path:http://10.15.15.58:10002/?redirectUrl=/pages/work-order/detail}")
    private String orderPath;
    @Value("${message.meeting.path:http://10.15.15.58:10002/?redirectUrl=/pages/smart-meeting/reservation-detail}")
    private String meetingPath;
    @Value("${message.meeting.attendant.path:http://10.15.15.58:10002/?redirectUrl=/pages/meeting-service/detail}")
    private String meetingAttendantPath;

    public DingDingWorkNoticeSender() {
        channelCode = MessagePushChannelEnum.DING.getCode();
    }

    /**
     * 组装消息event
     *
     * @param event event
     * @param list  用户集合
     */
    @Override
    public void assembleEvent(MessageEvent event, List<CommonUser> list) {
        DingDingWorkNoticeExtModel model = new DingDingWorkNoticeExtModel();
        //用户钉钉id
        model.setUserSet(list.stream().filter(user -> StringUtils.isNotEmpty(user.getThirdUserId())).collect(Collectors.toSet()));
        String url = getUrl(event.getType(),event.getBusinessId());
        if (url != null) {
            model.setSingleTitle("查看详情");
            model.setSingleUrl(url + "&id=" + event.getBusinessId());
        }
        event.setExtModel(model);
    }

    /**
     * 校验消息event
     *
     * @param event 事件
     * @return 校验结果
     */
    @Override
    public boolean verifyEvent(MessageEvent event) {
        DingDingWorkNoticeExtModel model = (DingDingWorkNoticeExtModel) event.getExtModel();
        if (CollectionUtils.isEmpty(model.getUserSet())) {
            log.error("钉钉工作通知参数校验失败,参数:{}", event);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private String getUrl(String type, Long businessId) {
        if (businessId == null) {
            return null;
        }
        if (MessageSourceEnum.RESTAURANT.getCode().equals(type)) {
            return restaurantPath;
        } else if (MessageSourceEnum.ORDER.getCode().equals(type)) {
            return orderPath;
        } else if (MessageSourceEnum.MEETING.getCode().equals(type)) {
            return meetingPath;
        } else if (MessageSourceEnum.MEETING_ATTENDANT.getCode().equals(type)) {
            return meetingAttendantPath;
        }
        return null;
    }
}

