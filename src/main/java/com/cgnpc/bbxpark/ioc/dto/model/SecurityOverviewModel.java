package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@ApiModel(value = "园区安防概览")
public class SecurityOverviewModel {
    @ApiModelProperty(value = "评分")
    private Long score;

    @ApiModelProperty(value = "告警闭环率(%)")
    private BigDecimal alarmCloseRate;

    @ApiModelProperty(value = "设备在线率(%)")
    private BigDecimal onlineRate;

    @ApiModelProperty(value = "离线设备数量")
    private Long offlineDeviceCount;

    @ApiModelProperty(value = "昨日进园人数")
    private Integer yesterdayEnterCount;

    @ApiModelProperty(value = "园区人数")
    private Integer peopleCount;
}
