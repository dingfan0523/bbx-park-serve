package com.cgnpc.bbxpark.common.service.impl;


import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.message.dto.req.MessageSendParam;
import com.cgnpc.bbxpark.message.service.IMessageSendService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 消息中心公用服务
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/29 17:10
 */
@Service
@Slf4j
public class MessageCommonServiceImpl implements IMessageCommonService {

    /**
     * 消息发送服务接口.
     */
    @Resource
    private IMessageSendService messageSendService;


    @Override
    @Async("asyncEventBusExecutor")
    public void sendMessage(String templateCode, Long tenantId, Long businessId, String receiver, Map<String, String> variables) {
        sendMessage(templateCode, tenantId, businessId, Collections.singleton(receiver), variables);
    }

    @Override
    @Async("asyncEventBusExecutor")
    public void sendMessage(String templateCode, Long tenantId, Long businessId, Set<String> receivers, Map<String, String> variables) {
        MessageSendParam param = new MessageSendParam();
        try {
            param.setTemplateCode(templateCode);
            param.setTenantId(tenantId);
            param.setBusinessId(businessId);
            param.setReceivers(receivers);
            param.setVariables(variables);
            messageSendService.sendByTemplate(param);
        } catch (Exception e) {
            log.error("调用消息中心失败,参数:{},错误:{}", param, Throwables.getStackTraceAsString(e));
        }
    }
}
