
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_meal_line_pos")
public class MealLinePos extends BaseExEntity implements Serializable {

	/**
	*餐线id.
	**/
	private Long mealLineId;
	/**
	*pos号
	**/
	private String pos;
}
