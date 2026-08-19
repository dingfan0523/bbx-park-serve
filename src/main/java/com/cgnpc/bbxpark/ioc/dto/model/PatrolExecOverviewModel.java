package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "巡更执行概览")
public class PatrolExecOverviewModel {
    @ApiModelProperty(value = "工单完成率(%)")
    private BigDecimal workOrderFinishRate;

    @ApiModelProperty(value = "巡更异常率(%)")
    private BigDecimal patrolAbnormalRate;

    @ApiModelProperty(value = "预估巡更里程(km)")
    private BigDecimal estMileage;

    @ApiModelProperty(value = "预估巡更时长(h)")
    private BigDecimal estDuration;
}
