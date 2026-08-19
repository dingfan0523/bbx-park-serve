
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@TableName("bbx_dishes_schedule")
public class DishesSchedule extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4259199664081195699L;
	/**
	*餐厅id.
	**/
	private Long restaurantId;
	/**
	*餐线id.
	**/
	private Long mealLineId;
	/**
	*菜品id.
	**/
	private Long dishesId;
	/**
	*菜品名称.
	**/
	private String name;
	/**
	*菜品图片.
	**/
	private String imageUrl;
	/**
	*类别(字典).
	**/
	private String type;
	/**
	*用餐时间(字典).
	**/
	private String mealTime;
	/**
	*出品日期(date).
	**/
	private Date productionDate;
	/**
	*星期（1,2,3,4,5,6,7->对应周一到周日）.
	**/
	private String week;
	/**
	*单价.
	**/
	private BigDecimal price;
	/**
	*克重.
	**/
	private String weight;
	/**
	*辣度建议(0,1,2,3,4,5).
	**/
	private Integer pungencyDegree;
	/**
	*原料信息.
	**/
	private String information;
	/**
	*状态(1->上架;0->下架).
	**/
	private Integer status;
	/**
	*删除状态(1->未删;0->已删).
	**/
	private Integer deleted;
}
