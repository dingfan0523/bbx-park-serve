package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 巡检点数据模型实体
 */
@Data
@TableName("bbx_inspection_point")
public class InspectionPoint extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3751701934361975463L;

	/**
	*名称.
	**/
	private String name;
	/**
	*编码.
	**/
	private String code;
	/**
	*位置id.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Long spaceId;
	/**
	*设备ID;多个以英文逗号隔开，例如：1,2,3.
	**/
	private String deviceId;
	/**
	*巡检要求.
	**/
	private String remark;
	/**
	*启用状态;0->否;1->是.
	**/
	private Integer status = 1;
	/**
	*删除状态(0->已删;1->未删).
	**/
	private Integer deleted = 1;
}
