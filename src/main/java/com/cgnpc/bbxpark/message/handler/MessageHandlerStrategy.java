package com.cgnpc.bbxpark.message.handler;


import com.cgnpc.bbxpark.config.eventbus.MessageEvent;

/**
 * 消息处理策略
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/28 10:14
 */
public interface MessageHandlerStrategy {

    /**
     * 处理事件消息
     *
     * @param event 事件
     */
    void handler(MessageEvent event);
}
