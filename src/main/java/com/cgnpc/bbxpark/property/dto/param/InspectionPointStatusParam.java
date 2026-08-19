package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/10/17 14:29
 */
@Data
public class InspectionPointStatusParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status;
}
