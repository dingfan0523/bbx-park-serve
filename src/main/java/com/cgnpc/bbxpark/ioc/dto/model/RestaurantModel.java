package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏餐厅模型
 */
@Data
public class RestaurantModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "餐厅id.")
    private Long id;

    @ApiModelProperty(value = "餐厅名称.")
    private String name;

    @ApiModelProperty(value = "餐线集合")
    private List<RestaurantLineModel> lines;

}

