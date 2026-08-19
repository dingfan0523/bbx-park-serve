package com.cgnpc.bbxpark.workbench.application.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.cgnpc.bbxpark.workbench.application.IPermissonApplication;
import com.cgnpc.bbxpark.workbench.cudauth.utils.UserPermissionUtils;
import com.cgnpc.cud.shiro.util.ContextHolder;
import com.cgnpc.pro.api.ICudUserService;
import com.cgnpc.pro.api.IPermissionService;
import com.cgnpc.pro.model.respvo.Menu;
import com.cgnpc.pro.model.respvo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissonApplicationImpl implements IPermissonApplication {

    @Autowired(required = false)
    private IPermissionService permissionService;

    @Autowired(required = false)
    private ICudUserService cudUserService;

    /**
     * 登录模式
     **/
    @Value("${cud.login-mode:auth2}")
    private String loginMode;

    /**
     * 授权系统应用id
     **/
    @Value("${cud.pro-auth.appId:}")
    private String appId;

    /**
     * 权限缓存key
     */
    public final static String cacheKey = ":userPermission:";

    public static final String PRO_LOGIN = "pro";

    public static final String AEP_LOGIN = "aep";

    /**
     * 角色缓存key
     */
    public final static String roleCacheKey = ":userRoles:";

    /**********************************
     * 用途说明:获取用户的菜单权限
     * 参数说明
     * 返回值说明:
     ***********************************/
    @Override
    public Set<Menu> selectMenus(String s, String... strings) {
        String userId = cudUserService.getUser();
        //确认登录模式需要参数
        String project = PRO_LOGIN.equals(loginMode) ? appId : ContextHolder.getProject();
        Set<Menu> menus = permissionService.selectMenus(userId, project);
        //更新缓存数据
        this.uauCachePutPermissions(userId,null);
        return menus;
    }

    /**
     * 根据用户ID查询角色详细信息
     *
     * @param
     * @return 菜单列表
     */
    @Override
    public List<RoleVO> selectRoleDetail(String s, String... args) {
        String userId = cudUserService.getUser();
        List<RoleVO> roleVOList = new ArrayList<>();
         roleVOList = permissionService.selectRoleDetail(userId);
        if (CollectionUtil.isNotEmpty(roleVOList)) {
            roleVOList = roleVOList.stream().distinct().collect(Collectors.toList());
            //更新缓存数据
            this.uauCachePutRoles(userId,roleVOList.stream().map(RoleVO::getRoleCode).collect(Collectors.toSet()));
        }
        return roleVOList;
    }

    /**********************************
     * 用途说明:获取用户的角色
     * 参数说明
     * 返回值说明:
     ***********************************/
    @Override
    public Set<String> selectRoles(String s, String... strings) {
        String userId = cudUserService.getUser();
        //确认登录模式需要参数
        String project = PRO_LOGIN.equals(loginMode) ? appId : ContextHolder.getProject();
        Set<String> result = permissionService.selectRoles(userId,project);
        //更新缓存数据
        this.uauCachePutPermissions(userId,null);
        return result;
    }

    /**********************************
     * 用途说明:获取用户的其他权限
     * 参数说明
     * 返回值说明:
     ***********************************/
    @Override
    public Set<String> selectPermissions(String s, String... strings) {
        String userId = cudUserService.getUser();
        //确认登录模式需要参数
        String project = PRO_LOGIN.equals(loginMode) ? appId : ContextHolder.getProject();
        Set<String> result = permissionService.selectPermissions(userId, project);
        if (CollUtil.isEmpty(result)) {
            log.error("获取权限数据为空！");
            return Collections.EMPTY_SET;
        }
       //更新缓存数据
        this.uauCachePutPermissions(userId, result);
        return result;
    }


    /**
     * @Author P629041
     * @Description 更新缓存权限数据
     * @Date 16:18 2023/5/24
     * @Param []
     * @return void
     **/
    private void uauCachePutPermissions(String userId,Set<String> permissionsSet){
        if (CollUtil.isEmpty(permissionsSet)) {
            permissionsSet = this.selectPermissions(userId);
        }
        //确认登录模式
        String key = PRO_LOGIN.equals(loginMode) ? PRO_LOGIN : AEP_LOGIN;
        UserPermissionUtils.cachePut(key + cacheKey + userId, permissionsSet);
    }

    /**
     * @Author P629041
     * @Description 更新缓存角色数据
     * @Date 16:18 2023/5/24
     * @Param []
     * @return void
     **/
    private void uauCachePutRoles(String userId,Set<String> roleSet){
        if (CollUtil.isEmpty(roleSet)) {
            log.error("缓存角色数据为空！userId:{},roleSet:{}",userId,roleSet);
            return;
        }
        //确认登录模式
        String key = PRO_LOGIN.equals(loginMode) ? PRO_LOGIN : AEP_LOGIN;
        UserPermissionUtils.cachePut(key + roleCacheKey + userId, roleSet);
    }
}