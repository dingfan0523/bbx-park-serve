
package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.util.Date;

/***
 * @Description 抄表计划管理数据模型实体
 * @author huangyongtao
 * @date 2025/3/25 16:04
 */
@Data
@TableName("bbx_meter_reading_plan")
public class MeterReadingPlan extends BaseExEntity {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*计划名称.
	**/
	private String planName;
	/**
	*启用状态;1->是;0->否.
	**/
	private Integer status;
	/**
	 *审核方式;1->是;0->否.
	 **/
	private Integer auditType = 0;
    /**
     *审核人名称.
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
	*空间位置id;多个以英文，逗号隔开.
	**/
	private String spaceId;
	/**
	*抄表类型;water：水表；electricity：电表；gas：燃气表.
	**/
	private String readingType;
	/**
	 *抄表要求
	 **/
	private String readingRemark;
    /**
     *处理人名称.
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
	*计划描述.
	**/
	private String remark;
}
