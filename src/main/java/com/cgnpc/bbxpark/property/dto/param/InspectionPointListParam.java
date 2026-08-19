
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InspectionPointListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4451807422546360983L;

    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "编码.")
    private String code;

    @ApiModelProperty(value = "位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status;

    @ApiModelProperty(value = "主键id集合.")
    private List<Long> ids;
}
