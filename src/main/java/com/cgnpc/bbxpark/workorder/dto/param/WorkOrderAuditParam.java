package com.cgnpc.bbxpark.workorder.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/***
 * @Description 工单审核入参
 * @author huangyongtao
 * @date 2025/11/10 13:46
 */
@Data
public class WorkOrderAuditParam implements Serializable {
    @ApiModelProperty(value = "工单id.")
    @NotNull(message = "工单id不能为空")
    private Long id;
    @ApiModelProperty(value = "审核备注.")
    private String auditRemark;
    @NotNull(message = "工单id不能为空")
    @ApiModelProperty(value = "处理结果（1->正常;0->异常")
    private Integer auditResult;
}
