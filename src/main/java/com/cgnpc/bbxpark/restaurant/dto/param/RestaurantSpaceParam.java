
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class RestaurantSpaceParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3655474605395874395L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    @NotNull( message = "餐厅id不能为空")
    private Long restaurantId;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @Length(max = 128)
    @ApiModelProperty(value = "空间名称.")
    private String spaceName;

    @Length(max = 128)
    @ApiModelProperty(value = "人流图片.")
    private String flowImageUrl;

    @Length(max = 128)
    @ApiModelProperty(value = "餐线图片.")
    private String mealLineImageUrl;

}
