package com.cgnpc.bbxpark.message.handler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 消息处理策略实现类的基础抽象类
 * 实现了消息处理策略注册的通用功能
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/28 11:47
 */
public abstract class BaseHandler implements MessageHandlerStrategy {
    @Resource
    private HandlerHolder holder;
    /**
     * 渠道code
     */
    protected String channelCode;

    @PostConstruct
    private void init() {
        holder.putHandler(channelCode, this);
    }
}
