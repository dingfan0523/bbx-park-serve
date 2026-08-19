package com.cgnpc.bbxpark;

//import com.cgnpc.cuddemo.config.MybatisPlusConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Indexed;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/******************************
 * 用途说明: springbootr 入口方法
 * 作者姓名: pxmwlin
 * 创建时间: 2019/6/17 11:19
 ******************************/
// 扫描指定包下面的mapper接口
@Indexed
@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.cgnpc.bbxpark",
        "com.cgnpc.bbxpark.workbench",
        "com.cgnpc.ereport",
        "com.cgnpc.cud.form",
        "com.cgnpc.cud.httpclient",
        "com.cgnpc.cud.auth",
        "com.cgnpc.cud.monitor",
        "com.cgnpc.cud.dict",
        "com.cgnpc.cud.wf",
        "com.cgnpc.cud.rpt",
        "com.cgnpc.cud.emailtemplate",
        "com.cgnpc.cud.messagetemplate",
        "com.cgnpc.cud.gen",
        "cn.com.cgnpc.aep.bizcenter.appcenter.sdk",
        "com.cgnpc.cud.core.common.util",
        "com.cgnpc.framework",
        "com.cgnpc.dingtalk",
        "com.cgnpc.mobile",
        "com.cgnpc.pro",
        // "com.cgnpc.qrtz",
        "com.cgnpc.cud.autofill",
        "com.cgnpc.cud.log",
        "com.cgnpc.cud.shiro.subject"
})
public class Application extends SpringBootServletInitializer {

    /***********************************
    * 用途说明: 入口方法
    * 参数说明 args
    ***********************************/
    public static void main(String[] args) throws Exception {
        System.setProperty("org.springframework.boot.logging.LoggingSystem","none");
        SpringApplication.run(Application.class, args);
    }

    /**********************************
    * 用途说明: 程序打war包需要的
    * 参数说明 application
    * 返回值说明:
    ***********************************/
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
