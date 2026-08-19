package com.cgnpc.framework.dto;

import lombok.Data;

/******************************
 * 用途说明:获取用户姓名,用户电话
 * 作者姓名: P627149
 * 创建时间: 2021/9/22 10:19
 ******************************/
@Data
public class UserNamePhone {

    /**
     * 员工名称
     */
    private String userName;

    /**
     * 员工电话
     */
    private String userPhone;

}
