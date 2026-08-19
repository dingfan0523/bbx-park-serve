package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "租车用途统计")
public class CarRentTypeStatModel {
    @ApiModelProperty(value = "租车类型")
    private String rentType;

    @ApiModelProperty(value = "次数")
    private Integer count;

    @ApiModelProperty(value = "占比(%)")
    private Double ratio;
}
