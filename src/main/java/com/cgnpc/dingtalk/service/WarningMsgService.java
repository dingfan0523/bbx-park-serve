package com.cgnpc.dingtalk.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cgnpc.dingtalk.constant.UrlConstant;
import com.cgnpc.dingtalk.enums.MsgTypeEnum;
import com.cgnpc.dingtalk.exception.InvokeDingTalkException;
import com.cgnpc.dingtalk.model.DingTalkWorkNoticeInputVO;
import com.cgnpc.dingtalk.model.WorkNotice;
import com.cgnpc.dingtalk.util.AccessTokenUtil;
import com.cgnpc.mobile.config.DtalkProperties;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

/**
 * 用途说明: 发送工作通知
 * 作者姓名: P633860
 * 创建时间: 2023/6/25
 */

@Slf4j
@Service
@SuppressWarnings("all")
public class WarningMsgService {

    @Autowired
    private DtalkProperties dtalkProperties;


    /**
     * 发送异步工作通知
     */
    public Long sendWorkNotice(DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO) {

        String dingtalkUrl = AccessTokenUtil.checkURL(dtalkProperties.getDingTalkUrl());
        String accessToken = AccessTokenUtil.getAccessToken(dingtalkUrl, dtalkProperties.getAppKey(), dtalkProperties.getAppSecret());
        DingTalkClient client = new DefaultDingTalkClient(dingtalkUrl + UrlConstant.ASYNC_SEND_MESSAGE);
        OapiMessageCorpconversationAsyncsendV2Request request = buildOapiMessageCorpconversationAsyncsendV2Request(dingTalkWorkNoticeInputVO);
        try {
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, accessToken);
            if (rsp.isSuccess()) {
                Long taskId = rsp.getTaskId();
                log.info("钉钉消息发送成功!{}", JSON.toJSONString(rsp));
                return taskId;
            }
        } catch (ApiException e) {
            e.printStackTrace();
            log.info("钉钉消息发送失败!{}",e);
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }
        return 0L;
    }

    /**
     * 构建发送工作通知请求对象
     */
    private OapiMessageCorpconversationAsyncsendV2Request buildOapiMessageCorpconversationAsyncsendV2Request(DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO) {

        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();

        request.setAgentId(Long.valueOf(dtalkProperties.getAgentId()));
        // userId 需要调用接口获取
        if (CollUtil.isNotEmpty(dingTalkWorkNoticeInputVO.getUserIdList())) {
            request.setUseridList(dingTalkWorkNoticeInputVO.getUserIdList().stream().collect(Collectors.joining(",")));
        } else {
            request.setUseridList(dtalkProperties.getUseridList());
        }
        // 是否发送给企业全部用户 当设置为false时必须指定userid_list或dept_id_list其中一个参数的值
        request.setToAllUser(dingTalkWorkNoticeInputVO.getToAllUser());
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = buildOapiMessageCorpconversationAsyncsendV2RequestMsg(dingTalkWorkNoticeInputVO.getWorkNotice());
        request.setMsg(msg);

        return request;
    }

    /**
     * 构建工作通知消息
     */
    private OapiMessageCorpconversationAsyncsendV2Request.Msg buildOapiMessageCorpconversationAsyncsendV2RequestMsg(WorkNotice workNotice) {

        // 创建消息
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        if (StrUtil.isNotBlank(workNotice.getMsgType())){
            msg.setMsgtype(workNotice.getMsgType());
        } else {
            msg.setMsgtype(MsgTypeEnum.TEXT.getMsgType());
        }
        if (MsgTypeEnum.CARD.getMsgType().equals(workNotice.getMsgType())) {
            msg.setMsgtype("action_card");
            msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
            msg.getActionCard().setTitle(workNotice.getTitle());
            msg.getActionCard().setMarkdown(workNotice.getContent());
            msg.getActionCard().setSingleTitle(workNotice.getContent());
            msg.getActionCard().setSingleUrl(workNotice.getUrl());

        } else if (MsgTypeEnum.IMAGE.getMsgType().equals(workNotice.getMsgType())){
            msg.setImage(new OapiMessageCorpconversationAsyncsendV2Request.Image());
            msg.getImage().setMediaId("@lALPDeC23g17KgDNAQDM_g");

        } else if (MsgTypeEnum.FILE.getMsgType().equals(workNotice.getMsgType())){
            msg.setFile(new OapiMessageCorpconversationAsyncsendV2Request.File());
            msg.getFile().setMediaId("@lALPDeC23g17KgDNAQDM_g");

        } else if (MsgTypeEnum.LINK.getMsgType().equals(workNotice.getMsgType())){
            msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
            msg.getLink().setTitle(workNotice.getTitle());
            msg.getLink().setText(workNotice.getContent());
            msg.getLink().setPicUrl("@lALPDeC23g17KgDNAQDM_g");
            msg.getLink().setMessageUrl(workNotice.getUrl());

        } else if (MsgTypeEnum.MARKDOWN.getMsgType().equals(workNotice.getMsgType())){
            msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
            msg.getMarkdown().setTitle(workNotice.getTitle());
            msg.getMarkdown().setText(workNotice.getContent());

        } else if (MsgTypeEnum.OA.getMsgType().equals(workNotice.getMsgType())){
            msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
            msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
            msg.getOa().getHead().setText(workNotice.getTitle());
            msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
            msg.getOa().getBody().setContent(workNotice.getContent());

        } else {
            msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
            msg.getText().setContent(workNotice.getContent());
        }
        return msg;
    }


}
