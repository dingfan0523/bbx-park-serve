package com.cgnpc.bbxpark.common.utils;

import java.util.UUID;

/******************************
 * 用途说明: 自动生成主键
 * 作者姓名: PXMWRYA
 * 创建时间: 2019-06-17 14:39
 ******************************/
public class IDHelper {
    /**********************************
    * 用途说明:生成uuid 作为主键
    * 参数说明
    * 返回值说明:
    ***********************************/
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}