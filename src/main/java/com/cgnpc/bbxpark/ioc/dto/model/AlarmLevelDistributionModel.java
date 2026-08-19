package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "告警等级分布")
public class AlarmLevelDistributionModel {
    @ApiModelProperty(value = "等级")
    private String level;

    @ApiModelProperty(value = "次数")
    private Integer count;

    @ApiModelProperty(value = "占比(%)")
    private Double ratio;
}
