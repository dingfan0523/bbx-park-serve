
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;



@Data
public class DishesScheduleImortReturnModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3311741091516297681L;

    @ApiModelProperty(value = "餐厅名称.")
    private String restaurantName;

    @ApiModelProperty(value = "餐线名称.")
    private String name;

    @ApiModelProperty(value = "错误信息.")
    private String errMessage;

    @ApiModelProperty(value = "菜品错误信息集合.")
    private List<DishesImportReturnModel> dishesImportReturnModels;



}
