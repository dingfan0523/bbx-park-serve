
package com.cgnpc.bbxpark.workorder.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 工单计划详细信息数据模型实体
 * @author huangyongtao
 * @date 2025/3/25 16:08
 */
@Data
@TableName("bbx_work_plan_detail")
public class WorkPlanDetail extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*计划id.
	**/
	private Long planId;
    /**
     *计划类型（抄表计划：meterPlan；维保计划：maintainPlan；巡检计划：inspectionPlan；巡更计划：patrolPlan；盘点计划：inventoryPlan；任务计划：taskPlan）.
     **/
    private String planType;
	/**
	*工单id.
	**/
	private Long workId;
	/**
	*计划名称.
	**/
	private String planName;
	/**
	 *审核方式;1->是;0->否.
	 **/
	private Integer auditType;
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
	 *物业分组名称.
	 **/
	private String scheduleName;
	/**
	 *派单方式;10：分组人员抢单；20：组长派单；30：直接指派.
	 **/
	private Integer dispatchType;
	/**
	*空间位置id;多个以英文，逗号隔开.
	**/
	private String spaceId;
	/**
	*空间位置名称.
	**/
	private String spaceName;
	/**
	*抄表类型;water：水表；electricity：电表；gas：燃气表.
	**/
	private String readingType;
	/**
	 *抄表要求
	 **/
	private String readingRemark;
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
	*计划的周期;1:周期抄表；2：单次抄表.
	**/
	private Integer planPeriod;
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
     *计划要求
     **/
    private String remark;
}
