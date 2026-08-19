package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "消费占比项")
public class RestaurantConsumptionShareItem {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型：restaurant-餐厅, mealLine-餐线")
    private String type;

    @ApiModelProperty(value = "消费金额（单位：元）")
    private Double amount;

    @ApiModelProperty(value = "消费占比（百分比，如 73.6）")
    private Double sharePercent;
}
