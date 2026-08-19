package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;

/**
 * 包间套餐入参数据模型
 * @author dingfan
 * @date 2024/8/7 9:47
 */
@Data
public class AppComboListParam implements Serializable {
    @ApiModelProperty(value = "包间id.")
    @NotNull(groups = Default.class,message = "包间id不能为空")
    private Long compartmentId;
}
