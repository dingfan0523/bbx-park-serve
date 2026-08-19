package com.cgnpc.bbxpark.kouzi.controller;

import com.alibaba.fastjson.JSONObject;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @create zhaoshuo
 * @time 2025/3/28
 * @desc 扣子ai请求类
 */
@RestController
@RequestMapping("/api/kouZiAi")
@Api(tags = "扣子ai请求类")
@Slf4j
public class KouziController {
    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "发送消息")
    @PostMapping(value = "/sendMsg")
    @RequiredToken
    public CudResult<String> sendMsg(@RequestParam String msg){
        try{
            String returnMsg ="";
            log.info("扣子ai发送的消息："+msg);
            if (StringUtils.isEmpty(msg)){
                return CudResult.success(returnMsg);
            }
            // 定义请求URL
            String url = "https://api.coze.cn/v1/workflow/run";

            // 定义请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer pat_rXrsGUrth5ac84mvppshwjHuDZxP02o4VoBWZNHEphhJns6Rl3witHBQqvtqhc0x");

            // 定义请求体
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("query", msg);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("workflow_id", "7484107780526882827");
            requestBody.put("parameters", parameters);
            requestBody.put("app_id", "7483713243849392138");

            // 创建HttpEntity对象
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 发送POST请求
            JSONObject response = restTemplate.postForObject(url, requestEntity, JSONObject.class);
            log.info("扣子ai返回的消息："+response.toJSONString());
            if (response.getInteger("code").equals(0)){
                returnMsg = response.getJSONObject("data").getString("data");
            }else {
                log.error("请求扣子api失败！");
                returnMsg = "这个问题暂时无法回答呢。";
            }
            return CudResult.success(returnMsg);
        }catch (Exception e){
            return CudResult.success("这个问题暂时无法回答呢。");
        }
    }
}
