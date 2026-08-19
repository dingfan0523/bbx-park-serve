package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "用电趋势分析")
public class ElectricityTrendModel {
    @ApiModelProperty(value = "时间")
    private String date;

    @ApiModelProperty(value = "本年/本月 该时间用电量")
    private BigDecimal value;

    @ApiModelProperty(value = "去年/上月 该时间用电量")
    private BigDecimal lastValue;

    @ApiModelProperty(value = "去年本月 该时间用电量")
    private BigDecimal lastYearValue;

    @ApiModelProperty(value = "单位")
    private String unit;
   }
