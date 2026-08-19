package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/******************************
 * 用途说明:租户成员
 * 作者姓名: P309150
 * 创建时间: 2025/11/17 13:43
 ******************************/
@Data
public class TenantMemberModel  implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4917244009410168246L;

    @NotNull
    @ApiModelProperty(value = "租户成员信息标识")
    private Long id;

    @ApiModelProperty(value = "状态，0启用1禁用")
    private Short status;

    @NotNull
    @ApiModelProperty(value = "租户标识")
    private Long tenantId;

    @NotNull
    @ApiModelProperty(value = "用户标识")
    private String userId;

    @ApiModelProperty(value = "身份")
    private Integer identity;

    @ApiModelProperty(value = "部门标识")
    private String departmentId;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;
}
