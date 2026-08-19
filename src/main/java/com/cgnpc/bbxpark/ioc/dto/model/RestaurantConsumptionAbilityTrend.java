package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "消费能力分析")
public class RestaurantConsumptionAbilityTrend  implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时间(MM-dd)")
    private String time;

    @ApiModelProperty(value = "总消费金额（单位：元）")
    private Double totalAmount;

    @ApiModelProperty(value = "日均消费金额（单位：元）")
    private Double dailyAvgAmount;
}