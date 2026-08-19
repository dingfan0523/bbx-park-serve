
package com.cgnpc.bbxpark.space.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpaceManagerPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4829060645596973874L;

    @ApiModelProperty(value = "关联空间ID.")
    private Long spaceId;

}
