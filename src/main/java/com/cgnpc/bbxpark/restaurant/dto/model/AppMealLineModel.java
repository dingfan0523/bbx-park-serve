
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class AppMealLineModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4880174263764988443L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐线名称.")
    private String name;
}
