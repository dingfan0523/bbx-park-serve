package com.cgnpc.qrtz.model;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 超时节点视图
 * </p>
 *
 * @author P636016 XIAOJINHUI
 * @since 2023-09-20
 */
@Data
public class IpmAssAll implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 当前处理人
     */
    private String assigneeUser;

    /**
     * 流程当前状态（0正常：1超时：2:即将超时）
     */
    private String dateFlag;

    /**
     * 流程当前状态（0正常：1超时：2:即将超时）
     */
    private String allDateFlag;

    /**
     * 代理主键
     */
    private String assId;

    /**
     * 流程ID
     */
    private String procId;

    /**
     * 流程实例ID
     */
    private String procInstId;

    /**
     * 环节ID
     */
    private String actId;

    /**
     * 环节名
     */
    private String actName;

    /**
     * 修改时间
     */
    private Date modifyDate;

    private Date newDate;

    private Date overIntoTime;

    private Date expireIntoTime;

    /**
     * 发起人
     */
    private String startUser;

    /**
     * 发起时间
     */
    private Date startTime;

    /**
     * 流程实例标题
     */
    private String procTitle;

    /**
     * 超时时间
     */
    private String overTime;

    /**
     * 即将超时时间
     */
    private String expireTime;

    /**
     * 操作类型id
     */
    private Integer operationTypeId;

    /**
     * 操作类型
     */
    private String operationTypeName;

    /**
     * 超时时间通知方式
     */
    private String overTimeNotifyWay;

    /**
     * 超时时间通知间隔
     */
    private BigDecimal overTimeInterval;

    /**
     * 预警时间通知方式
     */
    private String expireTimeNotifyWay;

    /**
     * 预警时间通知间隔
     */
    private BigDecimal expireTimeInterval;

    /**
     * 是否委托
     */
    private String isRep;

    private Integer overDateFlag1;

    private Integer expireDateFlag1;


}
