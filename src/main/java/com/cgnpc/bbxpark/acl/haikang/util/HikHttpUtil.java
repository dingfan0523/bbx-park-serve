package com.cgnpc.bbxpark.acl.haikang.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.cgnpc.bbxpark.acl.haikang.model.HkResult;
import com.cgnpc.bbxpark.acl.haikang.model.PlayPreviewURLsParam;
import com.cgnpc.cud.core.exception.BaseException;
import com.cgnpc.report.auth.exception.BusinessException;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhengme
 * @Date 2023/8/18
 * @Description 海康配置
 * @Version 1.0
 **/
@Slf4j
public class HikHttpUtil {

    private static final String ARTEMIS_PATH = "/artemis";

    private static final String HOST = "10.33.211.21";
    private static final String APPKEY = "25580137";
    private static final String APPSECRET = "0pdnCjwiuGZkuV5YNiGH";
//    private static final String APPKEY = "25292530";
//    private static final String APPSECRET = "9n1Sj105avHm4azvgJEO";
//    private static final Logger log = LogManager.getLogger(HikHttpUtil.class);

    /***
     *
     *
     * @param hkenum  海康api的eunm
     * @param body 海康api的入场
     * @return
     */
    public static HkResult doPostStringArtemis(HaikangApiEnum hkenum, String body) {
        return doPostStringArtemis(hkenum, body, true);
    }

    /***
     *
     *
     * @param hkenum  海康api的eunm
     * @param body 海康api的入场
     * @param isHandException 是否直接返回异常
     * @return
     */
    public static HkResult doPostStringArtemis(HaikangApiEnum hkenum, String body, Boolean isHandException) {
        HkResult result = null;
        try {
            ArtemisConfig config = setArtemisConfig();
            Map<String, String> path = getPath(hkenum.getUri());
            String resultJsonStr = ArtemisHttpUtil.doPostStringArtemis(config, path, body, null, null,"application/json");
            result = JSONUtil.toBean(resultJsonStr, HkResult.class);
        } catch (Exception e) {
            log.error(String.format( "%s调用失败,msg=%s",hkenum.getOperationName(), e.getMessage()));
            throw new BaseException("网络异常，请稍候重试");
        }
        if (!result.isSuccess() && isHandException) {
            log.error(String.format("%s调用失败,code=%s,msg=%s", hkenum.getOperationName(), result.getCode(), result.getMsg()));
            throw new BaseException("网络异常，请稍候重试");
        }
        return result;
    }

    public static HkResult doPostStringArtemisFastJson(String url, String body) throws Exception {
        ArtemisConfig config = setArtemisConfig();
        Map<String, String> path = getPath(url);
        String resultJsonStr = ArtemisHttpUtil.doPostStringArtemis(config, path, body, null, null, "application/json");
        HkResult result = JSONUtil.toBean(resultJsonStr, HkResult.class);
        return result;
    }

    /**
     * 组装海康配置参数
     * @return
     */
    private static ArtemisConfig setArtemisConfig() {
        ArtemisConfig config = new ArtemisConfig();
        config.setHost(HOST);
        config.setAppKey(APPKEY);
        config.setAppSecret(APPSECRET);
        return config;
    }

    /**
     * 组装海康url参数
     * @param url
     * @return
     */
    private static Map<String, String> getPath(String url) {
        url = ARTEMIS_PATH + url;
        Map<String, String> path = new HashMap<>();
        path.put("https://", url);
        return path;
    }

    public static void main(String[] args) throws Exception {
        HkResult result = null;
        HaikangApiEnum hkenum1 = HaikangApiEnum.PLAYLIVEURLS;
        PlayPreviewURLsParam params = new PlayPreviewURLsParam();
        params.setCameraIndexCode("7261bf477fe94333972d3781bd4d8349");
//        params.setCameraIndexCode("c94f1af5dcde4d3db76880b85b6c064c");
        params.setStreamType(1);
        params.setProtocol("ws");
        String body = JSON.toJSONString(params);
        Map<String, String> path1 = getPath(hkenum1.getUri());
        ArtemisConfig config = setArtemisConfig();
        String resultJsonStr1 = ArtemisHttpUtil.doPostStringArtemis(config, path1, body, null, null, "application/json");
        result = JSONUtil.toBean(resultJsonStr1, HkResult.class);
        System.out.println(result);
        HaikangApiEnum hkenum = HaikangApiEnum.CAMERASEARCH;
        try {
//            Map<String, String> path = getPath("/api/resource/v1/cameras");
            Map<String, String> path = getPath("/api/resource/v2/camera/search");
            //Map<String, String> path = getPath(hkenum.getUri());
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("pageSize",1000);
            bodyMap.put("pageNo",1);
//            bodyMap.put("name","十四");
            String resultJsonStr = ArtemisHttpUtil.doPostStringArtemis(config, path, JSON.toJSONString(bodyMap), null, null, "application/json");
            result = JSONUtil.toBean(resultJsonStr, HkResult.class);
            JSONObject data = (JSONObject) result.getData();
            AtomicInteger num = new AtomicInteger(0);
            JSONUtil.parseArray(data.get("list")).stream().map(obj -> (JSONObject) obj) .forEach(item -> {
                //log.info("==>  {},{}  ", item.get("indexCode"),item.get("name"));
                System.out.println(item.get("indexCode")+","+item.get("name"));
                num.getAndIncrement();
            });
            //476
            System.out.println("海康摄像头数量" + num);
            //  log.info(JSON.toJSONString(result.getData(),true));

            // "indexCode":"7261bf477fe94333972d3781bd4d8349",
            // "name":"BBX-西区四层-东南角中区连接处",
        } catch (Exception e) {
            throw new BusinessException(String.format( "%s调用失败,msg=%s",hkenum.getOperationName(), e.getMessage()));
        }
        if (!result.isSuccess()) {
            throw new BusinessException(String.format("%s调用失败,code=%s,msg=%s", hkenum.getOperationName(), result.getCode(), result.getMsg()));
        }

    }
}

