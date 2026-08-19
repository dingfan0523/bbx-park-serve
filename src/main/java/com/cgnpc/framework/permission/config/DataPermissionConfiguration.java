package com.cgnpc.framework.permission.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Description 租户权限配置类
 * @Author P629041
 * @Date 2024/8/26 11:03
 */
@Data
@Configuration
@ConfigurationProperties("cud.data-permission")
public class DataPermissionConfiguration {

    /**
     * 租户功能开关
     **/
    private boolean enable;

    /**
     * 管理员角色Code
     **/
    private String adminRoleCode;

    /**
     * 需要拦截的查询sql 全类限定名
     **/
    private List<String> selectSqlList;

    /**
     * 需要拦截的修改sql 全类限定名
     **/
    private List<String> updateSqlList;

}
