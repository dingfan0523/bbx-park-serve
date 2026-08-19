
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SpaceManagerModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4783679608318751380L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "关联空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "人员ID.")
    private String userId;

    @ApiModelProperty(value = "人员类型.")
    private Integer type;

    @ApiModelProperty(value = "人员姓名.")
    private String name;

    @ApiModelProperty(value = "工号.")
    private String staffid;

    @ApiModelProperty(value = "手机号.")
    private String phone;

    @ApiModelProperty(value = "备注.")
    private String remark;
}
