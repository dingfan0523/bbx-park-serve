package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用水趋势分析")
public class WaterTrendModel {
    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "冷水用量")
    private Double coldWater;

    @ApiModelProperty(value = "热水用量")
    private Double hotWater;
}
