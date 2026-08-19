
package com.cgnpc.bbxpark.space.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class TenantMemberPageParam extends CudPageDto implements Serializable {

	private static final long serialVersionUID = 2334480412677237305L;

	@ApiModelProperty(value = "创建者.")
	private Long createUserId;

	@ApiModelProperty(value = "状态，0启用1禁用.")
	private Short status;

	@ApiModelProperty(value = "租户标识.")
	private Long tenantId;

	@ApiModelProperty(value = "修改者.")
	private Long modifyUserId;

	@ApiModelProperty(value = "用户标识.")
	private String userId;

	@ApiModelProperty(value = "姓名")
	private String userName;

	@ApiModelProperty(value = "租户成员")
	private Integer identity;

	@ApiModelProperty(value = "部门标识.")
	private String departmentId;

	private List<Long> userIds;

}
