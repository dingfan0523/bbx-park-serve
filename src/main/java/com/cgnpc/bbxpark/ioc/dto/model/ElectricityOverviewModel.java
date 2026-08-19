package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "用电宏观成效")
public class ElectricityOverviewModel {
    @ApiModelProperty(value = "每日用电强度")
    private BigDecimal dailyElectricityIntensity;

    @ApiModelProperty(value = "每日用电强度单位")
    private String dailyElectricityIntensityUnit;

    @ApiModelProperty(value = "总用电量")
    private BigDecimal totalElectricity;

    @ApiModelProperty(value = "总用电量单位")
    private String totalElectricityUnit;
}
