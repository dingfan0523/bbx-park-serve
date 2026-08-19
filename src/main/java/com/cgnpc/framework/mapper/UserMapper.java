package com.cgnpc.framework.mapper;



import com.cgnpc.framework.domain.SysUser;

//import com.cgnpc.cud.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/******************************
 * 用途说明: CurrentUser 表数据库控制层接口
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
public interface UserMapper extends BaseMapper<SysUser> {

    /**********************************
    * 用途说明: 通过id获取用户user
    * 参数说明 id
    * 返回值说明:
    ***********************************/
    SysUser getUser(long id);

    /**********************************
    * 用途说明: 插入一个用户user
    * 参数说明 user
    * 返回值说明:
    ***********************************/
    void insertUser(SysUser user);
}
