
package com.cgnpc.bbxpark.invitation.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 邀约区域审批信息
 * @author huangyongtao
 * @date 2025/8/6 11:14
 */
@Data
public class InvitationSpaceApprovalModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;
    @ApiModelProperty(value = "审批人集合")
    private List<ApproverModel> approverList;
}
