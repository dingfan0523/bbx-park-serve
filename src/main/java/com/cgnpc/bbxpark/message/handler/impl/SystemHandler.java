package com.cgnpc.bbxpark.message.handler.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cgnpc.bbxpark.common.enums.MessagePushChannelEnum;
import com.cgnpc.bbxpark.common.enums.MessageSourceEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.message.domain.MessageLog;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticeParam;
import com.cgnpc.bbxpark.message.dto.resp.SystemExtModel;
import com.cgnpc.bbxpark.message.handler.BaseHandler;
import com.cgnpc.bbxpark.message.service.IMessageLogService;
import com.cgnpc.bbxpark.message.service.IMessageNoticeService;
import com.cgnpc.bbxpark.config.eventbus.MessageEvent;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 系统站内信 消息处理策略实现类
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/25 15:23
 */
@Component
@Slf4j
public class SystemHandler extends BaseHandler {
    @Autowired
    private IMessageNoticeService messageNoticeService;
    @Resource
    private IMessageLogService messageLogService;
    @Resource
    @Qualifier("asyncEventBusExecutor")
    private Executor executorService;

    public SystemHandler() {
        channelCode = MessagePushChannelEnum.INFO.getCode();
    }

    /**
     * 处理事件消息
     *
     * @param event 事件消息
     */
    @Override
    public void handler(MessageEvent event) {
        List<MessageNoticeParam> list = assembleParam(event);
        for (MessageNoticeParam param : list) {
            executorService.execute(() -> {
                try {
                    messageNoticeService.add(param);
                    //Result result = messageNoticeFeignClient.add(param).getBody();
                    //log.info("站内信发送结果,参数:{},返回信息:{}", param, result == null ? "null" : result.getResult());
                    //发送成功更新状态
                    LambdaUpdateWrapper<MessageLog> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.in(MessageLog::getId, event.getMessageLogIdList()).eq(MessageLog::getPushUid, param.getReceiverId()).eq(event.getTenantId() != null, MessageLog::getTenantId, event.getTenantId());
                    wrapper.set(MessageLog::getStatus, Status.enabled.getKey()).set(MessageLog::getPushTime, new Date());
                    wrapper.set(MessageLog::getPushRemark,"");
                    messageLogService.update(wrapper);
                } catch (Exception e) {
                    //更新错误原因
                    log.error("站内信发送失败,参数:{},错误信息:\n{}", param, Throwables.getStackTraceAsString(e));
                }
            });
        }
    }

    /**
     * 拼装参数
     */
    private List<MessageNoticeParam> assembleParam(MessageEvent event) {
        SystemExtModel extModel = (SystemExtModel) event.getExtModel();
        return extModel.getUserSet().stream().map(user -> {
            MessageNoticeParam message = new MessageNoticeParam();
            //固定参数
            message.setTypeId(1L);
            message.setStatus(1);
            message.setReceiverScope( 1);
            message.setPublishTime(new Date());
            message.setSendingSettings(1L);
            message.setSummary("");
            message.setCreateTime(new Date());
            message.setCreatorId("0");
            //参数
            message.setReceiverId(user.getId());
            message.setBusinessId(event.getBusinessId());
            message.setType(MessageSourceEnum.getType(event.getType()));
            message.setTitle(event.getTitle());
            message.setContent(event.getContent());
            message.setTenantId(event.getTenantId());
            return message;
        }).collect(Collectors.toList());
    }
}
