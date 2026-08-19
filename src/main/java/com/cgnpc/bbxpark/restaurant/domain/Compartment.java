
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_compartment")
public class Compartment extends BaseExEntity implements Serializable {
	/**
	*所属餐厅id.
	**/
	private Long restaurantId;
	/**
	*包间名称.
	**/
	private String name;
	/**
	*包间图片.
	**/
	private String imageUrl;
	/**
	*包间位置id.
	**/
	private Long spaceId;
	/**
	*位置名称(冗余字段).
	**/
	private String spaceName;
	/**
	*容纳人数.
	**/
	private Integer people;
	/**
	*面积.
	**/
	private Double area;
	/**
	*包间介绍.
	**/
	private String introduce;
	/**
	 *满意度.
	 **/
	private Double satisfaction;
	/**
	*状态(1->启用;0->禁用).
	**/
	private Integer status;
}
