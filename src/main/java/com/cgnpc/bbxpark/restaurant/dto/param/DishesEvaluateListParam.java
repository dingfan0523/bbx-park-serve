
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.groups.Default;
import java.io.Serializable;


@Data
public class DishesEvaluateListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3363691453410079607L;

    @ApiModelProperty(value = "菜品名称.")
    @NotEmpty(groups = Default.class,message = "菜品名称不能为空")
    private String name;
}
