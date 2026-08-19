package com.cgnpc.bbxpark.message.handler.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cgnpc.bbxpark.common.enums.MessagePushChannelEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.DingDingProperties;
import com.cgnpc.bbxpark.common.utils.DingDingUtil;
import com.cgnpc.bbxpark.message.domain.MessageLog;
import com.cgnpc.bbxpark.message.dto.resp.CommonUser;
import com.cgnpc.bbxpark.message.dto.resp.DingDingWorkNoticeExtModel;
import com.cgnpc.bbxpark.message.handler.BaseHandler;
import com.cgnpc.bbxpark.message.service.IMessageLogService;
import com.cgnpc.bbxpark.config.eventbus.MessageEvent;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendprogressRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendresultRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendprogressResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendresultResponse;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 钉钉工作通知 消息处理策略实现类
 *
 * @author 3y
 */
@Slf4j
@Service
public class DingDingWorkNoticeHandler extends BaseHandler {
    @Resource
    private DingDingProperties properties;
    @Resource
    private DingDingUtil dingUtil;
    @Resource
    private IMessageLogService messageLogService;

    public DingDingWorkNoticeHandler() {
        channelCode = MessagePushChannelEnum.DING.getCode();
    }

    /**
     * 处理事件消息
     *
     * @param event 事件消息
     */
    @Override
    public void handler(MessageEvent event) {
        try {
            //获取钉钉的accessToken
            String accessToken = dingUtil.getAccessToken();
            //组装参数并发送钉钉工作通知
            OapiMessageCorpconversationAsyncsendV2Request request = assembleParam(event);
            OapiMessageCorpconversationAsyncsendV2Response response = new DefaultDingTalkClient(properties.getDingDingSendUrl()).execute(request, accessToken);
            if (response.isSuccess()) {
                //更新发送状态为:成功
                AssertUtils.isFalse(response.getTaskId() == null,"钉钉taskId为空");
                success(event,response,accessToken);
            }else {
                //更新发送失败原因
                fail(event,response,accessToken);
            }
        } catch (Exception e) {
            log.error("钉钉工作通知发送错误,错误:\n{},参数:\n{}", Throwables.getStackTraceAsString(e), event);
        }
    }

