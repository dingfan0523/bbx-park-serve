
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.groups.Default;
import java.io.Serializable;


@Data
public class DishesEvaluatePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4018805867280263089L;

    @ApiModelProperty(value = "菜品名称.")
    @NotEmpty(groups = Default.class,message = "菜品名称不能为空")
    private String name;
}
