
package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @value 区域管理员空间关联列表参数模型
 * @author huangyongtao
 * @date 2025/3/11 11:13
 */
@Data
public class RegionSpaceRelationListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "区域管理id.")
    private Long regionId;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

}
