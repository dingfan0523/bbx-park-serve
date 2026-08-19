package com.cgnpc.bbxpark.message.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户消息数据模型实体
 */
@Data
@TableName("sys_message_user")
public class MessageUser extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3785018401296861615L;

	/**
	*用户标识.
	**/
	private String userId;
	/**
	*消息标识.
	**/
	private Long msgId;
	/**
	*阅读状态，0未读1已读.
	**/
	private Integer readStatus;
	/**
	*签收状态，0未确认1已确认.
	**/
	private Integer confirmStatus;
	/**
	*是否点赞，0未点赞1已点赞.
	**/
	private Integer likeStatus;
	/**
	*是否收藏，0未收藏1已收藏.
	**/
	private Integer collectStatus;
	/**
	*地址参数，格式为code、value对象数组.
	**/
	private String linkParam;
	/**
	*回复内容.
	**/
	private String replyContent;
	/**
	*状态，0删除1正常.
	**/
	private Integer status;
}
