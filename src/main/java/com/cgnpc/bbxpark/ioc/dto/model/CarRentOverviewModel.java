package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "租车分析概览")
public class CarRentOverviewModel {
    @ApiModelProperty(value = "租车次数")
    private Integer rentCount;

    @ApiModelProperty(value = "租车天数")
    private Integer rentDays;
}
