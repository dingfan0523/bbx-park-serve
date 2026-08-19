
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value 区域管理员空间关联业务数据模型
 * @author huangyongtao
 * @date 2025/3/11 11:12
 */
@Data
public class RegionSpaceRelationModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "区域管理id.")
    private Long regionId;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

}
