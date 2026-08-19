package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "用水趋势分析")
public class WaterStatisticsModel {
    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "水用量")
    private BigDecimal water;
}
