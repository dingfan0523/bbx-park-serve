package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 巡更点数据模型实体
 */
@Data
@TableName("bbx_patrol_point")
public class PatrolPoint extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3098685867407594972L;

	/**
	*名称.
	**/
	private String name;
	/**
	*编码.
	**/
	private String code;
	/**
	*类型（10：安保；20：保洁；30：消控；40：环境；50：设备）.
	**/
	private Integer type;
	/**
	*方式（10：拍照；20：其他）.
	**/
	private Integer way;
	/**
	*位置id.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Long spaceId;
	/**
	*巡更要求.
	**/
	private String remark;
	/**
	*重点检查(0->否;1->是).
	**/
	private Integer keyPoint = 0;
	/**
	*启用状态;0->否;1->是.
	**/
	private Integer status = 1;
	/**
	*删除状态(0->已删;1->未删).
	**/
	private Integer deleted = 1;
}
