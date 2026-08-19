
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ParkSpaceListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4587033727007666635L;

    @ApiModelProperty(value = "空间名称")
    private String spaceName;

    @ApiModelProperty(value = "空间id集合")
    private List<Long> spaceIdList;

    @ApiModelProperty(value = "父级空间id")
    private Long parentId;
}
