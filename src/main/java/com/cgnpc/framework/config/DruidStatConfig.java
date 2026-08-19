package com.cgnpc.framework.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/******************************
 * 用途说明: 配置druid登录功能
 * 作者姓名: P621122
 * 创建时间: 2021/8/19 9:42
 ******************************/
@Configuration
public class DruidStatConfig {

    @Bean
    public ServletRegistrationBean druidServlet() {

        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        registrationBean.addInitParameter("allow","127.0.0.1");
        registrationBean.addInitParameter("deny", "");
        registrationBean.addInitParameter("loginUsername", "root");
        registrationBean.addInitParameter("loginPassword", "sdfgg234871!@#$DF!)");
        registrationBean.addInitParameter("resetEnable", "false");
        return registrationBean;

    }

}
