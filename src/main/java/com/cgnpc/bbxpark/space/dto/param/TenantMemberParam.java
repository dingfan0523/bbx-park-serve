
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class TenantMemberParam implements Serializable {

	private static final long serialVersionUID = 4132966885382717114L;

//	@NotNull(groups = UpdateGroup.class)
	@ApiModelProperty(value = "租户成员信息标识.")
	private Long id;

//	@NotNull(groups = InsertGroup.class)
	@ApiModelProperty(value = "状态，0启用1禁用.")
	private Short status;

//	@NotNull(groups = InsertGroup.class)
	@ApiModelProperty(value = "租户标识.")
	private Long tenantId;

//	@NotNull(groups = InsertGroup.class)
	@ApiModelProperty(value = "用户标识.")
	private String userId;

	@ApiModelProperty(value = "身份")
	private Integer identity;
}
