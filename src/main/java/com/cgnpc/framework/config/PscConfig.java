package com.cgnpc.framework.config;

import cn.com.cgnpc.aep.bizcenter.bpmcenter.api.vo.RequestConditionVO;
import com.cgn.psc.common.Config;
import com.cgn.psc.entity.common.RequestCondition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/******************************
 * 用途说明: PSC相关配置
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
@Configuration
public class PscConfig {
    /**
     * appcode
     */
    @Value("${cud.psc.appcode:}")
    private String appCode;
    /**
     * appcode
     */
    @Value("${cud.psc.url:}")
    private String url;
    /**
     * appcode
     */
    @Value("${cud.psc.passwd:}")
    private String passwd;
    /**
     * appcode
     */
    @Value("${cud.psc.targetcode:}")
    private String targetcode;
    /**
     * appcode
     */
    @Value("${cud.psc.userid:}")
    private String userid;
    /**
     * appcode
     */
    @Value("${cud.psc.username:}")
    private String username;

    /**
     * environment
     */
    @Value("${cud.psc.environment:}")
    private String environment;


    @Value("${cud.psc.procsetTemplateType:}")
    private String procsetTemplateType;

    /**********************************
    * 用途说明: 在配置类中初始化一个bean
    * 参数说明
    * 返回值说明:
    ***********************************/
    @Bean
    RequestCondition requestCondition() {
        RequestCondition requestCondition = new RequestCondition();
        requestCondition.setAppCode(appCode);
        requestCondition.setImpersonator(userid);
        requestCondition.setImpersonatorChsName(username);
        requestCondition.setRequestUrl(url);
        requestCondition.setTargetAppCode(targetcode);
        requestCondition.setClientHostName("");
        return requestCondition;
    }

    /**********************************
     * 用途说明: 在配置类中初始化一个bean
     * 参数说明
     * 返回值说明:
     ***********************************/
    @Bean
    RequestConditionVO requestConditionVO(){
        RequestConditionVO requestConditionVO = new RequestConditionVO();
        requestConditionVO.setAppCode(appCode);
        requestConditionVO.setClientHostName("127.0.0.1");
        requestConditionVO.setClientIP("127.0.0.1");
        requestConditionVO.setEnvironment(environment);
        requestConditionVO.setImpersonator(username);
        requestConditionVO.setImpersonatorChsName(userid);
        requestConditionVO.setPassWord("");
        requestConditionVO.setProcsetTemplateType(procsetTemplateType);
        requestConditionVO.setRequestUrl(url);
        requestConditionVO.setServiceIp("");
        requestConditionVO.setServicePort("");
        requestConditionVO.setSignature("");
        requestConditionVO.setTargetAppCode(targetcode);
        requestConditionVO.setTimeStamp("");
        return requestConditionVO;
    }

    /**********************************
     * 用途说明: servlet相关的生命周期注解 初始化Config的参数
     * 参数说明
     * 返回值说明:
     ***********************************/
    @PostConstruct
    private void initPsc() {
        Config.setEnvironment(environment);// 环境接口
        Config.setAppCode(appCode);
        Config.setProcsetTemplateType(procsetTemplateType);
        Config.setPscService(url);
    }
}
