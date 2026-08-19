
package com.cgnpc.bbxpark.invitation.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 邀约空间关联列表参数模型
 * @author huangyongtao
 * @date 2025/8/1 14:09
 */
@Data
public class InvitationSpaceRelationListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邀约id.")
    private Long inviteId;

    @ApiModelProperty(value = "邀约id集合.")
    private List<Long> inviteIdList;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "空间名称.")
    private String spaceName;

    @ApiModelProperty(value = "空间全称.")
    private String spaceFullName;
}
