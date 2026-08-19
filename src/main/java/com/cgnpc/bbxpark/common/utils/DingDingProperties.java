package com.cgnpc.bbxpark.common.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 钉钉配置信息
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/25 15:04
 */
@Data
@Configuration
public class DingDingProperties {
    /**
     * 应用id
     */
    @Value("${dingDing.appId:123}")
    private String appId;
    /**
     * 钉钉的appKey
     */
    @Value("${dingDing.appKey:123}")
    private String appKey;
    /**
     * 钉钉的appSecret
     */
    @Value("${dingDing.appSecret:F520SeScYvkDAsF3ON9VaQt8wNPGRcBCyx07T5YBG2KAoRaB1eVVjoRppX2QVTD0}")
    private String appSecret;
    /**
     * 企业id
     */
    @Value("${dingDing.corpId:dingec7220f2b2d8ea62acaaa37764f94726}")
    private String corpId;
    /**
     * 应用id
     */
    @Value("${dingDing.agentId:3262841441}")
    private String agentId;
    /**
     * 获取钉钉userId路径
     */
    @Value("${dingDing.userId.url:https://oapi.dingtalk.com/topapi/v2/user/getuserinfo}")
    private String dingDingUserIdUrl;
    /**
     * 获取钉钉accessToken路径
     */
    @Value("${dingDing.accessToken.url:https://oapi.dingtalk.com/gettoken}")
    private String dingDingAccessTokenUrl;

    @Value("${dingDing.jsapiTicket.url:https://oapi.dingtalk.com/get_jsapi_ticket}")
    private String dingDingJsapiTicketUrl;
    /**
     * 钉钉工作通知url
     */
    @Value("${dingDing.send.url:https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2}")
    private String dingDingSendUrl;
    /**
     * 钉钉工作通知进度url
     */
    @Value("${dingDing.sendProcess.url:https://oapi.dingtalk.com/topapi/message/corpconversation/getsendprogress}")
    private String dingDingSendProcessUrl;
    /**
     * 钉钉工作通知回调url
     */
    @Value("${dingDing.sendBack.url:https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult}")
    private String dingDingSendBackUrl;
    /**
     * 钉钉工作通知进度查询重试次数
     */
    @Value("${dingDing.reCount:5}")
    private Integer reCount;
    /**
     * 钉钉工作通知进度查询间隔时间(单位:毫秒)
     */
    @Value("${dingDing.interval:2000}")
    private Integer dingDingInterval;
}
