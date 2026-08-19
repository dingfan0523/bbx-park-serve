package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "便民班车热门线路")
public class ShuttleHotLineModel {
    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "预约次数")
    private Integer count;
}
