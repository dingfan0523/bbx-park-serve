package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏餐厅废弃物模型
 */
@Data
public class RestaurantWasteTypeModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时间")
    private String time;

    @ApiModelProperty(value = "垃圾类型.")
    private String type;

    @ApiModelProperty(value = "垃圾数量.")
    private Double quantity;
}

