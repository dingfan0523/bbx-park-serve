package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "智能化设备总览")
public class IntelligentDeviceOverview {

    @ApiModelProperty(value = "设备总数")
    private Integer total;

    @ApiModelProperty(value = "正常设备数")
    private Integer normalCount;

    @ApiModelProperty(value = "正常设备比率(%)")
    private Double normalRate;

    @ApiModelProperty(value = "告警设备数")
    private Integer alarmCount;

    @ApiModelProperty(value = "告警设备比率(%)")
    private Double alarmRate;
}
