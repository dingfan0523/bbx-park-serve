package com.cgnpc.bbxpark.common.utils;

import com.cgnpc.cud.core.exception.BaseException;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 钉钉工具类
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/25 14:56
 */
@Component
@Slf4j
public class DingDingUtil {
    @Autowired
    private DingDingProperties properties;

    /**
     * 获取钉钉access_token
     *
     * @return 钉钉access_token
     */
    public String getAccessToken() {
        String accessToken = "";
        try {
            DingTalkClient client = new DefaultDingTalkClient(properties.getDingDingAccessTokenUrl());
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey(properties.getAppKey());
            req.setAppsecret(properties.getAppSecret());
            OapiGettokenResponse rsp = client.execute(req);
            accessToken = rsp.getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
//            throw GenericException.fail("获取钉钉的access_token失败！");
            throw new BaseException("获取钉钉的access_token失败！");
        }
        return accessToken;
    }

    /**
     * 获取钉钉jsapi_ticket
     *
     * @param accessToken access_token
     * @return 钉钉jsapi_ticket
     */
    public String getJsapiTicket(String accessToken) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(properties.getDingDingJsapiTicketUrl());
            OapiGetJsapiTicketRequest req = new OapiGetJsapiTicketRequest();
            OapiGetJsapiTicketResponse rsp = client.execute(req,accessToken);
            return rsp.getTicket();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("获取钉钉的jsapiTicket失败！");
        }
    }

    /**
     * 获取钉钉用户id
     * @param accessToken access_token
     * @param code code
     * @return 钉钉用户id
     */
    public String getUserId(String accessToken, String code){
        try {
            DingTalkClient client = new DefaultDingTalkClient(properties.getDingDingUserIdUrl());
            OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
            req.setCode(code);
            OapiV2UserGetuserinfoResponse rsp = client.execute(req,accessToken);
            return rsp.getResult().getUserid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("获取钉钉用户信息失败！");
        }
    }
}
