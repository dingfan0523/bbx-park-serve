
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class DishesEvaluateParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4887691618614153018L;

    @Length(max = 64)
    @ApiModelProperty(value = "菜品名称.")
    @NotEmpty(groups = InsertGroup.class,message = "菜品名称不能为空")
    private String name;

    @ApiModelProperty(value = "满意度.")
    @NotNull(groups = InsertGroup.class,message = "满意度不能为空")
    private Integer satisfaction;

    @Length(max = 64)
    @ApiModelProperty(value = "味道(字典)")
    private String taste;

    @ApiModelProperty(value = "匿名状态(0->未匿名;1->匿名)")
    @Max(value = 1,groups = InsertGroup.class,message = "匿名状态非法")
    @Min(value = 0,groups = InsertGroup.class,message = "匿名状态非法")
    private Integer anonymityStatus;
}
