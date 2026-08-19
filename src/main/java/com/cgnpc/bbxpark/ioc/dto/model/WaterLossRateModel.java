package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "能源流失率分析(用水)")
public class WaterLossRateModel {
    @ApiModelProperty(value = "支路名称")
    private String branchName;

    @ApiModelProperty(value = "用水量")
    private BigDecimal water;

    @ApiModelProperty(value = "占比")
    private BigDecimal ratio;

    public WaterLossRateModel(String branchName,BigDecimal water,BigDecimal ratio){
        this.branchName = branchName;
        this.water = water;
        this.ratio = ratio;
    }
}
