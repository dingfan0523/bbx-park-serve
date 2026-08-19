
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
public class TenantInfoListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4542992714234597720L;

//    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "租户标识.")
    private Long id;

//    @NotBlank(groups = InsertGroup.class)
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

    @Length(max = 200)
    @ApiModelProperty(value = "说明.")
    private String description;

    @Length(max = 200)
    @ApiModelProperty(value = "租户简介.")
    private String intro;

//    @NotBlank(groups = InsertGroup.class)
    @Length(max = 50)
    @ApiModelProperty(value = "租户名.")
    private String name;

//    @NotNull(groups = InsertGroup.class)
    @ApiModelProperty(value = "状态，0启用1禁用.")
    private Short status;

//    @NotNull(groups = InsertGroup.class)
    @ApiModelProperty(value = "租户类型，0个人1组织.")
    private Short tenantType;

    @NotNull
    private List<Long> ids;

}
