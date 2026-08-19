
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@TableName("bbx_dishes_gallery")
public class DishesGallery extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4394305725127291459L;
	/**
	*菜品名称.
	**/
	private String name;
	/**
	*图片url.
	**/
	private String imageUrl;
	/**
	*单价.
	**/
	private BigDecimal price;
	/**
	*克重.
	**/
	private Integer weight;
	/**
	*辣度建议(0,1,2,3,4,5).
	**/
	private Integer pungencyDegree;
	/**
	*原料信息.
	**/
	private String information;
	/**
	*满意度.
	**/
	private Double satisfaction;
}
