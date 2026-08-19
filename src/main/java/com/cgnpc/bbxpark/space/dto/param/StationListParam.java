
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class StationListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4842498411367338605L;

    @ApiModelProperty(value = "关联空间ID.")
    private Long spaceId;
}
