package com.cgnpc.bbxpark.message.service;


import com.cgnpc.bbxpark.message.dto.req.MessageSendParam;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/10/28 9:08
 */
public interface IMessageSendService {
    /**
     * 根据模板发送消息
     *
     * @param param 参数
     * @return 发送结果
     */
    boolean sendByTemplate(MessageSendParam param);

    /**
     * 根据id重发消息
     *
     * @param id id
     */
    boolean sendById(Long id);
}
