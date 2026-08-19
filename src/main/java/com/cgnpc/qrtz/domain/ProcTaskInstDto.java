package com.cgnpc.qrtz.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/******************************
 * 用途说明: PSC待办 流程实例
 * 作者姓名: P636016 XIAOJINHUI
 * 创建时间: 2023/11/15 15:04:24
 ******************************/
@Data
public class ProcTaskInstDto implements Serializable {

    private String appDefCode;

    private Date assignTime;

    private String assigneeId;

    private String assigneeName;

    private String id;

    private Boolean isRejectInst;

    private String isShareTask;

    private Date openTime;

    private String priority;

    private String procActEnName;

    private String procActId;

    private String procActInstId;

    private String procActName;

    private String procCategoryId;

    private String procCategoryName;

    private String procDefEnName;

    private String procDefId;

    private String procDefName;

    private String procInstId;

    private String procInstStatus;

    private String procModelId;

    private String procSubject;

    private String sentByApprovalAction;

    private String sentByDeptId;

    private String sentByDeptName;

    private String sentByDeptPath;

    private String entByProcActEnName;

    private String sentByProcActId;

    private String sentByProcActName;

    private String sentByUserId;

    private String sentByUserName;

    private Date startTime;

    private String startUserDeptId;

    private String startUserDeptName;

    private String startUserId;

    private String startUserIdName;

    private String startUserName;

    private String  task;

    private String taskName;

    private String fromUserIdTransfer;

    private String fromUserNameTransfer;

    private String toUserIdTransfer;

    private String toUserNameTransfer;
}
