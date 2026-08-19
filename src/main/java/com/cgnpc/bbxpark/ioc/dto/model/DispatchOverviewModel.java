package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "出车分析概览")
public class  DispatchOverviewModel {
    @ApiModelProperty(value = "平均出车次数")
    private Double avgDepartCount;

    @ApiModelProperty(value = "人均驾驶公里数")
    private Double perCapitaMileageKm;
}
