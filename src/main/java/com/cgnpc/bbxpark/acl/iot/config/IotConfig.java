package com.cgnpc.bbxpark.acl.iot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/6/26 8:56
 */
@Configuration
@Data
public class IotConfig {
    @Value("${iot.device.login.userName:fengys5}")
    private String username;
    @Value("${iot.device.login.password:123456}")
    private String password;
    @Value("${iot.device.login.tenantId:000000}")
    private String tenantId;
    @Value("${iot.device.ip:http://10.33.15.64:8085}")
    private String iotIp;
    @Value("${iot.openapi.ip:http://10.33.15.64:8081}")
    private String iotOpenIp;
    /**
     * 授权调用地址
     */
    @Value("${iot.device.login.url:/auth/login}")
    private String authServe;
    /**
     * 设备状态查询调用地址
     */
    @Value("${iot.device.stateUrl:/thirdPartyApi/apiThirdList/ids}")
    private String stateServe;
    /**
     * 设备属性信息查询调用地址
     */
    @Value("${iot.openapi.infoListUrl:/thirdPartyApi/apiThirdListByGroupId}")
    private String infoListServe;
    /**
     * 产品物模型查询调用地址
     */
    @Value("${iot.openapi.thingModelUrl:/thirdPartyApi/queryThingModel}")
    private String thingModelServe;
    /**
     * 服务调用接口
     */
    @Value("${iot.openapi.serviceUrl:/thirdPartyApi/invokeThingService}")
    private String serviceServe;

    /**
     * IOT服务调用地址
     */
    @Value("${znElevator.iotUrl2:http://10.15.15.70:31010/thirdPartyApi/queryCurrentMultiDeviceProperty}")
    private String iotPropertiesUrl;
}
