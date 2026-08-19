package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class InspectionMaterialListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3075079428152341068L;

    @ApiModelProperty(value = "巡检id.")
    private Long inspectionId;
}
