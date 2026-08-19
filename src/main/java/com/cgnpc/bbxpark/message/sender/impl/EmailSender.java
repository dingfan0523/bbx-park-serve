package com.cgnpc.bbxpark.message.sender.impl;

import com.cgnpc.bbxpark.common.enums.MessagePushChannelEnum;
import com.cgnpc.bbxpark.message.dto.resp.CommonUser;
import com.cgnpc.bbxpark.message.sender.BaseSender;
import com.cgnpc.bbxpark.config.eventbus.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/10/28 14:23
 */
@Component
@Slf4j
public class EmailSender extends BaseSender {
    public EmailSender() {
        channelCode = MessagePushChannelEnum.MAIL.getCode();
    }

    @Override
    public void assembleEvent(MessageEvent event, List<CommonUser> list) {

    }

    @Override
    public boolean verifyEvent(MessageEvent event) {
        return false;
    }
}
