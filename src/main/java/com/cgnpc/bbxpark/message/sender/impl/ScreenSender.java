package com.cgnpc.bbxpark.message.sender.impl;

import com.cgnpc.bbxpark.common.enums.MessagePushChannelEnum;
import com.cgnpc.bbxpark.config.eventbus.MessageEvent;
import com.cgnpc.bbxpark.message.dto.resp.CommonUser;
import com.cgnpc.bbxpark.message.dto.resp.SystemExtModel;
import com.cgnpc.bbxpark.message.sender.BaseSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统站内信消息处理器
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/25 15:23
 */
@Component
@Slf4j
public class ScreenSender extends BaseSender {

    public ScreenSender() {
        channelCode = MessagePushChannelEnum.SCREEN.getCode();
    }

    /**
     * 组装消息event
     *
     * @param event 事件
     * @param list  用户集合
     */
    @Override
    public void assembleEvent(MessageEvent event, List<CommonUser> list) {
        SystemExtModel model = new SystemExtModel();
        //用户id
        model.setUserSet(list.stream().filter(user->user.getId() != null).collect(Collectors.toSet()));
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
        SystemExtModel model = (SystemExtModel) event.getExtModel();
        if (CollectionUtils.isEmpty(model.getUserSet())) {
            log.error("大屏消息参数校验失败,参数:{}", event);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
