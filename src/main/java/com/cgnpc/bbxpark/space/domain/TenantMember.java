package com.cgnpc.bbxpark.space.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/******************************
 * 用途说明:租户成员
 * 作者姓名: P309150
 * 创建时间: 2025/11/17 13:43
 ******************************/
@Data
@TableName("uic_tenant_member")
public class TenantMember extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4395327972770269676L;

	/**
	*状态，0启用1禁用.
	**/
	private Integer status;
	/**
	*租户标识.
	**/
	private Long tenantId;
	/**
	 * 身份
	 */
    @TableField("\"IDENTITY\"")
	private Integer identity;
	/**
	*用户标识.
	**/
	private String userId;
    private String staffName;
	/**
	 *部门标识.
	 **/
    @TableField(exist = false)
	private String departmentId;
	/**
	 *部门标识.
	 **/
    @TableField(exist = false)
	private String departmentName;
}
