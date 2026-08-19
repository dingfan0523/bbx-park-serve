package com.cgnpc.bbxpark.workorder.dto.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/3/27
 * @desc 工单评价类
 */
@Data
public class WorkOrderEvaluationParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单id.")
    @NotNull(message = "工单id不能为空")
    private Long id;

    @ApiModelProperty(value = "满意度.")
    @NotNull(message = "满意度不能为空")
    private Integer satisfaction;

    @ApiModelProperty(value = "评价.")
    private String evaluateContent;
}
