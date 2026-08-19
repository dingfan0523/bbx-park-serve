
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class RestaurantTagParam implements Serializable {

    @ApiModelProperty(value = "标签label.")
    private String label;

    @ApiModelProperty(value = "标签value.")
    private String value;

}
