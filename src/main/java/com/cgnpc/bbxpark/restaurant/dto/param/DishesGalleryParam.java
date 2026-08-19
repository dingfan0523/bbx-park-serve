
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DishesGalleryParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3886915784858976600L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @Length(max = 30)
    @NotNull(groups = InsertGroup.class)
    @ApiModelProperty(value = "菜品名称.")
    private String name;

    @NotNull(groups = InsertGroup.class)
    @ApiModelProperty(value = "图片url.")
    private String imageUrl;
}
