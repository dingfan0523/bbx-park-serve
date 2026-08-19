package com.cgnpc.bbxpark.space.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;


@Data
public class UserPageParam extends CudPageDto implements Serializable {

	private static final long serialVersionUID = 2524898657775022522L;

	@Length(max = 200)
	@ApiModelProperty(value = "详细地址.")
	private String address;

	@Length(max = 20)
	@ApiModelProperty(value = "所在区县/乡镇.")
	private String area;

	@Length(max = 20)
	@ApiModelProperty(value = "所在城市.")
	private String city;

	@ApiModelProperty(value = "所在城市代码.")
	private String cityCode;

	@ApiModelProperty(value = "所在区县/乡镇代码.")
	private String areaCode;

	@ApiModelProperty(value = "所在省份代码.")
	private String provinceCode;

	@Length(max = 20)
	@ApiModelProperty(value = "所在国家.")
	private String country;

	@ApiModelProperty(value = "创建者.")
	private Long createUserId;

	@Length(max = 200)
	@ApiModelProperty(value = "说明.")
	private String description;

	@Length(max = 50)
	@ApiModelProperty(value = "邮箱地址.")
	private String email;

	@Length(max = 20)
	@ApiModelProperty(value = "身份证号.")
	private String idNumber;

	@ApiModelProperty(value = "身份，0超级管理员1普通管理员.")
	private Short identity;

	@Length(max = 20)
	@ApiModelProperty(value = "移动电话.")
	private String mobile;

	@Length(max = 20)
	@ApiModelProperty(value = "姓名拼音.")
	private String namePinyin;

	@Length(max = 20)
	@ApiModelProperty(value = "用户昵称.")
	private String nickName;

	@Length(max = 20)
	@ApiModelProperty(value = "邮政编码.")
	private String postcode;

	@Length(max = 20)
	@ApiModelProperty(value = "所在省份.")
	private String province;

	@ApiModelProperty(value = "性别，0未知1男2女3保密.")
	private Short sex;

	@Length(max = 20)
	@ApiModelProperty(value = "工号.")
	private String staffid;

	@ApiModelProperty(value = "状态，0正常1禁用2注销.")
	private Short status;

	@Length(max = 20)
	@ApiModelProperty(value = "家庭电话.")
	private String telephone;

	@ApiModelProperty(value = "修改者.")
	private Long modifyUserId;

	@Length(max = 20)
	@ApiModelProperty(value = "真实姓名.")
	private String userName;

	@ApiModelProperty(value = "登录账号.")
	private String userAccount;

	@ApiModelProperty(value = "类型，0管理用户1普通用户.")
	private Short userType;

	@ApiModelProperty(value = "仅显示注销人员")
	private Boolean onlyNormal;

	@ApiModelProperty(value = "部门id")
	private String departmentId;

	@ApiModelProperty(value = "园区id")
	private Long tenantId;

	private List<String> userIds;
	private List<String> noUserIds;

	@ApiModelProperty(value = "角色id")
	private Long roleId;

	@ApiModelProperty(value = "消息id")
	private Long messageId;
}
