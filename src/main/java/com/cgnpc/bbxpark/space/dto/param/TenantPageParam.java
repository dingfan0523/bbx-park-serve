
package com.cgnpc.bbxpark.space.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
@Data
@ApiModel(value = "分页传输对象")
public class TenantPageParam extends CudPageDto implements Serializable {

	@Length(max = 20)
	@ApiModelProperty(value = "租户编码，唯一.")
	private String code;

	@Length(max = 200)
	@ApiModelProperty(value = "联系人地址.")
	private String contactAddress;

	@Length(max = 20)
	@ApiModelProperty(value = "联系人手机.")
	private String contactMobile;

	@Length(max = 20)
	@ApiModelProperty(value = "联系人姓名.")
	private String contactName;

	@ApiModelProperty(value = "创建者.")
	private Long createUserId;

	@Length(max = 200)
	@ApiModelProperty(value = "说明.")
	private String description;

	@Length(max = 200)
	@ApiModelProperty(value = "租户简介.")
	private String intro;

	@Length(max = 50)
	@ApiModelProperty(value = "租户名.")
	private String name;

	@ApiModelProperty(value = "状态，0启用1禁用.")
	private Short status;

	@ApiModelProperty(value = "租户类型，0个人1组织.")
	private Short tenantType;

	@ApiModelProperty(value = "修改者.")
	private Long modifyUserId;

    @ApiModelProperty(value = "当前登录人.")
    private Long loginUserId;

    @ApiModelProperty(value = "当前登录人员工号")
    private String loginStaffNo;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;
}
