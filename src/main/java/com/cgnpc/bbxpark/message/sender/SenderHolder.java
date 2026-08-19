package com.cgnpc.bbxpark.message.sender;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * channel->Sender的映射关系
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/28 11:39
 */
@Component
public class SenderHolder {
    /**
     * 渠道code->发送策略实现的容器
     */
    private Map<String, MessageSenderStrategy> senders = new HashMap<>(16);

    /**
     * 注册渠道发送策略
     *
     * @param channelCode 渠道code
     * @param handler     消息发送策略实现类
     */
    public void putHandler(String channelCode, MessageSenderStrategy handler) {
        senders.put(channelCode, handler);
    }

    /**
     * 获取指定渠道的消息发送策略
     *
     * @param channelCode 渠道code
     * @return 消息发送策略实现类
     */
    public MessageSenderStrategy route(String channelCode) {
        return senders.get(channelCode);
    }
}
