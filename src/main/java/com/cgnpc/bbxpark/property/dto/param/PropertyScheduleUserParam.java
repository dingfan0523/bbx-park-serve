
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 物业排班人员入参数据模型
 */
@Data
public class PropertyScheduleUserParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "人员id.")
    private String userId;
    @ApiModelProperty(value = "人员工号.")
    private String staffid;
    @ApiModelProperty(value = "是否负责人:1->否;0->是")
    private Integer manager;
}
