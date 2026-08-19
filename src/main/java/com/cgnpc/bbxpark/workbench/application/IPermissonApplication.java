package com.cgnpc.bbxpark.workbench.application;


import com.cgnpc.pro.model.respvo.Menu;
import com.cgnpc.pro.model.respvo.RoleVO;

import java.util.List;
import java.util.Set;

public interface IPermissonApplication {
    /**********************************
     * 用途说明: 获取菜单
     * 参数说明 strings
     * 返回值说明:
     ***********************************/
    public Set<Menu> selectMenus(String s, String... strings);

    /**
     * 根据用户ID查询角色详细信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<RoleVO> selectRoleDetail(String userId, String... args);

    /**********************************
     * 用途说明: 获取角色
     * 参数说明 strings
     * 返回值说明:
     ***********************************/
    public Set<String> selectRoles(String s, String... strings);

    /**********************************
     * 用途说明: 获取资源
     * 参数说明 strings
     * 返回值说明:
     ***********************************/
    public Set<String> selectPermissions(String s, String... strings);
}