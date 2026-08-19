package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class PatrolMaterialParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4019276325819961512L;

    @ApiModelProperty(value = "巡更id.")
    private Long patrolId;
    @ApiModelProperty(value = "材料id.")
    private Long materialId;
    @ApiModelProperty(value = "材料数量.")
    private Integer materialNum;
}
