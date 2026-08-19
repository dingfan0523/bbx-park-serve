package com.cgnpc.bbxpark.config.minio.configure;

import com.cgnpc.bbxpark.config.minio.configure.service.FileCenterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * Configuration :表示该类是一个配置类
 * ConditionalOnClass :注解用于判断当前项目是否包含 CephService 类；
 * Bean :注解用于定义需要自动配置的组件。
 */
@Configuration
@ConditionalOnClass(FileCenterService.class)
public class MinioAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "minio",name = "fileCenterService",havingValue = "true")   //读取配置文件中的ceph.file中的cephService值为true
    @ConditionalOnMissingBean(FileCenterService.class)        //保证容器中bean的唯一性
    public FileCenterService fileCenterService() {
        return new FileCenterService();
    }


}
