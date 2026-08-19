package com.cgnpc.bbxpark.message.handler.impl;


import com.cgnpc.bbxpark.common.enums.MessagePushChannelEnum;
import com.cgnpc.bbxpark.config.eventbus.MessageEvent;
import com.cgnpc.bbxpark.ioc.dto.model.ScreenMessageModel;
import com.cgnpc.bbxpark.ioc.service.WebSocketService;
import com.cgnpc.bbxpark.message.handler.BaseHandler;
import com.cgnpc.bbxpark.message.service.IMessageLogService;
import com.cgnpc.bbxpark.message.service.IMessageNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.Executor;

/**
 * 大屏消息推送 消息处理策略实现类
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/25 15:23
 */
@Component
@Slf4j
public class ScreenHandler extends BaseHandler {
    @Autowired
    private IMessageNoticeService messageNoticeService;
    @Resource
    private IMessageLogService messageLogService;
    @Resource
    @Qualifier("asyncEventBusExecutor")
    private Executor executorService;
    @Autowired
    private WebSocketService webSocketService;

    public ScreenHandler() {
        channelCode = MessagePushChannelEnum.SCREEN.getCode();
    }

    /**
     * 处理事件消息
     *
     * @param event 事件消息
     */
    @Override
    public void handler(MessageEvent event) {
        ScreenMessageModel message = new ScreenMessageModel();
        message.setType(message.getType());
        message.setTitle(event.getTitle());
        message.setContent(event.getContent());
        message.setDate(new Date());
        message.setId(RandomUtils.nextLong(1,1000000000));
        webSocketService.sendNotifyToScreen(event.getTenantId() + "",message);
    }
}
