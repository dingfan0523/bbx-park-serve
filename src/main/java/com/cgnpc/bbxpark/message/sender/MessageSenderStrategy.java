package com.cgnpc.bbxpark.message.sender;


import com.cgnpc.bbxpark.message.dto.resp.CommonUser;
import com.cgnpc.bbxpark.config.eventbus.MessageEvent;

import java.util.List;

/**
 * 消息发送策略
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/28 10:14
 */
public interface MessageSenderStrategy {
    /**
     * 组装消息event
     *
     * @param event 事件
     * @param list  用户集合
     */
    void assembleEvent(MessageEvent event, List<CommonUser> list);

    /**
     * 校验消息event
     *
     * @param event 事件
     */
    boolean verifyEvent(MessageEvent event);
}
