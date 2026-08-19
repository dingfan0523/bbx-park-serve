
package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 物业分组排班计划数据模型实体
 * @author huangyongtao
 * @date 2025/9/28 11:46
 */
@Data
@TableName("bbx_property_schedule_plan")
public class PropertySchedulePlan extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*物业分组id.
	**/
	private Long scheduleId;
	/**
	*周期类型;month：月；week：周.
	**/
	private String periodType;
	/**
	*周期的标识;多个以英文逗号隔开，例如：1,2,3.
	**/
	private String periodSign;
	/**
	*周期的开始时间.
	**/
	private Date periodStartTime;
	/**
	*周期的结束时间.
	**/
	private Date periodEndTime;
	/**
	*计划开始的时间.
	**/
	private Date planStartTime;
	/**
	*计划结束的时间.
	**/
	private Date planEndTime;

}
