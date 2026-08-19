package com.cgnpc.bbxpark.workorder.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 工单转派入参
 * @author 54766
 */
@Data
public class WorkOrderAllotParam implements Serializable {
    @ApiModelProperty(value = "工单id.")
    @NotNull(message = "工单id不能为空")
    private Long id;

    @ApiModelProperty(value = "处理人id.")
    @NotNull(message = "处理人人不能为空")
    private String processedPersonId;

    @ApiModelProperty(value = "处理人名称.")
    @NotNull(message = "处理人人不能为空")
    private String processedPersonName;

    @ApiModelProperty(value = "处理人工号.")
    @NotNull(message = "处理人人不能为空")
    private String processedPersonStaffid;

}
