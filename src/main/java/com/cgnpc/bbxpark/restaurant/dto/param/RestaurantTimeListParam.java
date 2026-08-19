
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class RestaurantTimeListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4723974029066039949L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    private Long restaurantId;

    @ApiModelProperty(value = "用餐类型（字典）.")
    private String type;


    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

}
