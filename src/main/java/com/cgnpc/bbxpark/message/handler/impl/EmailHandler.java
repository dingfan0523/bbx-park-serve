package com.cgnpc.bbxpark.message.handler.impl;

import com.cgnpc.bbxpark.common.enums.MessagePushChannelEnum;
import com.cgnpc.bbxpark.message.handler.BaseHandler;
import com.cgnpc.bbxpark.config.eventbus.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/10/28 14:23
 */
@Component
@Slf4j
public class EmailHandler extends BaseHandler {
    public EmailHandler(){
        channelCode = MessagePushChannelEnum.MAIL.getCode();
    }
    @Override
    public void handler(MessageEvent messageEvent) {

    }
}
