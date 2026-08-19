
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_compartment_evaluate")
public class CompartmentEvaluate extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3075262205272879767L;
	/**
	*包间id.
	**/
	private Long compartmentId;
	/**
	*包间预定id.
	**/
	private Long reserveId;
	/**
	*满意度.
	**/
	private Integer satisfaction;
	/**
	*菜品.
	**/
	private String dishes;
	/**
	*环境.
	**/
	private String environment;
	/**
	*服务.
	**/
	private String service;
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
