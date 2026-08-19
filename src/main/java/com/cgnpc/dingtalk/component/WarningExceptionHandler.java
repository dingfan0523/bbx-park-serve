package com.cgnpc.dingtalk.component;

import com.alibaba.fastjson.JSONObject;
import com.cgnpc.cud.core.exception.SmsException;
import com.cgnpc.cud.workflow2.base.domian.log.CudLogAuditConfigEntity;
import com.cgnpc.cud.workflow2.base.enums.ModuleEnum;
import com.cgnpc.cud.workflow2.base.enums.OperateTypeEnum;
import com.cgnpc.cud.workflow2.base.log.utils.LogbackUtil;
import com.cgnpc.cud.workflow2.base.model.WfResult;
import com.cgnpc.dingtalk.model.DingTalkWorkNoticeInputVO;
import com.cgnpc.dingtalk.model.WorkNotice;
import com.cgnpc.dingtalk.service.AsyncWarningMsgService;
import com.cgnpc.mobile.config.DtalkProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 用途说明: 文件上传/redis/邮件/短信发送失败抛出异常
 * 作者姓名: P633860
 * 创建时间: 2023/7/6
 */
@ControllerAdvice
@SuppressWarnings("all")
public class WarningExceptionHandler {

    @Autowired
    private AsyncWarningMsgService asyncWarningMsgService;

    @Autowired
    private DtalkProperties dtalkProperties;

    @ResponseBody
    @ExceptionHandler(value = SmsException.class)
    public WfResult<?> smsExceptionHandle(SmsException e) {
        WfResult<?> r = new WfResult<>();
        r.error("消息服务异常" + e.getMessage());
        if (e != null && e.getErrorCode() != null) {
            if (dtalkProperties != null && dtalkProperties.getEnable()) {
                //发送钉钉通知
                DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO = new DingTalkWorkNoticeInputVO();
                dingTalkWorkNoticeInputVO.setWorkNotice(WorkNotice.builder()
                        .title(e.getErrorCode().getResourceCode())
                        .content(e.getErrorCode().getResourceCode() + "\n" + e.getErrorCode().getMessage()).build());
                asyncWarningMsgService.sendWorkNotice(dingTalkWorkNoticeInputVO);
            } else {
                //审计日志
                CudLogAuditConfigEntity logAuditConfigEntity = new CudLogAuditConfigEntity(ModuleEnum.PROCESS_PROCESS_TEMPLATE.getName(), OperateTypeEnum.ADD_TYPE.getName(), "抄送", "", e.getErrorCode().getResourceCode() + e.getErrorCode().getMessage());
                LogbackUtil.getActuatorConfigLogger().info(JSONObject.toJSONString(logAuditConfigEntity));
            }
            r.error(e.getErrorCode().getResourceCode() + e.getErrorCode().getMessage());
        }
        return r;
    }


}

