package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 盘点计划管理数据模型实体
 */
@Data
@TableName("bbx_inventory_plan")
public class InventoryPlan extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4538555851201553014L;

	/**
	*计划名称.
	**/
	private String planName;
	/**
	*启用状态;0->否;1->是.
	**/
	private Integer status = 1;
	/**
	*审核方式;0->否;1->是.
	**/
	private Integer auditType = 0;
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
