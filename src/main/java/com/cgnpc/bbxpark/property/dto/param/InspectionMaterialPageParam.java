
package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class InspectionMaterialPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3426827702017505619L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "巡检id.")
    private Long inspectionId;

    @ApiModelProperty(value = "材料id.")
    private Long materialId;

    @ApiModelProperty(value = "材料数量.")
    private Integer materialNum;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

}
