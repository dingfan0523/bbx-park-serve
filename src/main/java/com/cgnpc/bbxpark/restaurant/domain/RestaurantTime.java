
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_restaurant_time")
public class RestaurantTime extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4919660011466522581L;

	/**
	*餐厅id.
	**/
	private Long restaurantId;
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
