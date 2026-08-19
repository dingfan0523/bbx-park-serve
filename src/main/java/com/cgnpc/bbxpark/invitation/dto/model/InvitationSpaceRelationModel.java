
package com.cgnpc.bbxpark.invitation.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 邀约空间关联业务数据模型
 * @author huangyongtao
 * @date 2025/8/1 13:58
 */
@Data
public class InvitationSpaceRelationModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "邀约id.")
    private Long inviteId;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "空间名称.")
    private String spaceName;

    @ApiModelProperty(value = "空间全称.")
    private String spaceFullName;

    @ApiModelProperty(value = "区域审批信息集合.")
    private List<InvitationSpaceApprovalModel> approvalModels;
}
