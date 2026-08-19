package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏供餐状态模型
 */
@Data
public class RestaurantLineStatusModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "餐线名称")
    private String lineName;

    @ApiModelProperty(value = "餐厅名称")
    private String restaurantName;

    @ApiModelProperty(value = "餐线状态：营业中/休息中")
    private String status;

    @ApiModelProperty(value = "排队状态：空闲/正常/繁忙")
    private String queueStatus;

    @ApiModelProperty(value = "菜品数量")
    private Integer dishCount;

}

