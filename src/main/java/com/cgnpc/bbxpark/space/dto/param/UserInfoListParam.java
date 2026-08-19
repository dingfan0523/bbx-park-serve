
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class UserInfoListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3811193141899864741L;

    @ApiModelProperty(value = "用户标识.")
    private String id;

    @Length(max = 200)
    @ApiModelProperty(value = "详细地址.")
    private String address;

    @Length(max = 20)
    @ApiModelProperty(value = "所在区县/乡镇.")
    private String area;

    @Length(max = 200)
    @ApiModelProperty(value = "用户头像.")
    private String avatar;

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

    @ApiModelProperty(value = "来源，默认SYSTEM")
    private String source;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @Length(max = 200)
    @ApiModelProperty(value = "说明.")
    private String value;

    @Length(max = 50)
    @ApiModelProperty(value = "邮箱地址.")
    private String email;

    @Length(max = 20)
    @ApiModelProperty(value = "身份证号.")
    private String idNumber;

    @ApiModelProperty(value = "身份，0超级管理员1普通管理员.")
    private Short identity;

    @Length(max = 200)
    @ApiModelProperty(value = "个人简介.")
    private String intro;

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

    @Length(max = 200)
    @ApiModelProperty(value = "个性签名.")
    private String signature;

    @Length(max = 20)
    @ApiModelProperty(value = "工号.")
    private String staffid;

    @ApiModelProperty(value = "状态，0正常1禁用2注销.")
    private Short status;

    @Length(max = 20)
    @ApiModelProperty(value = "家庭电话.")
    private String telephone;

    @ApiModelProperty(value = "修改时间.")
    private Date updateTime;

    @ApiModelProperty(value = "修改者.")
    private String updatorId;

    @Length(max = 20)
    @ApiModelProperty(value = "真实姓名.")
    private String userName;

    @ApiModelProperty(value = "登录账号模糊.")
    private String userAccount;

    @ApiModelProperty(value = "登录账号精确.")
    private String exactUserAccount;

    @ApiModelProperty(value = "类型，0管理用户1普通用户.")
    private Short userType;

    @ApiModelProperty(value = "用户标识集合")
    private List<String> ids;

}
