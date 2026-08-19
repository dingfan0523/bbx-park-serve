package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 巡更路线数据模型实体
 */
@Data
@TableName("bbx_patrol_route")
public class PatrolRoute extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4813906407414851798L;

	/**
	*路线名称.
	**/
	private String name;
	/**
	*路线类型（10：安保路线；20：保洁路线；30：消控路线；40：环境路线；）.
	**/
	private Integer type;
	/**
	*路线等级（10：重要；20：一般）.
	**/
	private Integer level;
	/**
	*是否有序(0->否;1->是).
	**/
	private Integer sequence = 0;
	/**
	*路线距离（km）.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Double distance;
	/**
	*预计用时(分钟).
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Double useTime;
	/**
	*路线描述.
	**/
	private String remark;
	/**
	*巡更点ID;多个以英文逗号隔开，例如：1,2,3.
	**/
	private String pointId;
	/**
	*启用状态;0->否;1->是.
	**/
	private Integer status = 1;
	/**
	*删除状态(0->已删;1->未删).
	**/
	private Integer deleted = 1;
}
