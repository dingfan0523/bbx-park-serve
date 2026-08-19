package com.cgnpc.bbxpark.workbench.application.impl;

import com.cgnpc.bbxpark.workbench.application.IUserApplication;
import com.cgnpc.framework.domain.SysUser;
import com.cgnpc.framework.service.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/******************************
 * 用途说明: CurrentUser 表数据服务层接口实现
 * 作者姓名: pxmwlin
 * 创建时间: 2019/8/26 9:20
 ******************************/
@Service
public class UserApplicationImpl implements IUserApplication{

    @Autowired
    IUserRepository iUserRepository;

    /**********************************
     * 用途说明: 通过id获取用户user
     * 参数说明 id
     * 返回值说明:
     ***********************************/
    @Override
    public SysUser getUser(long id) {
        return  iUserRepository.getById(id);
    }

    /**********************************
     * 用途说明: 新增一个用户user
     * 参数说明 user
     * 返回值说明:
     ***********************************/
    @Override
    public void insertUser(SysUser user) {
        iUserRepository.insertUser(user);
    }
}