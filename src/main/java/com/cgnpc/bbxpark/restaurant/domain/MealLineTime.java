
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_meal_line_time")
public class MealLineTime extends BaseExEntity implements Serializable {

	/**
	*餐线id.
	**/
	private Long mealLineId;
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
}
