
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SpaceImagePageParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4102788512238392820L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "关联空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "图片类型.")
    private String type;

    @ApiModelProperty(value = "图片集合.")
    private String images;

    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "删除状态(1->未删;0->已删).")
    private Integer deleted = 0;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "乐观锁.")
    private Integer revision;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private String updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

}
