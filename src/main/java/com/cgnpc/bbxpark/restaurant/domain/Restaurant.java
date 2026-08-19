
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName(value = "bbx_restaurant")
public class Restaurant extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3059270012457148469L;

	/**
	*餐厅名称.
	**/
	private String name;
	/**
	*餐厅图片.
	**/
	private String imageUrl;
	/**
	*容纳人数.
	**/
	private Integer capacity;
	/**
	*餐厅标签(jsonArray格式).
	**/
	@ApiModelProperty(value = "餐厅标签(jsonArray格式).")
	private String tags;
	/**
	*餐厅电话.
	**/
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String telphone;
	/**
	*介绍.
	**/
	private String introduce;
	/**
	*通知.
	**/
	private String notification;
	/**
	*状态(1->启用;0->禁用).
	**/
	private Integer status;
}
