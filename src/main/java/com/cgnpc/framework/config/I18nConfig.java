package com.cgnpc.framework.config;


//import com.cgnpc.cud.interceptor.UbaInterceptor;

import com.cgnpc.cud.core.support.MultipleMessageSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/******************************
 * 用途说明: 资源文件配置加载
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    @Value("${spring.messages.basename}")
    private String i18nBasenames;

    /**********************************
    * 用途说明: 生成一个bean
    * 参数说明
    * 返回值说明:
    ***********************************/
    @Bean
    public MultipleMessageSource messageSource(){
        MultipleMessageSource multipleMessageSource=new MultipleMessageSource();
        String[] i18nBasenamesArray = i18nBasenames.split(",");
        multipleMessageSource.setBasenames(i18nBasenamesArray);
        return  multipleMessageSource;
    }

    /**********************************
    * 用途说明: 设置国际化的本地解析
    * 参数说明
    * 返回值说明:
    ***********************************/
    @Bean
    public CookieLocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setCookieMaxAge(604800);
        cookieLocaleResolver.setDefaultLocale(Locale.CHINA);
        cookieLocaleResolver.setCookieName("lang");
        return cookieLocaleResolver;
    }

    /**********************************
    * 用途说明: 申城一个拦截器相关类的bean
    * 参数说明 null
    * 返回值说明:
    ***********************************/
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**********************************
    * 用途说明: 添加类localeChangeInterceptor到拦截器中
    * 参数说明 registry
    * 返回值说明:
    ***********************************/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}

