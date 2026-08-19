
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class ClickVolumeModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3484957401528748793L;

    @ApiModelProperty(value = "主键标识.")
    private Long id;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "菜单编码.")
    private String menuCode;

    @ApiModelProperty(value = "用户id.")
    private String userId;

    @ApiModelProperty(value = "访问总计.")
    private Integer visitCount;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "更新者.")
    private String updatorId;


}
