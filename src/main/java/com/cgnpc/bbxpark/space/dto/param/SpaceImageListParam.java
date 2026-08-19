
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpaceImageListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4787373105443427354L;

    @ApiModelProperty(value = "关联空间ID.")
    private Long spaceId;
}
