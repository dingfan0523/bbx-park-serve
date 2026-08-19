package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "能源流失率分析")
public class ElectricityLossRateModel {
    @ApiModelProperty(value = "支路名称")
    private String branchName;

    @ApiModelProperty(value = "用电量")
    private BigDecimal electricity;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "占比")
    private BigDecimal ratio;

    public ElectricityLossRateModel(String branchName,BigDecimal electricity,BigDecimal ratio){
        this.branchName = branchName;
        this.electricity = electricity;
        this.ratio = ratio;
    }
}
