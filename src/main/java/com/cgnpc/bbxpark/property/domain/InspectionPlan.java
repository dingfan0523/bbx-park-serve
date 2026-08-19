package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 巡检计划管理数据模型实体
 */
@Data
@TableName("bbx_inspection_plan")
public class InspectionPlan extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3342003651684722453L;

	/**
	*计划名称.
	**/
	private String planName;
	/**
	*启用状态;0->否;1->是.
	**/
	private Integer status = 0;
	/**
	*审核方式;0->否;1->是.
	**/
	private Integer auditType = 1;
	/**
	*审核人id.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
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
	*物业分组id.
	**/
	private Long scheduleId;
	/**
	*派单方式;10：分组人员抢单；20：组长派单；30：直接指派.
	**/
	private Integer dispatchType;
	/**
	*处理人id.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String handleUid;
	/**
	*处理人名称.
	**/
	private String handleUname;
	/**
	*处理人工号.
	**/
	private String handleStaffid;
	/**
	*巡检点id;多个以英文逗号隔开.
	**/
	private String pointId;
	/**
	*计划的周期;1:周期；2：单次.
	**/
	private Integer planPeriod = 1;
	/**
	*周期类型;year：年；quarter：季度；month：月；week：周；day：日.
	**/
	private String periodType;
	/**
	*周期的标识;1:第一天；2：最后一天.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Integer periodSign;
	/**
	*周期的开始时间.
	**/
	private Date periodStartTime;
	/**
	*计划开始的时间.
	**/
	private Date planStartTime;
	/**
	*计划时长;单位小时.
	**/
	private Integer planDuration;
	/**
	*计划描述.
	**/
	private String remark;
	/**
	*删除状态(0->已删;1->未删).
	**/
	private Integer deleted = 1;
}
