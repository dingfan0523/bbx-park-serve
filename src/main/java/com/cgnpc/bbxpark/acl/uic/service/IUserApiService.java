package com.cgnpc.bbxpark.acl.uic.service;

import com.cgnpc.bbxpark.acl.uic.model.StaffModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;

import java.util.List;

public interface IUserApiService {
    /**
     * 获取当前登录用户员工号
     * @return 员工号
     */
    String getCurrentStaffNo();

    /**
     * 获取当前登录用户名
     * @return 用户名
     */
    String getCurrentStaffName();

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    UserInfoModel getCurrentUserInfo();

    UserInfoModel detail(String stallNo);

    /**
     * 根据员工号查询用户信息
     * @param staffNo 员工号
     * @return 用户信息
     */
    UserInfoModel getByStaffNo(String staffNo);
    /**
     * 根据员工号集合获取用户信息集合
     * @param staffNos 员工号集合
     * @return 员工信息集合
     */
    List<UserInfoModel> getByStaffNos(List<String> staffNos);

    /**
     * 根据部门编码查询用户列表
     * @param orgId 部门编码
     * @param pageIndex 页码
     * @param pageSize 每页条数
     */
    List<UserInfoModel> getStaffsByOrgId(String orgId,Integer pageIndex,Integer pageSize,String keyword);

    /**
     * 根据部门编码查询中台原有的用户列表
     * @param orgId 部门编码
     * @param pageIndex 页码
     * @param pageSize 每页条数
     */
    List<StaffModel> getOriginalStaffsByOrgId(String orgId, Integer pageIndex, Integer pageSize, String keyword);

    Boolean parkAdmin();

    /**
     * 根据员工号查询用户信息（二级部门信息）
     * @param staffNo 员工号
     * @return 用户信息
     */
    UserInfoModel getSecondDeptByStaffNo(String staffNo);
}
