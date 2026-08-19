
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_meal_line")
public class MealLine extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3714912634750969198L;
	/**
	*餐厅id.
	**/
	private Long restaurantId;
	/**
	*餐线名称.
	**/
	private String name;
	/**
	*餐线类型(字典).
	**/
	private String type;
	/**
	*状态(1->启用;0->禁用).
	**/
	private Integer status;
    /**
     *设备id.
     **/
    @TableField(strategy = FieldStrategy.IGNORED)
    private Long deviceId;
    /**
     *所属楼层物模型编码
     **/
    private String sslcCode;
}
