package com.cgnpc.framework.service;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import cn.com.cgnpc.aep.bizcenter.email.vo.SendEmails;


/******************************
 * 用途说明: 调用邮件中心的服务
 * 作者姓名: pxmwrya
 * 创建时间: 2019/8/26_9:20
 ******************************/
public interface IEmailCenterService {

    /**********************************
    * 用途说明: 发送邮件
    * 参数说明 sendEmails
    * 返回值说明:
    ***********************************/
    ApiResult sendEmail(SendEmails sendEmails) throws Exception;

    /**********************************
    * 用途说明: 封装 SendEmails对象
    * 参数说明 alowUser 自己的账号
    * 参数说明 cc 抄送人
    * 参数说明 content 内容
    * 参数说明 sendStyle
    * 参数说明 sendTo 邮件接收人
    * 参数说明 subject 邮件主题
    * 返回值说明:
    ***********************************/
    SendEmails buildSendEmails(String alowUser, String[] cc, String content, String sendStyle, String[] sendTo, String subject);


}