package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "营收总览")
public class RestaurantRevenueOverview implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总消费人次（单位：万次）")
    private Double totalConsumerCount = 0d;

    @ApiModelProperty(value = "总营收（单位：万元）")
    private Double totalRevenue = 0d;
}
