package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用餐行为分析")
public class RestaurantConsumptionBehaviorModel {

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "总消费金额")
    private Double totalTradeAmount = 0d;

    @ApiModelProperty(value = "总余额")
    private Double totalAccountBalance = 0d;

    @ApiModelProperty(value = "在用卡数量")
    private Integer distinctCardCount = 0;

    @ApiModelProperty(value = "消费次数")
    private Integer totalConsumeCount = 0;
}
