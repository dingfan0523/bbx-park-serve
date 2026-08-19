package com.cgnpc.qrtz.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 超时通知日志
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cud_log_timeout_notify")
public class LogTimeoutNotify implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "NOTIFICATION_ID")
    private String notificationId;

    /**
     * 流程实例ID
     */
    @TableField("PROC_INST_ID")
    private String procInstId;

    /**
     * 优先级
     */
    @TableField("PRIORITY")
    private String priority;

    /**
     * 流程ID
     */
    @TableField("PROC_ID")
    private String procId;

    /**
     * 流程名
     */
    @TableField("PROC_NAME")
    private String procName;

    /**
     * 流程版本
     */
    @TableField("PROC_VERSION")
    private String procVersion;

    /**
     * 环节ID
     */
    @TableField("ACT_ID")
    private String actId;

    /**
     * 前置规则ID
     */
    @TableField("ACT_PRE_CONFIG_ID")
    private String actPreConfigId;

    /**
     * 环节名
     */
    @TableField("ACT_NAME")
    private String actName;

    /**
     * 流程实例环节ID
     */
    @TableField("PROC_INS_ACT_ID")
    private String procInsActId;

    /**
     * PSC流程模型ID
     */
    @TableField("PSC_MODEL_ID")
    private String pscModelId;

    /**
     * 流程实例标题
     */
    @TableField("PROC_TITLE")
    private String procTitle;

    /**
     * 表单ID
     */
    @TableField("FORM_ID")
    private String formId;

    /**
     * 表单版本
     */
    @TableField("FORM_VERSION")
    private Integer formVersion;

    /**
     * 数据模型ID
     */
    @TableField("DATA_MODEL_ID")
    private String dataModelId;

    /**
     * 数据是否有效状态标识0删除&1正常
     */
    @TableField("DELETE_FLAG")
    @TableLogic
    private Integer deleteFlag;

    /**
     * 创建人工号
     */
    @TableField("CREATE_USER_NO")
    private String createUserNo;

    /**
     * 创建人姓名
     */
    @TableField("CREATE_USER_NAME")
    private String createUserName;

    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private Date createDate;

    /**
     * 通知时间
     */
    @TableField("NOTIFY_DATE")
    private Date notifyDate;

    /**
     * 通知方式
     */
    @TableField("NOTIFY_WAY")
    private String notifyWay;

    /**
     * 接收人
     */
    @TableField("RECEIVER")
    private String receiver;

    /**
     * 修改人工号
     */
    @TableField("MODIFY_USER_NO")
    private String modifyUserNo;

    /**
     * 修改人工姓名
     */
    @TableField("MODIFY_USER_NAME")
    private String modifyUserName;

    /**
     * 修改时间
     */
    @TableField("MODIFY_DATE")
    private Date modifyDate;

    /**
     * 预警通知方式
     */
    @TableField("EXPIRE_TIME_NOTIFY_WAY")
    private String expireTimeNotifyWay;

    /**
     * 预警时间
     */
    @TableField("EXPIRE_TIME")
    private String expireTime;

    /**
     * 超时通知方式
     */
    @TableField("OVERTIME_NOTIFY_WAY")
    private String overTimeNotifyWay;

    /**
     * 超时时间
     */
    @TableField("OVERTIME")
    private String overTime;

    /**
     * 预警天数
     */
    @TableField("EXPIRE_DAYS")
    private BigDecimal expireDays;

    /**
     * 预警小时
     */
    @TableField("EXPIRE_HOURS")
    private BigDecimal expireHours;

    /**
     * 超时天数
     */
    @TableField("OVERTIME_DAYS")
    private BigDecimal overtimeDays;

    /**
     * 超时小时
     */
    @TableField("OVERTIME_HOURS")
    private BigDecimal overtimeHours;

    /**
     * 预警通知间隔时间
     */
    @TableField("EXPIRE_TIME_INTERVAL")
    private BigDecimal expireTimeInterval;

    /**
     * 超时通知间隔时间
     */
    @TableField("OVERTIME_INTERVAL")
    private BigDecimal overTimeInterval;


    /**
     * 通知状态
     */
    @TableField(value = "STATUS")
    private String status;


}
