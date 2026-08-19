
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
public class TenantMemberAddParam implements Serializable {

	@NotNull
	@ApiModelProperty(value = "分组标识.")
	private Long tenantId;

	@NotEmpty
	@ApiModelProperty(value = "用户标识.")
	private List<String> userIds;

	@ApiModelProperty(value = "租户成员")
	private Integer identity;
}
