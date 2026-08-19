package com.cgnpc.framework.service.impl;

import com.cgnpc.cud.core.service.BaseServiceImpl;
import com.cgnpc.framework.domain.SysUser;
import com.cgnpc.framework.mapper.UserMapper;

import com.cgnpc.framework.service.IUserRepository;
import org.springframework.stereotype.Service;

/******************************
 * 用途说明: CurrentUser 表数据服务层接口实现类
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
@Service
public class UserRepositoryImpl extends BaseServiceImpl<UserMapper, SysUser> implements IUserRepository {

    /**********************************
    * 用途说明: 通过id获取用户user
    * 参数说明 id
    * 返回值说明:
    ***********************************/
    @Override
    public SysUser getUser(long id) {
        return baseMapper.getUser(id);
    }

    /**********************************
    * 用途说明: 插入一个用户user
    * 参数说明 user
    * 返回值说明:
    ***********************************/
    @Override
   public void insertUser(SysUser user){
         baseMapper.insertUser(user);
    }
}