
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class MealLineListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3118547448372376972L;

    @ApiModelProperty(value = "餐厅id.")
    private Long restaurantId;

    @ApiModelProperty(value = "餐线名称.")
    private String name;

    @ApiModelProperty(value = "餐线类型(字典).")
    private String type;

    @ApiModelProperty(value = "状态(1->启用;0->禁用).")
    private Integer status;
}
