package com.cgnpc.bbxpark.message.service.impl;


import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.message.domain.MessageLog;
import com.cgnpc.bbxpark.message.domain.MessageTemplate;
import com.cgnpc.bbxpark.message.dto.req.MessageSendParam;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplateListParam;
import com.cgnpc.bbxpark.message.dto.resp.CommonUser;
import com.cgnpc.bbxpark.message.dto.resp.MessageTemplateModel;
import com.cgnpc.bbxpark.message.dto.resp.MessageUserInfoModel;
import com.cgnpc.bbxpark.message.mapper.MessageTemplateRepository;
import com.cgnpc.bbxpark.message.sender.MessageSenderStrategy;
import com.cgnpc.bbxpark.message.sender.SenderHolder;
import com.cgnpc.bbxpark.message.service.IMessageLogService;
import com.cgnpc.bbxpark.message.service.IMessageSendService;
import com.cgnpc.bbxpark.message.service.IMessageTemplateService;
import com.cgnpc.bbxpark.space.dto.param.UserInfoListParam;
import com.cgnpc.bbxpark.config.eventbus.MessageEvent;
import com.google.common.eventbus.AsyncEventBus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/10/28 9:08
 */
@Service
@Slf4j
public class MessageSendServiceImpl implements IMessageSendService {
    @Resource
    private MessageTemplateRepository messageTemplateRepository;
    @Resource
    private IMessageTemplateService templateService;
    @Resource
    private IMessageLogService messageLogService;
    @Resource
    private AsyncEventBus asyncEventBus;
    @Resource
    private SenderHolder holder;

    /**
     * 根据模板发送消息
     *
     * @param param 参数
     */
    public boolean sendByTemplate(MessageSendParam param) {
        //查询模板信息
        MessageTemplateModel templateModel = findTemplate(param.getTemplateCode(),param.getTenantId());
        //模板前置检查
        AssertUtils.isTrue(preVerifyTemplate(templateModel, param.getTemplateCode()), "消息模板校验失败");
        //推送人合并(系统默认推送人+模板配置推送人)
        assert templateModel != null;
        List<MessageUserInfoModel> receivers = getReceivers(param.getTemplateCode(), param.getReceivers(), templateModel.getUserModelList());
        AssertUtils.isTrue(CollectionUtils.isNotEmpty(receivers), "消息模板推送人为空");

        MessageTemplate template = BeanUtils.convertTo(templateModel, MessageTemplate::new);
        String[] channelArray = template.getPushChannel().split(",");
        //遍历发送
        for (String channel : channelArray) {
            try {
                //选择渠道实现
                MessageSenderStrategy strategy = holder.route(channel);
                //组装渠道参数
                MessageEvent event = buildMessageEvent(template, param, channel);
                strategy.assembleEvent(event, BeanUtils.convertListTo(receivers, CommonUser::new));
                //保存日志
                if (!saveLog(event, receivers)) {
                    log.error("消息日志保存失败,参数:{},用户参数:\n{}", event, receivers);
                    return false;
                }
                if(strategy.verifyEvent(event)){
                    //参数校验成功,才发送消息
                    asyncEventBus.post(event);
                }
            } catch (Exception e) {
                log.error("消息发送失败,渠道:{},参数:\n{}", channel, param);
            }
        }
        return true;
    }

