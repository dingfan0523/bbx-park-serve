package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "战略指标总览")
public class StrategicMetricsModel {
    @ApiModelProperty(value = "设备健康度(%)")
    private BigDecimal deviceHealthRate;
    @ApiModelProperty(value = "库存预警率(%)")
    private Double inventoryWarningRate;
    @ApiModelProperty(value = "工单闭环率(%)")
    private Double workOrderClosedRate;
}
