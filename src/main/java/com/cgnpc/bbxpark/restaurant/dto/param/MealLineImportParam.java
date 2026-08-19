
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class MealLineImportParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3118547448372376972L;

    @ApiModelProperty(value = "餐厅id.")
    private Long restaurantId;

    @ApiModelProperty(value = "餐厅名称.")
    private String restaurantName;

    @ApiModelProperty(value = "餐线id.")
    private Long mealLineId;

    @ApiModelProperty(value = "餐线名称.")
    private String mealLineName;

    @ApiModelProperty(value = "菜品集合.")
    private List<DishesScheduleParam> dishesScheduleParams;

}
