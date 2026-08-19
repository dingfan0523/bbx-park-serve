package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏餐厅包间预定折线图
 */
@Data
public class RestaurantCompartmentReserveModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时间（MM-dd）")
    private String time;

    @ApiModelProperty(value = "预定总数")
    private Integer totalNum;
}

