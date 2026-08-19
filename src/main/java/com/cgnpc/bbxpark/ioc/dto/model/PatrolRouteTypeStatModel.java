package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "巡更路线类型统计")
public class PatrolRouteTypeStatModel {
    @ApiModelProperty(value = "类型")
    private Long type;

    @ApiModelProperty(value = "巡更正常数量")
    private Integer normalCount = 0;

    @ApiModelProperty(value = "巡更异常数量")
    private Integer abnormalCount = 0;
}
