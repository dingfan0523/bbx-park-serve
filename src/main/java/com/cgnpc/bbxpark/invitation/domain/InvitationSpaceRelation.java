
package com.cgnpc.bbxpark.invitation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 邀约空间关联数据模型实体
 * @author huangyongtao
 * @date 2025/8/1 13:46
 */
@Data
@TableName("bbx_invitation_space_relation")
public class InvitationSpaceRelation extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*邀约id.
	**/
	private Long inviteId;
	/**
	*空间id.
	**/
	private Long spaceId;
	/**
	*空间名称.
	**/
	private String spaceName;
	/**
	*空间全称.
	**/
	private String spaceFullName;

}
