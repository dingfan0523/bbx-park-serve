
package com.cgnpc.bbxpark.settings.dto.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



@Data
public class ClickVolumeListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4514431155083541877L;

    @ApiModelProperty(value = "主键标识.")
    private Long id;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "菜单编码.")
    private String menuCode;

    @ApiModelProperty(value = "用户id.")
    private String userId;

    @ApiModelProperty(value = "访问总计.")
    private Integer visitCount = 1;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "更新者.")
    private String updatorId;

    @ApiModelProperty(value = "主键标识集合.")
    private List<Long> ids;
}
