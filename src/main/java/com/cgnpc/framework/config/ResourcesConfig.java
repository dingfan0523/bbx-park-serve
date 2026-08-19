package com.cgnpc.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/******************************
 * 用途说明: 通用配置
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
@Configuration
public  class ResourcesConfig implements WebMvcConfigurer
{
    @Autowired
    CudConfig cudConfig;
    /**
     * 首页地址
     */
    @Value("${cud.shiro.login-success-url:/index}")
    private String indexUrl;

    /**********************************
    * 用途说明: 默认首页的设置，当输入域名是可以自动跳转到默认指定的网页
    * 参数说明 registry
    * 返回值说明:
    ***********************************/
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("forward:" + indexUrl);
        registry.addViewController("/vue").setViewName("redirect:{'/dist/index.html'}");
    }

    /**********************************
    * 用途说明: 对静态资源配置
    * 参数说明 registry
    * 返回值说明:
    ***********************************/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
       // registry.addResourceHandler("/static/**").addResourceLocations("classpath:/dist/static/");
        registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/dist/");
        /** 头像上传路径 */
        registry.addResourceHandler("/profile/**").addResourceLocations("file:" + cudConfig.getProfile());

        /** swagger配置 */
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}