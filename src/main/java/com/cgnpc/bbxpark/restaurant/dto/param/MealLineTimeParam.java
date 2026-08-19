
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MealLineTimeParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐线id.")
    private Long mealLineId;

    @Length(max = 32)
    @ApiModelProperty(value = "用餐类型（字典）.")
    private String type;

    @Length(max = 32)
    @ApiModelProperty(value = "开始时间.")
    private String startTime;

    @Length(max = 32)
    @ApiModelProperty(value = "结束时间.")
    private String endTime;
}
