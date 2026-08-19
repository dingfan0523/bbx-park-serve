
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class DishesGalleryModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3757578319613320439L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "菜品名称.")
    private String name;

    @ApiModelProperty(value = "图片url.")
    private String imageUrl;
}
