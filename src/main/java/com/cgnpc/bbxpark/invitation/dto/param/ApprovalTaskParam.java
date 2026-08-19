
package com.cgnpc.bbxpark.invitation.dto.param;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 审批任务入参数据模型
 * @author 54766
 */
@Data
public class ApprovalTaskParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3652118401302532550L;

    @ApiModelProperty(value = "业务id")
    @NotNull(message = "业务id不能为空", groups = {InsertGroup.class})
    private Long businessId;
    @ApiModelProperty(value = "审批节点集合")
    private List<ApprovalTaskNode> taskNodes;
}