    /**
     * 组装钉钉请求参数
     */
    private OapiMessageCorpconversationAsyncsendV2Request assembleParam(MessageEvent messageEvent) {
        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        DingDingWorkNoticeExtModel contentModel = (DingDingWorkNoticeExtModel) messageEvent.getExtModel();
        try {
            // 接收者相关
            req.setAgentId(Long.parseLong(properties.getAgentId()));
            Set<String> receivers = contentModel.getUserSet().stream().map(CommonUser::getThirdUserId).collect(Collectors.toSet());
//            req.setUseridList(StringUtils.join(receivers, StrPool.COMMA));
            //钉钉消息通知-卡片消息
            OapiMessageCorpconversationAsyncsendV2Request.Msg message = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            boolean flag = StringUtils.isNotEmpty(contentModel.getSingleUrl()) && StringUtils.isNotEmpty(contentModel.getSingleTitle());
            if(flag){
                //卡片消息参数
                message.setMsgtype("action_card");
                OapiMessageCorpconversationAsyncsendV2Request.ActionCard actionCard = new OapiMessageCorpconversationAsyncsendV2Request.ActionCard();
                actionCard.setTitle(messageEvent.getTitle());
                actionCard.setMarkdown(messageEvent.getContent());
                actionCard.setSingleTitle(contentModel.getSingleTitle());
                actionCard.setSingleUrl(contentModel.getSingleUrl());
                message.setActionCard(actionCard);
            }else {
                //markdown消息参数
                message.setMsgtype("markdown");
                OapiMessageCorpconversationAsyncsendV2Request.Markdown markdown = new OapiMessageCorpconversationAsyncsendV2Request.Markdown();
                markdown.setTitle(messageEvent.getTitle());
                markdown.setText(messageEvent.getContent());
                message.setMarkdown(markdown);
            }
            req.setMsg(message);
        } catch (Exception e) {
            log.error("钉钉工作通知参数组装失败,错误:\n{},参数:\n{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(messageEvent));
            throw e;
        }
        return req;
    }

    /**
     * 工作通知发送进度查询
     * @param accessToken token
     * @param taskId 任务id
     * @return 是否完成发送
     */
    private boolean sendProcess(String accessToken,Long taskId){
        //查询次数
        int count = 0;
        Long start = System.currentTimeMillis();
        try{
            do{
                count++;
                DingTalkClient client = new DefaultDingTalkClient(properties.getDingDingSendProcessUrl());
                OapiMessageCorpconversationGetsendprogressRequest request  = new OapiMessageCorpconversationGetsendprogressRequest();
                request.setAgentId(Long.parseLong(properties.getAgentId()));
                request.setTaskId(taskId);
                OapiMessageCorpconversationGetsendprogressResponse response = client.execute(request, accessToken);
                log.info("钉钉工作通知进度第{}次查询,accessToken:{},taskId:{},结果:\n{}",count,accessToken,taskId,JSON.toJSONString(response));
                if(response.isSuccess() && response.getProgress().getStatus() == 2){
                    //钉钉工作通知发送进度完成
                    return Boolean.TRUE;
                }
                Thread.sleep(properties.getDingDingInterval());
            }while (count < properties.getReCount());
        }catch (Exception e){
            log.error("钉钉工作通知进度查询错误,accessToken:{},taskId:{},错误:\n{}",accessToken,taskId,Throwables.getStackTraceAsString(e));
//            throw GenericException.fail("钉钉工作通知进度查询错误");
        }
        log.error("钉钉工作通知进度超时未完成,accessToken:{},taskId:{}",accessToken,taskId);
        return Boolean.FALSE;
    }

    /**
     * 工作通知回调查询
     * @param accessToken token
     * @param taskId 任务id
     * @return 返回真实发送成功钉钉用户id集合
     */
    private List<String> sendCallBack(String accessToken, Long taskId){
        if(!sendProcess(accessToken,taskId)){
            return Collections.emptyList();
        }
        List<String> receiverIds = new ArrayList<>();
        try{
            DingTalkClient client = new DefaultDingTalkClient(properties.getDingDingSendBackUrl());
            OapiMessageCorpconversationGetsendresultRequest req = new OapiMessageCorpconversationGetsendresultRequest();
            req.setAgentId(Long.parseLong(properties.getAgentId()));
            req.setTaskId(taskId);
            OapiMessageCorpconversationGetsendresultResponse rsp = client.execute(req, accessToken);
            if(rsp.isSuccess() && rsp.getSendResult() != null){
                if(!CollectionUtils.isEmpty(rsp.getSendResult().getReadUserIdList())){
                    receiverIds.addAll(rsp.getSendResult().getReadUserIdList());
                }
                if(!CollectionUtils.isEmpty(rsp.getSendResult().getUnreadUserIdList())){
                    receiverIds.addAll(rsp.getSendResult().getUnreadUserIdList());
                }
            }
        }catch (Exception e){
            log.error("钉钉工作通知回调查询错误,accessToken:{},taskId:{},错误:\n{}",accessToken,taskId,Throwables.getStackTraceAsString(e));
//            throw GenericException.fail("钉钉工作通知回调查询错误");
        }
        return receiverIds;
    }

    /**
     * 发送成功后续逻辑
     * @param event 参数
     * @param response 钉钉参数
     * @param accessToken token
     */
    private void success(MessageEvent event,OapiMessageCorpconversationAsyncsendV2Response response,String accessToken){
        //预计接收者用户
        DingDingWorkNoticeExtModel contentModel = (DingDingWorkNoticeExtModel) event.getExtModel();
        Set<CommonUser> predictReceivers = contentModel.getUserSet();
        //实际接收者用户
        List<String> realReceiverThirdIds = sendCallBack(accessToken,response.getTaskId());
        Set<String> realReceiverIds = predictReceivers.stream().filter(user->realReceiverThirdIds.contains(user.getThirdUserId())).map(CommonUser::getId).collect(Collectors.toSet());
        if(!CollectionUtils.isEmpty(realReceiverIds)){
            //发送成功更新状态
            LambdaUpdateWrapper<MessageLog> wrapper = new LambdaUpdateWrapper<>();
            wrapper.in(MessageLog::getId, event.getMessageLogIdList()).in(MessageLog::getPushUid,realReceiverIds).eq(event.getTenantId() != null, MessageLog::getTenantId, event.getTenantId());
            wrapper.set(MessageLog::getStatus, Status.enabled.getKey()).set(MessageLog::getPushTime, new Date());
            wrapper.set(MessageLog::getPushRemark,"");
            messageLogService.update(wrapper);
        }
        log.info("钉钉工作通知发送成功,accessToken:{},返回:\n{},参数:\n{},实际成功用户:\n{}", accessToken, JSON.toJSONString(response), event,realReceiverIds);
    }

    /**
     * 发送失败后续逻辑
     * @param event 参数
     * @param response 钉钉参数
     * @param accessToken token
     */
    private void fail(MessageEvent event,OapiMessageCorpconversationAsyncsendV2Response response,String accessToken){
        LambdaUpdateWrapper<MessageLog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(MessageLog::getId, event.getMessageLogIdList()).eq(event.getTenantId() != null, MessageLog::getTenantId, event.getTenantId());
        wrapper.set(MessageLog::getPushRemark,"系统错误");
        messageLogService.update(wrapper);
        log.error("钉钉工作通知发送失败,accessToken:{},返回:\n{},参数:\n{}", accessToken, JSON.toJSONString(response), event);
    }
}

