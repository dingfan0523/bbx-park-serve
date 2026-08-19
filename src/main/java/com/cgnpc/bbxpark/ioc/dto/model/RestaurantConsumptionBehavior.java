package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用餐行为分析")
public class RestaurantConsumptionBehavior {

    @ApiModelProperty(value = "日均消费人次")
    private Double dailyConsumerCount = 0d;

    @ApiModelProperty(value = "日均消费金额（单位：元）")
    private Double dailyAmount = 0d;

    @ApiModelProperty(value = "卡内余额均值（单位：元）")
    private Double avgCardBalance = 0d;

    @ApiModelProperty(value = "在用卡数量")
    private Integer activeCardCount = 0;
}
