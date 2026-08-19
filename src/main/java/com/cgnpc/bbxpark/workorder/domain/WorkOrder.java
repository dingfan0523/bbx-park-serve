
package com.cgnpc.bbxpark.workorder.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工单主数据模型实体
 */
@Data
@TableName("bbx_work_order")
public class WorkOrder extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*工单名称.
	**/
	private String name;
	/**
	 *工单编码.
	 **/
	private String code;
	/**
	*工单类型(报修工单:repair 设备告警:deviceAlarm；抄表计划：meterPlan；维保计划：maintainPlan；巡检计划：inspectionPlan；巡更计划：patrolPlan；盘点计划：inventoryPlan；任务计划：taskPlan).
	**/
	private String type;
	/**
	*工单来源（人工上报：person；告警触发：alarm；抄表计划：meterPlan；维保计划：maintainPlan；巡检计划：inspectionPlan；巡更计划：patrolPlan；盘点计划：inventoryPlan；任务计划：taskPlan）.
	**/
	private String source;
	/**
	*工单描述.
	**/
	private String remark;
	/**
	*问题图片.
	**/
	private String problemPictureUrl;
	/**
	*处理人id.
	**/
	private String processedPersonId;
	/**
	 * 处理人工号
	 */
	private String processedPersonStaffid;
	/**
	 *处理人名称.
	 **/
	private String processedPersonName;
	/**
	*处理图片.
	**/
	private String processedPictureUrl;
	/**
	*处理描述.
	**/
	private String processedDesc;
	/**
	*流转状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50）.
	**/
	private Integer status;

	/**
	*退回原因.
	**/
	private String returnReason;
	/**
	*关闭原因.
	**/
	private String closeReason;
	/**
	*满意度.
	**/
	private Integer satisfaction;
	/**
	*评价.
	**/
	private String evaluateContent;
	/**
	*告警时间.
	**/
	private Date alarmTime;
	/**
	*业务id.
	**/
	private Long businessId ;
	/** 空间名称 */
	private String spaceName ;
	/** 空间id（使用逗号分隔） */
	private String spaceId ;
    /** 分配人员id */
    private String allotUid ;
	/** 分配人员名称 */
	private String allotUname ;
	/** 分配人员工号 */
	private String allotUstaffid ;
	/** 处理结果（1已解决，2确认存在异常，需要管理员介入） */
	private Integer handleResult ;
    /** 评价人id */
    private String evaluateUid;
	/** 评价人名称 */
	private String evaluateUname;
	/** 评价时间 */
	private Date evaluateTime;
	/** 评价人工号 */
	private String evaluateUstaffid;
	/** 处理方式 */
	private String processMode;
	/** 工单结束时间 */
	private Date endTime;
	/** 花费时间 */
	private Double expendTime;
	/** 工单接受时间 */
	private Date acceptTime;

	/**
	 * 超时时间
	 **/
	private Date outTime;

	/**
	 * 超时状态（0->是;1->否）
	 **/
	private Integer outStatus;

	/**
	 * 超时原因（1：工单生成为节假日；2：表有故障，等待 报修；3：个人原因（请假））
	 **/
	private Integer outReason;
    /**
     *转派人id.
     **/
    private String transferUid;

	/**
	 *转派人名称.
	 **/
	private String transferUname;
	/**
	 *转派人工号.
	 **/
	private String transferStaffid;
    /**
     *审核人id.
     **/
    private String auditUid;
	/**
	 *审核人名称.
	 **/
	private String auditUname;
	/**
	 *审核人工号.
	 **/
	private String auditStaffid;
	/**
	 *审核结果（0->正常;1->异常）
	 **/
	private Integer auditResult;
	/**
	 *审核备注
	 **/
	private String auditRemark;
	/**
	 *派单方式;10：分组人员抢单；20：组长派单；30：直接指派.
	 **/
	private Integer dispatchType;

    /** 部门id */
    private String departmentId;
    /** 部门名称 */
    private String departmentName;
    /** 响应时间 */
    private Double responseTime;
    /** 异常状态(1:是；0：否) */
    private Integer errorStatus;
}
