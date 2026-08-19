package com.cgnpc.bbxpark.workorder.dto.param;



import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 工单指派入参
 * @author 54766
 */
@Data
public class WorkOrderCloseParam implements Serializable {
    @ApiModelProperty(value = "工单id.")
    @NotNull(message = "工单id不能为空")
    private Long id;
    @ApiModelProperty(value = "关闭原因.")
    private String closeReason;
}
