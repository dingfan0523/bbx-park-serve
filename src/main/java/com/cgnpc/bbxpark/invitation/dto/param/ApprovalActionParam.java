package com.cgnpc.bbxpark.invitation.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/8/5 9:25
 */
@Data
public class ApprovalActionParam {
    /**
     * 业务ID
     */
    @ApiModelProperty(value = "邀约ID")
    private Long businessId;
    /**
     * 审批结果: true-通过, false-拒绝
     */
    @ApiModelProperty(value = "审批结果: true-通过, false-拒绝")
    private Boolean approved;
    /**
     * 审批备注
     */
    @ApiModelProperty(value = "审批备注")
    private String remark;
}
