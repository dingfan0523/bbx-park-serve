package com.cgnpc.dingtalk.util;

import com.cgnpc.dingtalk.constant.UrlConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import java.util.Objects;


/**
 * 用途说明: 获取access_token工具类
 * 作者姓名: P633860
 * 创建时间: 2023/6/25
 */

@Slf4j
@SuppressWarnings("all")
public class AccessTokenUtil {

    public static String getAccessToken(String url, String appKey, String appSecret) {
        DefaultDingTalkClient client = new DefaultDingTalkClient(checkURL(url) + UrlConstant.GET_ACCESS_TOKEN_URL);
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod(HttpMethod.GET.name());
        try {
            OapiGettokenResponse response = client.execute(request);
            if (!Objects.isNull(response)) {
                return response.getAccessToken();
            }
        } catch (ApiException e) {
            log.info("获取钉钉access_token失败{}",e);
        }

        return null;
    }

    public static String checkURL(String url){
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }
}
