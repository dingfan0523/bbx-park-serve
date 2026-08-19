
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_restaurant_space")
public class RestaurantSpace extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3003134198558205809L;

	/**
	*餐厅id.
	**/
	private Long restaurantId;
	/**
	*空间id.
	**/
	private Long spaceId;
	/**
	*空间名称.
	**/
	private String spaceName;
	/**
	*人流图片.
	**/
	private String flowImageUrl;
	/**
	*餐线图片.
	**/
	private String mealLineImageUrl;
}
