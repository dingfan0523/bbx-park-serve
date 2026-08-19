
package com.cgnpc.bbxpark.invitation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 邀约访客数据模型实体
 * @author huangyongtao
 * @date 2025/8/1 13:55
 */
@Data
@TableName("bbx_invitation_visitor")
public class InvitationVisitor extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*邀约id.
	**/
	private Long inviteId;
	/**
	*名称.
	**/
	private String name;
	/**
	*电话.
	**/
	private String phone;
	/**
	*公司.
	**/
	private String company;
	/**
	*人脸照片.
	**/
	private String faceImg;
	/**
	*第三方访客id.
	**/
	private String thirdVisitId;
	/**
	*是否到访;0：到访；1：未到访.
	**/
	private Integer visitStatus;
	/**
	*到访时间.
	**/
	private Date visitTime;
}
