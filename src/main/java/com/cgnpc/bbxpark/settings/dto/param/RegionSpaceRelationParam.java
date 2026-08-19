
package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @value 区域管理员空间关联入参数据模型
 * @author huangyongtao
 * @date 2025/3/11 11:14
 */
@Data
public class RegionSpaceRelationParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "区域管理id.")
    private Long regionId;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "空间id集合.")
    private List<Long> spaceIds;
}
