
package com.cgnpc.bbxpark.invitation.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
/***
 * @Description 邀约空间关联入参数据模型
 * @author huangyongtao
 * @date 2025/8/1 14:10
 */
@Data
public class InvitationSpaceRelationParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邀约id.")
    private Long inviteId;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @Length(max = 50)
    @ApiModelProperty(value = "空间名称.")
    private String spaceName;

    @Length(max = 100)
    @ApiModelProperty(value = "空间全称.")
    private String spaceFullName;
}
