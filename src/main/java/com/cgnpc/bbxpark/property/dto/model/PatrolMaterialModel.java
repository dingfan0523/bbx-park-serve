package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class PatrolMaterialModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3712285888376450269L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "巡更id.")
    private Long patrolId;

    @ApiModelProperty(value = "材料id.")
    private Long materialId;

    @ApiModelProperty(value = "材料数量.")
    private Integer materialNum;
}
