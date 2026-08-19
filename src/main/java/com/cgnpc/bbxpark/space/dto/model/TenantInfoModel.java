package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/******************************
 * 用途说明:租户信息
 * 作者姓名: P309150
 * 创建时间: 2025/11/17 13:43
 ******************************/
@Data
public class TenantInfoModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4462447612118410080L;

    @NotNull
    @ApiModelProperty(value = "租户标识")
    private Long id;

    @NotBlank
    @Length(max = 20)
    @ApiModelProperty(value = "租户编码，唯一")
    private String code;

    @Length(max = 200)
    @ApiModelProperty(value = "联系人地址")
    private String contactAddress;

    @Length(max = 20)
    @ApiModelProperty(value = "联系人手机")
    private String contactMobile;

    @NotBlank
    @Length(max = 20)
    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @Length(max = 200)
    @ApiModelProperty(value = "说明")
    private String description;

    @Length(max = 200)
    @ApiModelProperty(value = "租户简介")
    private String intro;

    @NotBlank
    @Length(max = 50)
    @ApiModelProperty(value = "租户名")
    private String name;

    @ApiModelProperty(value = "状态，0启用1禁用")
    private Integer status;

    @ApiModelProperty(value = "租户类型，0个人1组织")
    private Short tenantType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "创建者")
    private String createBy;
    @ApiModelProperty(value = "修改时间")
    private Date updateDate;
    @ApiModelProperty(value = "修改者")
    private String updatorId;

}
