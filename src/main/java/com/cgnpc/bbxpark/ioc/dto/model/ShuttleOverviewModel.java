package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "便民班车概览")
public class ShuttleOverviewModel {
    @ApiModelProperty(value = "线路总数")
    private Integer lineTotal;

    @ApiModelProperty(value = "预定次数")
    private Integer count;
}
