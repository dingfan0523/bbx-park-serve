package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏餐厅废弃物模型
 */
@Data
public class RestaurantWasteStatisticsModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时间")
    private String time;

    @ApiModelProperty(value = "餐余垃圾KG.")
    private Double kitchenWaste = 0d;

    @ApiModelProperty(value = "厨余垃圾KG.")
    private Double foodWaste = 0d;
}

