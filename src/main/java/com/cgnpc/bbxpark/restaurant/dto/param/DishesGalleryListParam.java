
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class DishesGalleryListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3622141118063699639L;

    @ApiModelProperty(value = "菜品名称.")
    private String name;
}
