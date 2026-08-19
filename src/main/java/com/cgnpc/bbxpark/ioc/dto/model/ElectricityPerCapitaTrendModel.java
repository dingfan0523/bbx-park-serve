package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "人均用电趋势")
public class ElectricityPerCapitaTrendModel {
    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "人均用电量")
    private BigDecimal personElectricity;

    @ApiModelProperty(value = "单位")
    private String unit;
}
