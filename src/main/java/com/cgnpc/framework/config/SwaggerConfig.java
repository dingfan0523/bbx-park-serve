package com.cgnpc.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/******************************
 * 用途说明: Swagger配置
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    /**********************************
    * 用途说明: 生成swagger文档相关配置
    * 参数说明
    * 返回值说明:
    ***********************************/
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cgnpc"))//扫描com路径下的api文档
                //.paths(PathSelectors.any())//路径判断
                .build();
    }

    /**********************************
     * 用途说明: 生成swagger文档相关配置
     * 参数说明
     * 返回值说明:
     ***********************************/
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("系统框架在线文档")
                .contact(new Contact("中广核框架项目组", "http://xxx.xxx.xxx.xxx", "xxxx.xx.xx"))
                .version("1.0.0.RELEASE")
                .build();
    }
}