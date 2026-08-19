package com.cgnpc.ereport.service.impl;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgnpc.ereport.model.ReportTokenModel;
import com.cgnpc.ereport.properties.EreportProperties;
import com.cgnpc.ereport.service.IEreportConfigService;
import com.cgnpc.pro.api.ICudUserService;
import com.cgnpc.report.chart.common.client.oauth.ReportOauthTokenDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author P629988
 */
@Service
@Slf4j
public class IEreportConfigServiceImpl implements IEreportConfigService {

    public static final String EREPORT = "ereport";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EreportProperties ereportProperties;

    @Autowired
    private ICudUserService cudUserService;

    private static Map<String,ReportTokenModel> reportInfoMap = new HashMap<>();

    @Override
    public ReportTokenModel getAuthToken() {
        String userId = cudUserService.getUser();
         String syscode = ereportProperties.getSyscode();
        String tokenKey = syscode+userId;
        log.info("获取AccessToken异常UserUtils.getUser(){}",userId);
        ReportTokenModel reportTokenModel = null;
        if(StringUtils.isNotEmpty(userId)){
            long nowDate = System.currentTimeMillis();
            reportTokenModel = reportInfoMap.get(tokenKey);
            if(reportTokenModel == null || StringUtils.isEmpty(reportTokenModel.getAccessToken())) {
                reportTokenModel = new ReportTokenModel();
                reportTokenModel.setSyscode(syscode);
                reportTokenModel.setUser(userId);
                return initConfig(reportTokenModel);
            }
            long startDateTime = reportTokenModel.getAuthTime().getTime();
            int diffSeconds = (int)((nowDate - startDateTime) / 1000);
            if(diffSeconds > reportTokenModel.getExpiresIn() * 0.8){
                reportTokenModel.setSyscode(syscode);
                reportTokenModel.setUser(userId);
                return initConfig(reportTokenModel);
            }
        }
        return reportTokenModel;
    }

    @Override
    public String getEreportCode() {
        return ereportProperties.getSyscode();
    }

    /**
     * 用途说明: 获取AccessToken
     * 参数说明:
     * 返回值说明: AuthModel
     */
    private ReportTokenModel initConfig(ReportTokenModel reportTokenModel) {
        try{
            HttpHeaders httpHeaders = this.setHttpHeader();
            String url = ereportProperties.getTokenServiceUrl();
            HttpEntity httpEntity = new HttpEntity(null, httpHeaders);
            ApiResult apiResult = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ApiResult.class, new Object[0]).getBody();
            if(apiResult.getData()!=null){
                ReportOauthTokenDto oauthTokenDto = JSONObject.parseObject(JSON.toJSONString(apiResult.getData()),ReportOauthTokenDto.class);
                reportTokenModel.setAccessToken(oauthTokenDto.getAccessToken());
                reportTokenModel.setEreportUrl(ereportProperties.getEreportUrl());
                reportTokenModel.setAuthTime(new Date());
                reportTokenModel.setExpiresIn(oauthTokenDto.getExpiresIn());
            }else{
                log.info("获取AccessToken异常");
                throw new NullPointerException("获取AccessToken异常");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return reportTokenModel;
    }

    private HttpHeaders setHttpHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("user", cudUserService.getUser());
        httpHeaders.set("syscode", ereportProperties.getSyscode());
        httpHeaders.set("clientSecret", ereportProperties.getClientSecret());
        return httpHeaders;
    }
}