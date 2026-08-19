package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@ApiModel(value = "用水宏观成效")
public class WaterOverviewModel {
    @ApiModelProperty(value = "每日用水强度")
    private BigDecimal dailyWaterIntensity;
    @ApiModelProperty(value = "总用水量")
    private BigDecimal totalWater;

}
