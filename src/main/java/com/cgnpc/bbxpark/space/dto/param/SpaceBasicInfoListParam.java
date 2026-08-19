
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpaceBasicInfoListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4547351014833814390L;

    @ApiModelProperty(value = "用户id.")
    private String userId;

    @ApiModelProperty(value = "空间类型")
    private Integer type;
}
