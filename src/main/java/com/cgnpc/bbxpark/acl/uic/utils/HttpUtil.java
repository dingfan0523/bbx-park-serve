package com.cgnpc.bbxpark.acl.uic.utils;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.client.RestClient;
import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.vo.CgnRequestHeader;
import com.cgnpc.pro.auth.application.CudAepUtils;
import com.cgnpc.pro.config.aep.properties.CudAepProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class HttpUtil {

    public static String OK = "200";

    @Autowired(required = false)
    private CudAepProperties cudAepProperties;
    @Autowired(required = false)
    private RestClient restClient;

    /**
     * post请求
     * @param realServiceUrl 请求url
     * @param param 请求参数
     * @return 返回
     */
    public Object post(String realServiceUrl, Map<String, Object> param) {
        try{
            CgnRequestHeader header = CudAepUtils.getHeader(realServiceUrl, cudAepProperties);
            StringBuilder url = new StringBuilder(CudAepUtils.getUrl(cudAepProperties.getActive())).append(realServiceUrl);
            if (param != null && !param.isEmpty()) {
                param.forEach((k, v) -> url.append(url.toString().contains("?") ? "&" : "?").append(k).append("=").append(v));
            }
            ApiResult result = restClient.postCgnVoForRest(url.toString(), header, null);
            if (OK.equals(result.getCode())) {
                return result.getData();
            }
            log.error("中台服务调用请求失败,realServiceUrl:{},参数:{},返回信息:\n{}",realServiceUrl,param,result);
        }catch (Exception e){
            log.error("中台服务调用请求异常,realServiceUrl:{},参数:{},错误信息:\n{}",realServiceUrl,param,e);
        }
        return null;
    }

    /**
     * post请求
     * @param realServiceUrl 请求url
     * @param param 请求参数
     * @return 返回
     */
    public Object postBody(String realServiceUrl, Map<String, Object> param) {
        try{
            CgnRequestHeader header = CudAepUtils.getHeader(realServiceUrl, cudAepProperties);
            ApiResult result = restClient.postCgnVoForRest(CudAepUtils.getUrl(cudAepProperties.getActive()) + realServiceUrl, header, param);
            if (OK.equals(result.getCode())) {
                return result.getData();
            }
            log.error("中台服务调用请求失败,realServiceUrl:{},参数:{},返回信息:\n{}",realServiceUrl,param,result);
        }catch (Exception e){
            log.error("中台服务调用请求异常,realServiceUrl:{},参数:{},错误信息:\n{}",realServiceUrl,param,e);
        }
        return null;
    }
}
