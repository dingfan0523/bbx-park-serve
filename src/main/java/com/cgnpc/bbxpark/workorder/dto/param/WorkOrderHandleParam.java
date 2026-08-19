package com.cgnpc.bbxpark.workorder.dto.param;



import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 工单处理入参
 * @author 54766
 */
@Data
public class WorkOrderHandleParam implements Serializable {
    @ApiModelProperty(value = "工单id.")
    @NotNull(message = "工单id不能为空")
    private Long id;
    @ApiModelProperty(value = "处理图片集合.")
    private List<String> processedPictureUrlList;
    @ApiModelProperty(value = "处理描述(备注).")
    private String processedDesc;
    @ApiModelProperty(value = "处理结果.")
    private Integer handleResult;
    @ApiModelProperty(value = "处理方式.")
    private String processMode;
}