    /**
     * 根据id重新发送消息
     *
     * @param id 消息id
     */
    public boolean sendById(Long id) {
        MessageLog messageLog = messageLogService.getById(id);
        AssertUtils.notNull(messageLog, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.isFalse(Objects.equals(Status.enabled.getKey(), messageLog.getStatus()), "无法重复发送");
        //更新发送时间
        messageLog.setPushTime(new Date());
        messageLogService.updateById(messageLog);
        MessageEvent event = BeanUtils.convertTo(messageLog, MessageEvent.builder()::build);
        event.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        UserInfoListParam userParam = new UserInfoListParam();
        userParam.setId(messageLog.getPushUid());
        List<MessageUserInfoModel> sysUserList = templateService.findUserList(userParam);
        AssertUtils.isFalse(CollectionUtils.isEmpty(sysUserList), "未找到用户信息");
        CommonUser user = BeanUtils.convertTo(sysUserList.get(0), CommonUser::new);
        //选择渠道重新发送
        MessageSenderStrategy strategy = holder.route(messageLog.getPushChannel());
        strategy.assembleEvent(event, Collections.singletonList(user));
        if(strategy.verifyEvent(event)){
            //参数校验成功才发送,否则不发
            event.setChannel(messageLog.getPushChannel());
            event.setMessageLogIdList(Collections.singletonList(id));
            asyncEventBus.post(event);
        }
        return Boolean.TRUE;
    }

    /**
     * 保存消息日志
     *
     * @param event 消息事件信息
     * @param list  用户集合
     * @return 保存结果
     */
    public boolean saveLog(MessageEvent event, List<MessageUserInfoModel> list) {
        List<MessageLog> logs = list.stream().map(receiver -> {
            MessageLog log = BeanUtils.convertTo(event, MessageLog::new);
            log.setPushChannel(event.getChannel());
            log.setPushUid(receiver.getId());
            log.setPushRemark("系统错误");
            log.setPushTime(new Date());
            log.setPushUname(StringUtils.isEmpty(receiver.getRoleName()) ? receiver.getNickName() : receiver.getRoleName() + "(" + receiver.getNickName() + ")");
            return log;
        }).collect(Collectors.toList());
        if (messageLogService.saveBatch(logs)) {
            event.setMessageLogIdList(logs.stream().map(MessageLog::getId).collect(Collectors.toList()));
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 查找模板
     *
     * @param code 模板编码
     * @return 模板信息
     */
    private MessageTemplateModel findTemplate(String code,Long tenantId) {
        MessageTemplateListParam listParam = new MessageTemplateListParam();
        listParam.setCode(code);
        listParam.setTenantId(tenantId);
        List<MessageTemplateModel> list = templateService.findTemplates(listParam);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取推送人集合
     *
     * @param userSet  系统默认推送人集合
     * @param userList 模板配置推送人集合
     * @return 推送人集合
     */
    private List<MessageUserInfoModel> getReceivers(String code, Set<String> userSet, List<MessageUserInfoModel> userList) {
        List<MessageUserInfoModel> allUserList = new ArrayList<>();
        Set<String> allUserSet = new HashSet<>();
        if (userSet != null && !userSet.isEmpty()) {
            //系统默认推送人
            UserInfoListParam userParam = new UserInfoListParam();
            userParam.setIds(new ArrayList<>(userSet));
            List<MessageUserInfoModel> sysUserList = templateService.findUserList(userParam);
            sysUserList.forEach(model -> {
                if (!allUserSet.contains(model.getId())) {
                    allUserSet.add(model.getId());
                    allUserList.add(model);
                }
            });
        }
        if (!CollectionUtils.isEmpty(userList)) {
            //模板配置推送人
            userList.forEach(model -> {
                if (!allUserSet.contains(model.getId())) {
                    allUserSet.add(model.getId());
                    allUserList.add(model);
                }
            });
        }
        if (CollectionUtils.isEmpty(allUserList)) {
            log.error("消息模板推送人为空,模板编码:{},系统默认推送人:{},模板配置推送人:\n{}", code, userSet, userList);
        }
        return allUserList;
    }

    /**
     * 模板信息前置检查
     *
     * @return 检查结果
     */
    private boolean preVerifyTemplate(MessageTemplateModel model, String code) {
        if (model == null) {
            log.error("消息模板为空,模板编码:{}", code);
            return Boolean.FALSE;
        }
        if (model.getStatus() == Status.disabled.getKey()) {
            log.error("消息模板已禁用,模板编码:{},模板信息:\n{}", code, model);
            return Boolean.FALSE;
        }
        if (StringUtils.isEmpty(model.getPushChannel())) {
            log.error("消息模板未配置发送渠道,模板编码:{},模板信息:\n{}", code, model);
        }
        return Boolean.TRUE;
    }


    /**
     * 构建消息事件
     *
     * @param template 消息模板
     * @param param    参数
     * @return 消息事件
     */
    private MessageEvent buildMessageEvent(MessageTemplate template, MessageSendParam param, String channel) {
        MessageEvent event = MessageEvent.builder().businessId(param.getBusinessId())
                .tenantId(param.getTenantId()).templateId(template.getId()).channel(channel).type(template.getType())
                .title(template.getTitle()).content(template.getContent()).build();
        //消息内容占位符替换
        if (param.getVariables() != null && !param.getVariables().isEmpty()) {
            event.setContent(new StrSubstitutor(param.getVariables()).replace(event.getContent()));
        }
        return event;
    }
}
