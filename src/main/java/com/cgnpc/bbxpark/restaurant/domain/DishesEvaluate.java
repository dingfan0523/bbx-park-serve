
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_dishes_evaluate")
public class DishesEvaluate extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4833874215676955963L;
	/**
	*菜品id.
	**/
	private Long dishesId;
	/**
	*菜品名称.
	**/
	private String name;
	/**
	*满意度.
	**/
	private Integer satisfaction;
	/**
	*味道.
	**/
	private String taste;
	/**
	*评价人id.
	**/
	private String appraiserId;
	/**
	*评价人名称.
	**/
	private String appraiserName;
	/**
	*评价人工号.
	**/
	private String appraiserStaffid;
	/**
	*匿名状态(0->未匿名;1->匿名).
	**/
	private Integer anonymityStatus;
	/**
	*删除状态(1->未删;0->已删).
	**/
	private Integer deleted;
}
