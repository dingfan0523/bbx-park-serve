package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "出车次数趋势")
public class DispatchCountTrendModel {
    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "出车次数")
    private Integer count;
}
