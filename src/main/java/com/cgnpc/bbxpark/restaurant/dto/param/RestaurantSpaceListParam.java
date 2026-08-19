
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class RestaurantSpaceListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4576344276837299836L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    @NotNull(message = "餐厅id不能为空", groups = { UpdateGroup.class})
    private Long restaurantId;

    @ApiModelProperty(value = "空间id.")
    @NotNull(message = "空间id不能为空", groups = { UpdateGroup.class})
    private Long spaceId;

    @ApiModelProperty(value = "空间名称.")
    private String spaceName;

    @ApiModelProperty(value = "人流图片.")
    @NotEmpty(message = "人流图片不能为空", groups = { UpdateGroup.class})
    private String flowImageUrl;

    @ApiModelProperty(value = "餐线图片.")
    @NotEmpty(message = "餐线图片不能为空", groups = { UpdateGroup.class})
    private String mealLineImageUrl;

}
