package com.cgnpc.qrtz.service.impl;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.client.RestClient;
import com.cgnpc.mobile.config.DtalkProperties;
import com.cgnpc.qrtz.service.IUserDdService;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 钉钉免登 服务实现类
 * </p>
 *
 * @author P627253 喻佩
 * @since 2022-11-01
 */
@Slf4j
@Service
public class UserDdServiceImpl implements IUserDdService {


    @Autowired
    RestClient restClient;

    @Autowired
    DtalkProperties dtalkProperties;


    /**
     * 获取钉钉登录的token
     *
     * @return token
     */
    @Override
    public String getToken() {
        String accessToken = "";
        try {
            DingTalkClient client = new DefaultDingTalkClient(dtalkProperties.getDingTalkUrl() + "/gettoken");
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey(dtalkProperties.getAppKey());
            req.setAppsecret(dtalkProperties.getAppSecret());
            req.setHttpMethod("GET");
            OapiGettokenResponse rsp = client.execute(req);
            System.out.println(rsp.getAccessToken());
            accessToken = rsp.getAccessToken();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    /**
     * 获取钉钉的用户信息
     *
     * @param accessToken token
     * @param code        code
     * @return 用户信息
     */
    @Override
    public String getUserInfo(String accessToken, String code) {
        String userId = "";
        try {
            DingTalkClient userInfo = new DefaultDingTalkClient(dtalkProperties.getDingTalkUrl() + "/user/getuserinfo");
            OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
            request.setCode(code);
            request.setHttpMethod("GET");
            OapiUserGetuserinfoResponse response = userInfo.execute(request, accessToken);
            System.out.println(response.getUserid());
            userId = response.getUserid();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return userId;
    }

    /**
     * 获取用户信息
     *
     * @param accessToken token
     * @param userId      用户ID
     * @return
     */
    @Override
    public String get(String accessToken, String userId) {
        String staffNo = "";
        try {
            DingTalkClient get = new DefaultDingTalkClient(dtalkProperties.getDingTalkUrl() + "/user/get");
            OapiUserGetRequest request = new OapiUserGetRequest();
            request.setUserid(userId);
            request.setHttpMethod("GET");
            OapiUserGetResponse response = get.execute(request, accessToken);
            System.out.println(response.getJobnumber());
            staffNo = response.getJobnumber();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return staffNo;
    }

}
