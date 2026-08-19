
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class RestaurantTimeModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3024345541237993561L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    private Long restaurantId;

    @ApiModelProperty(value = "用餐类型（字典）.")
    private String type;

    @ApiModelProperty(value = "开始时间.")
    private String startTime;

    @ApiModelProperty(value = "结束时间.")
    private String endTime;



}
