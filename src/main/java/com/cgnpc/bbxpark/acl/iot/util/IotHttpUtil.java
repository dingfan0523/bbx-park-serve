package com.cgnpc.bbxpark.acl.iot.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/6/26 9:01
 */
@Slf4j
public class IotHttpUtil {
    /**
     * POST请求
     * @param url 请求地址
     * @param data 请求参数
     * @return 请求结果
     */
    public static String post(String url, String data) {
        return post(url,data,null);
    }

    public static String post(String url, String data,String token){
        try (HttpResponse httpResponse = HttpRequest.post(url).header("token",token).body(data).execute()) {
            int status = httpResponse.getStatus();
            if (httpResponse.isOk()) {
                return JSONObject.parseObject(httpResponse.body()).getString("data");
            } else {
                log.error("IOT服务调用请求失败，返回状态码：{}，响应内容：{}", status, httpResponse.body());
            }
        } catch (Exception e) {
            log.error("IOT服务调用请求异常，参数：{}\n错误信息：{}", data, e.getMessage(), e);
        }
        return null;
    }

    /**
     * GET请求
     * @param url 请求地址
     * @return 请求结果
     */
    public static String get(String url,Map<String,Object> paramMap) {
        try (HttpResponse httpResponse = HttpRequest.get(url).form(paramMap).execute()) {
            if (httpResponse.isOk()) {
                return JSONObject.parseObject(httpResponse.body()).getString("data");
            } else {
                log.error("IOT服务GET请求失败，返回状态码：{}，响应内容：{}", httpResponse.getStatus(), httpResponse.body());
            }
        } catch (Exception e) {
            log.error("IOT服务GET请求异常，URL：{}\n错误信息：{}", url, e.getMessage(), e);
        }
        return null;
    }
}
