
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class InspectionMaterialParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4029172516630652720L;

    @ApiModelProperty(value = "巡检id.")
    private Long inspectionId;
    @ApiModelProperty(value = "材料id.")
    private Long materialId;
    @ApiModelProperty(value = "材料数量.")
    private Integer materialNum;
}
