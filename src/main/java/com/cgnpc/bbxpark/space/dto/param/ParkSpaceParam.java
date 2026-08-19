package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@Data
public class ParkSpaceParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3246894107633436251L;

//    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "空间ID.")
    private Long id;

    @ApiModelProperty(value = "园区/租户id")
    private Long tenantId;

//    @NotNull(groups = {InsertGroup.class, UpdateGroup.class})
    @Length(max = 255)
    @ApiModelProperty(value = "空间编码.")
    private String spaceCode;

    @Length(max = 255)
    @ApiModelProperty(value = "空间名称.")
    private String spaceName;

    @Length(max = 255)
    @ApiModelProperty(value = "空间地址.")
    private String spaceAddr;

    @Length(max = 255)
    @ApiModelProperty(value = "空间描述.")
    private String spaceDesc;

//    @NotNull(groups = {InsertGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "所属空间ID.")
    private Long parentSpaceId;

    @ApiModelProperty(value = "所属空间编码.")
    private String parentSpaceCode;

    @ApiModelProperty(value = "排序序号.")
    private Integer orderCode;
}
