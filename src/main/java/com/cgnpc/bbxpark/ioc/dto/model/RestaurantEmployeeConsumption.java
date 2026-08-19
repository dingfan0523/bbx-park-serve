package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "员工消费信息")
public class RestaurantEmployeeConsumption {

    @ApiModelProperty(value = "员工名称（脱敏，如张*云）")
    private String staffName;

    @ApiModelProperty(value = "日均刷卡次数")
    private Double dailySwipeCount;

    @ApiModelProperty(value = "日均消费金额（单位：元）")
    private Double dailyAmount;

    @ApiModelProperty(value = "关怀项（如：日均消费金额低、卡余额不足）")
    private String careItem;
}