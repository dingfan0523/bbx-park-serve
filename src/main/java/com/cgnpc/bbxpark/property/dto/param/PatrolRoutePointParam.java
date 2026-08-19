package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/10/17 17:07
 */
@Data
public class PatrolRoutePointParam {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "巡更点id集合.")
    private List<Long> pointIds;
}
