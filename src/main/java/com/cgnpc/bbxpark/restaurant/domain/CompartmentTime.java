
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_compartment_time")
public class CompartmentTime extends BaseExEntity implements Serializable {

	/**
	*包间id.
	**/
	private Long compartmentId;
	/**
	*用餐类型（字典）.
	**/
	private String type;
	/**
	*开始时间.
	**/
	private String startTime;
	/**
	*结束时间.
	**/
	private String endTime;
	/**
	*租户id.
	**/
	private Long tenantId;

}
