package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "便民班车热门站点")
public class ShuttleHotStationModel {
    @ApiModelProperty(value = "站点")
    private String station;

    @ApiModelProperty(value = "预约次数")
    private Integer count;
}
