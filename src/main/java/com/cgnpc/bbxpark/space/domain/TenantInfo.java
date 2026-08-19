package com.cgnpc.bbxpark.space.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/******************************
 * 用途说明:租户信息
 * 作者姓名: P309150
 * 创建时间: 2025/11/17 13:43
 ******************************/
@Data
@TableName("uic_tenant_info")
public class TenantInfo implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3166310152159383901L;

	/**
	*租户编码，唯一.
	**/
	private String code;
	/**
	*联系人地址.
	**/
	private String contactAddress;
	/**
	*联系人手机.
	**/
	private String contactMobile;
	/**
	*联系人姓名.
	**/
	private String contactName;
	/**
	*说明.
	**/
	private String description;
	/**
	*租户简介.
	**/
	private String intro;
	/**
	*租户名.
	**/
	private String name;
	/**
	*状态，0启用1禁用.
	**/
	private Integer status;
	/**
	*租户类型，0个人1组织.
	**/
	private Short tenantType;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private Integer deleted = 1;

    @TableField(fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updatorId;

    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
}
