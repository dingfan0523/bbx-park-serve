package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "租车数量趋势")
public class CarRentTrendModel {
    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "租车数量")
    private Integer count;
}
