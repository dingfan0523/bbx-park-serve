package com.cgnpc.bbxpark.localauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 本地联调账号及首页 mock 数据配置。
 */
@Component
@Profile("local-auth")
@ConfigurationProperties(prefix = "bbx.local-auth")
public class LocalAuthProperties {

    private String userId = "LOCAL001";
    private String userName = "本地用户";
    private String password = "local123";
    private String deptId = "LOCAL-DEPT";
    private String deptName = "本地测试部门";
    private String deptNamePath = "本地组织\\本地测试部门";
    private String frontendUrl = "http://127.0.0.1:8010";
    private String roleCode = "default_admin_auth";
    private String roleName = "本地管理员";
    private String ereportCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptNamePath() {
        return deptNamePath;
    }

    public void setDeptNamePath(String deptNamePath) {
        this.deptNamePath = deptNamePath;
    }

    public String getFrontendUrl() {
        return frontendUrl;
    }

    public void setFrontendUrl(String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getEreportCode() {
        return ereportCode;
    }

    public void setEreportCode(String ereportCode) {
        this.ereportCode = ereportCode;
    }
}
