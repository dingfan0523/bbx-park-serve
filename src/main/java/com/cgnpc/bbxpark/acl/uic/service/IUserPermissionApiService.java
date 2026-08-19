package com.cgnpc.bbxpark.acl.uic.service;

import com.cgnpc.bbxpark.acl.uic.model.UserPermissionModel;

import java.util.List;
import java.util.Set;

public interface IUserPermissionApiService {
    /**
     * 获取当前登录用户的权限码集合
     * @return 权限code集合
     */
    Set<String> getPermissionCodeByCurrent();

    /**
     * 根据员工号获取权限集合
     * @param staffNo 员工号
     * @return 权限编码集合
     */
    List<UserPermissionModel> getPermissionByStaffNo(String staffNo);
}
