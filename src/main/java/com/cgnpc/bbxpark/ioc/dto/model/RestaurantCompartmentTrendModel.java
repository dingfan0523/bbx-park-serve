package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏餐厅包间预定折线图
 */
@Data
public class RestaurantCompartmentTrendModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "包间预定总数")
    private Integer totalNum;

    @ApiModelProperty(value = "包间预定趋势集合")
    private List<RestaurantCompartmentReserveModel> compartmentTrends;
}

