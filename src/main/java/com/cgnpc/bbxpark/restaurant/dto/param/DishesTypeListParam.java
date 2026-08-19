
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;


@Data
public class DishesTypeListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4906646899852549276L;

    @ApiModelProperty(value = "餐厅id.")
    @NotNull(groups = Default.class,message = "餐厅id不能为空")
    private Long restaurantId;
}
