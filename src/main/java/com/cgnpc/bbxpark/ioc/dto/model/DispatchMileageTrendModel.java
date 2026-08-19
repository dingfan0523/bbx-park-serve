package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "总驾驶里程趋势")
public class DispatchMileageTrendModel {
    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "总里程")
    private Double totalMileage;
}
