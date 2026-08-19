package com.cgnpc.bbxpark.workorder.dto.param;



import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 工单评价入参
 * @author dingfan
 */
@Data
public class WorkOrderEvaluateParam {
    @ApiModelProperty(value = "主键.")
    @NotNull(message = "工单id不能为空")
    private Long id;
    @ApiModelProperty(value = "满意度.")
    private Integer satisfaction;
    @Length(max = 255)
    @ApiModelProperty(value = "评价.")
    private String evaluateContent;
}
