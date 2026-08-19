package com.cgnpc.framework.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/******************************
 * 用途说明: 获取 httpServletRequest 和 httpServletResponse 对象
 * 作者姓名: pxmwrya
 * 创建时间: 2019/6/17 11:19
 ******************************/
public class HttpServlet {
    /**********************************
    * 用途说明:httpServletRequest对象
    * 参数说明
    * 返回值说明:
    ***********************************/
    public static HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getRequest();
        return request;
    }

    /**********************************
    * 用途说明:httpServletResponse 对象
    * 参数说明
    * 返回值说明:
    ***********************************/
    public static HttpServletResponse getResponse(){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getResponse();
        return response;
    }
}