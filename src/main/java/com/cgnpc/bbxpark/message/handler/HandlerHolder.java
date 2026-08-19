package com.cgnpc.bbxpark.message.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * channel->Handler的映射关系
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/28 11:39
 */
@Component
public class HandlerHolder {
    /**
     * 渠道code->消息处理策略实现的容器
     */
    private Map<String, MessageHandlerStrategy> handlers = new HashMap<>(16);

    /**
     * 注册渠道处理策略
     *
     * @param channelCode 渠道code
     * @param handler     消息处理策略实现类
     */
    public void putHandler(String channelCode, MessageHandlerStrategy handler) {
        handlers.put(channelCode, handler);
    }

    /**
     * 获取指定渠道的消息处理策略
     *
     * @param channelCode 渠道code
     * @return 消息处理策略实现类
     */
    public MessageHandlerStrategy route(String channelCode) {
        return handlers.get(channelCode);
    }
}
