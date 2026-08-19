package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "消费次数统计项")
public class RestaurantConsumptionCountItem {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型：restaurant-餐厅, mealLine-餐线")
    private String type;

    @ApiModelProperty(value = "消费次数")
    private Integer count;

}
