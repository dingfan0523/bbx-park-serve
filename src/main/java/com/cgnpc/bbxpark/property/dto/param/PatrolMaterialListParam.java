package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class PatrolMaterialListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3080073217463768943L;

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

    @ApiModelProperty(value = "主键id集合.")
    private List<Long> ids;
}
