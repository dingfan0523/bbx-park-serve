package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PatrolMaterialPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3590252890870998761L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "巡更id.")
    private Long patrolId;

    @ApiModelProperty(value = "材料id.")
    private Long materialId;

    @ApiModelProperty(value = "材料数量.")
    private Integer materialNum;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

}
