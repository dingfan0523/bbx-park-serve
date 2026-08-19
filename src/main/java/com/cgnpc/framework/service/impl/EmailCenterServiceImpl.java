package com.cgnpc.framework.service.impl;

import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.client.RestClient;
import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.config.Constants;
import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.vo.CgnRequestHeader;
import cn.com.cgnpc.aep.bizcenter.email.vo.SendEmails;
import com.cgnpc.cud.core.common.util.MessageUtils;
import com.cgnpc.framework.service.IEmailCenterService;
import com.cgnpc.pro.auth.application.CudAepUtils;
import com.cgnpc.pro.config.aep.properties.CudAepProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/******************************
 * 用途说明: 调用邮件中心的服务
 * 作者姓名: pxmwrya
 * 创建时间: 2019/8/26_9:20
 ******************************/
@Service
public class EmailCenterServiceImpl implements IEmailCenterService{

    @Autowired(required = false)
    RestClient restClient;

    @Autowired(required = false)
    CudAepProperties cudAepProperties;

    //访问成功状态编码200
    private static final String CODE = "200";

    /**********************************
    * 用途说明: 发送邮件
    * 参数说明 sendEmail
    * 返回值说明:
    ***********************************/
    @Override
    public ApiResult sendEmail(SendEmails sendEmail) throws Exception {
        String realServiceUrl = Constants.EMAIL_URL + "/sendEmail";
        String url = CudAepUtils.getUrl(cudAepProperties.getActive()) + realServiceUrl;
        //预生产
//        String url = Constants.API_GATEWAY_URL_P;
        CgnRequestHeader header = null;
        ApiResult result = null;
        header = CudAepUtils.getHeader(realServiceUrl,cudAepProperties);
        //调用远程接口
        result = restClient.postCgnVoForRest(url,header,sendEmail);
        if(!CODE.equals(result.getCode())){
            throw new Exception(MessageUtils.message("cud.emailcenter.error",result.getMsg()));
        }else{
            return result;
        }
    }

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
    @Override
    public SendEmails buildSendEmails(String alowUser, String[] cc, String content, String sendStyle, String[] sendTo , String subject) {
        SendEmails email = new SendEmails();
        email.setAlowUser(alowUser);
        email.setCc(cc);
        email.setContent(content);
        email.setSendTo(sendTo);
        email.setSendStyle(sendStyle);
        email.setSubject(subject);

        return email;
    }
}