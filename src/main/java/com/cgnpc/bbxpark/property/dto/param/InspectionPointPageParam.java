
package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class InspectionPointPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4254133951496618531L;

    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "编码.")
    private String code;

    @ApiModelProperty(value = "位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status;
}
