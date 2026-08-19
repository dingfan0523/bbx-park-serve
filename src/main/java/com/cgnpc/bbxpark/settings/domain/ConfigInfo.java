package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@TableName("sys_config_info")
@Data
@EqualsAndHashCode(callSuper = false)
public class ConfigInfo extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4619577897363435630L;
	/**
	*编码.
	**/
	private String code;
	/**
	*描述.
	**/
	private String description;
	/**
	*文本.
	**/
	private String label;
	/**
	*名称.
	**/
	private String name;
	/**
	*状态，1正常0禁用.
	**/
	private Integer status;
	/**
	*类型，0系统配置1自定义配置.
	**/
	private Integer type;
	/**
	*值.
	**/
	private String value;
}
