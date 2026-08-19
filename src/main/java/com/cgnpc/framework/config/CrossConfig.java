package com.cgnpc.framework.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/******************************
 * 用途说明: 跨域配置
 * 作者姓名: PXMWRYA
 * 创建时间: 2019/11/20 14:39
 ******************************/
//@Configuration
public class CrossConfig implements WebMvcConfigurer {

    /**********************************
    * 用途说明: 添加跨域配置
    * 参数说明 registry
    * 返回值说明:
    ***********************************/
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://cuddemo-d","http://localhost:8010")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)//允许跨域传cookie
                .maxAge(3600);
    }
}
