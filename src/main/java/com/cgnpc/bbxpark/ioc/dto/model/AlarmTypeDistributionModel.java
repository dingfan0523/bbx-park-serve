package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "告警类型分布")
public class AlarmTypeDistributionModel {
    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "次数")
    private Integer count;

    @ApiModelProperty(value = "占比(%)")
    private Double ratio;
}
