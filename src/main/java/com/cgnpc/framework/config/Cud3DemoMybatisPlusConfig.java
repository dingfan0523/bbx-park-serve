package com.cgnpc.framework.config;


import com.cgnpc.framework.permission.interceptor.PaginationSqlInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/******************************
 * 用途说明: MyBatisPlus相关配置  扫描mapper
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/

@Configuration
@MapperScan(basePackages = {
        "com.cgnpc.bbxpark.*.mapper",
        "com.cgnpc.bbxpark.workbench.*.mapper",
        "com.cgnpc.pro.mapper",
        "com.cgnpc.cud.authr.core.*.*.mapper",
        "com.cgnpc.cud.*.*.mapper",
        "com.cgnpc.cud.*.*.*.mapper",
        "com.cgnpc.cud.*.mapper",
        "com.cgnpc.framework.mapper",
        "com.cgnpc.mobile.mapper",
        "com.cgnpc.qrtz.mapper"})
//这个注解，作用相当于下面的@Bean MapperScannerConfigurer，2者配置1份即可
public class Cud3DemoMybatisPlusConfig {

    /**********************************
    * 用途说明:  开启分页支持
    * 参数说明
    * 返回值说明:
    ***********************************/
    @Bean
    public PaginationSqlInterceptor paginationInterceptor() {
        PaginationSqlInterceptor paginationInterceptor = new PaginationSqlInterceptor();
        // 开启 PageHelper 的支持
        paginationInterceptor.setOverflow(true);
        return paginationInterceptor;
    }
}
