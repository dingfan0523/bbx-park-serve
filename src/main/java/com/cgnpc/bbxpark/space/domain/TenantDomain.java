
package com.cgnpc.bbxpark.space.domain;

import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TenantDomain implements Serializable {
	@NotNull
	@ApiModelProperty(value = "租户标识.")
	private Long id;

	@NotBlank
	@Length(max = 20)
	@ApiModelProperty(value = "租户编码，唯一.")
	private String code;

	@Length(max = 200)
	@ApiModelProperty(value = "联系人地址.")
	private String contactAddress;

	@Length(max = 20)
	@ApiModelProperty(value = "联系人手机.")
	private String contactMobile;

	@ApiModelProperty(value = "租户管理员列表，传null不修改，否则会替换当前租户管理员")
	private List<Long> adminUserIdList;
    private List<String> staffNos;

//	@ApiModelProperty(value = "管理用户列表")
//	private List<UserInfoModel> adminUserList;
    @ApiModelProperty(value = "管理用户列表")
    private List<UserInfoModel> adminUserList;

	@NotBlank
	@Length(max = 20)
	@ApiModelProperty(value = "联系人姓名.")
	private String contactName;

	@ApiModelProperty(value = "创建时间.")
	private Date createDate;

	@ApiModelProperty(value = "创建者.")
	private String createUserNo;

	@Length(max = 200)
	@ApiModelProperty(value = "说明.")
	private String description;

	@Length(max = 200)
	@ApiModelProperty(value = "租户简介.")
	private String intro;

	@NotBlank
	@Length(max = 50)
	@ApiModelProperty(value = "租户名.")
	private String name;

	@ApiModelProperty(value = "状态，0启用1禁用.")
	private Short status;

	@ApiModelProperty(value = "租户类型，0个人1组织.")
	private Short tenantType;

	@ApiModelProperty(value = "修改时间.")
	private Date modifyUserName;

	@ApiModelProperty(value = "修改者.")
	private String modifyUserNo;
}
