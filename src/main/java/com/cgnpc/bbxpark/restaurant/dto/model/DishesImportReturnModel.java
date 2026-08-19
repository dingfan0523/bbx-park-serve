
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class DishesImportReturnModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4880174263765988443L;


    @ApiModelProperty(value = "错误信息.")
    private String errMessage;


}
