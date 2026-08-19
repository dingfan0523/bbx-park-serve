package com.cgnpc.bbxpark.acl.uic.service;

import com.cgnpc.bbxpark.acl.uic.model.RoleModel;
import com.cgnpc.bbxpark.acl.uic.model.SimpleStaffModel;

import java.util.List;

public interface IRoleApiService {
    /**
     * 获取角色集合
     * @return 角色集合
     */
    List<RoleModel> findRoles();

    /**
     * 根据角色id获取角色信息
     * @param roleId 角色id
     * @return 角色信息
     */
    RoleModel getRoleById(String roleId);
    /**
     * 根据员工号查询用户角色集合
     * @param staffNo 员工号
     * @return 角色集合
     */
    List<RoleModel> getRoleByStaffNo(String staffNo);

    /**
     * 获取当前登录用户的角色集合
     * @return 角色集合
     */
    List<RoleModel> getRoleByCurrent();
    /**
     * 根据角色编码查询用户集合
     * @param roleCode 角色编码
     * @return 用户集合
     */
    List<SimpleStaffModel> findUserByRole(String roleCode);

    List<String> findStaffNoByRoleCode(String roleCode);

    /**
     * 判断用户是否拥有某个角色
     * @param staffNo 用户
     * @param roleCode 角色编码
     * @return true,false
     */
    Boolean hasRole(String staffNo,String roleCode);
}
