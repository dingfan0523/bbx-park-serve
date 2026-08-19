
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CompartmentEvaluateParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3427376638507949709L;

    @ApiModelProperty(value = "包间id.")
    @NotNull(groups = InsertGroup.class,message = "包间id不能为空")
    private Long compartmentId;

    @ApiModelProperty(value = "包间预定id.")
    @NotNull(groups = InsertGroup.class,message = "预定id不能为空")
    private Long reserveId;

    @ApiModelProperty(value = "满意度.")
    @NotNull(groups = InsertGroup.class,message = "满意度不能为空")
    private Integer satisfaction;

    @ApiModelProperty(value = "菜品.")
    private String dishes;

    @ApiModelProperty(value = "环境.")
    private String environment;

    @ApiModelProperty(value = "服务.")
    private String service;

    @ApiModelProperty(value = "匿名状态(0->未匿名;1->匿名).")
    private Integer anonymityStatus;
}
