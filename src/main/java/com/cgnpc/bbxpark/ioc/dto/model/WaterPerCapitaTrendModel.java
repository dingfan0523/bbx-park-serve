package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "人均用水趋势")
public class WaterPerCapitaTrendModel {
    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "人均热水用量")
    private Double personHotWater;

    @ApiModelProperty(value = "人均冷水用量")
    private Double personColdWater;
}
