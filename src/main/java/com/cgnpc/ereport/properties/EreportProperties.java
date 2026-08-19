package com.cgnpc.ereport.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author P629988
 * @description
 * @date 2024/1/10
 */
@Data
@ConfigurationProperties(prefix = "cud.ereport")
@Component
public class EreportProperties {
    /**
     * 租户数据授权key
     */
    private String clientKey;
    /**
     * 系统编码
     */
    private String syscode;
    /**
     * 租户数据授权密钥
     */
    private String clientSecret;
    /**
     * 租户鉴权url
     */
    private String ascUrl;
    /**
     * 报表系统地址
     */
    private String ereportUrl;

    /**
     * 本系统服务地址
     */
    private String servicePath;

    /**
     * 获取token的url
     */
    private String tokenServiceUrl;

    /**
     * 上传文件地址
     */
    @Value("${cud.ereport.filePath:/home/ereport/files}")
    private String filePath;
}
