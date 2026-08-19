package com.cgnpc.bbxpark.message.sender;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 消息发送策略实现类的基础抽象类
 * <p>
 * 实现了消息发送策略注册的通用功能
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/28 11:47
 */
public abstract class BaseSender implements MessageSenderStrategy {
    @Resource
    private SenderHolder holder;
    /**
     * 渠道code
     */
    protected String channelCode;

    @PostConstruct
    private void init() {
        holder.putHandler(channelCode, this);
    }
}
