package com.cgnpc.bbxpark.invitation.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 审批任务保存参数
 * @author dingfan
 * @version 1.0
 * @date 2025/8/1 9:53
 */
@Data
public class ApprovalTaskSaveParam implements Serializable {
    @ApiModelProperty(value = "业务id")
    private Long businessId;
    @ApiModelProperty(value = "审批任务扩展内容")
    private List<ExtendContent> taskExtends;
}
