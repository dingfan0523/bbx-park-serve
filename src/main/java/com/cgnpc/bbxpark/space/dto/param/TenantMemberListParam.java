
package com.cgnpc.bbxpark.space.dto.param;


import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TenantMemberListParam extends CudPageDto implements Serializable {

	@ApiModelProperty(value = "租户成员信息标识.")
	private Long id;

	@ApiModelProperty(value = "状态，0启用1禁用.")
	private Short status;

	@ApiModelProperty(value = "租户标识.")
	private Long tenantId;

	@ApiModelProperty(value = "修改时间.")
	private Date modifyDate;

	@ApiModelProperty(value = "修改者.")
	private Long modifyUserId;

	@ApiModelProperty(value = "用户标识.")
	private String userId;

	@ApiModelProperty(value = "姓名")
	private String userName;

	@ApiModelProperty(value = "工号.")
	private String staffid;

	@ApiModelProperty(value = "租户成员")
	private Integer identity;

}
