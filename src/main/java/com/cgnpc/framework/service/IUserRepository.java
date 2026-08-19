package com.cgnpc.framework.service;


import com.cgnpc.framework.domain.SysUser;

import com.cgnpc.cud.core.service.IBaseService;

/******************************
 * 用途说明: CurrentUser 表数据服务层接口
 * 作者姓名: pxmwlin
 * 创建时间: 2019/8/26 9:20
 ******************************/
public interface IUserRepository extends IBaseService<SysUser> {

    /**********************************
    * 用途说明: 通过id获取用户user
    * 参数说明 id
    * 返回值说明:
    ***********************************/
    SysUser getUser(long id);

    /**********************************
    * 用途说明: 新增一个用户user
    * 参数说明 user
    * 返回值说明:
    ***********************************/
    void insertUser(SysUser user);
}