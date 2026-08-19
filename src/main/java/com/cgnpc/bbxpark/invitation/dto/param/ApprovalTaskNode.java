package com.cgnpc.bbxpark.invitation.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/8/1 10:38
 */
@Data
public class ApprovalTaskNode {
    @ApiModelProperty(value = "扩展内容")
    private ExtendContent taskExtend;
    @ApiModelProperty(value = "审批人id集合")
    private List<String> userIds;
}
