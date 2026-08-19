
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class RestaurantSpaceModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4432841348724827050L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    private Long restaurantId;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "空间名称.")
    private String spaceName;

    @ApiModelProperty(value = "人流图片.")
    private String flowImageUrl;

    @ApiModelProperty(value = "餐线图片.")
    private String mealLineImageUrl;

    @ApiModelProperty(value = "全路径")
    private String fullPath;

    @ApiModelProperty(value = "id路径")
    private String idFullPath;
}
