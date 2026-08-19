package com.cgnpc.bbxpark.message.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息类型数据模型实体
 */
@Data
@TableName("sys_message_type")
public class MessageType implements Serializable {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -4170938257658300754L;

	/**
	 *类型标识.
	 **/
	private Long id;
	/**
	 *类型名称.
	 **/
	private String name;
	/**
	 *类型编码.
	 **/
	private String code;
	/**
	 *图标.
	 **/
	private String icon;
	/**
	 *类型，0系统1自定义.
	 **/
	private Short type;
	/**
	 *状态，0正常1禁用.
	 **/
	private Short status;
	/**
	 *是否需要阅读反馈，0不需要1需要.
	 **/
	private Short needRead;
	/**
	 *是否需要签收确认，0不需要1需要.
	 **/
	private Short needConfirm;
	/**
	 *是否需要回复，0不需要1需要.
	 **/
	private Short needReply;
	/**
	 *是否需要办理，0不需要1需要.
	 **/
	private Short needHandle;
	/**
	 *能否删除，0可以1不可以.
	 **/
	private Short canDelete;
	/**
	 *能否定制通知配置，0可以1不可以.
	 **/
	private Short canNotifications;
	/**
	 *能否全员广播消息，0可以1不可以.
	 **/
	private Short canBroadcast;
	/**
	 *广播消息人数限制，0或-1表示无限制.
	 **/
	private Integer broadcastLimit;
	/**
	 *描述.
	 **/
	private String description;
	/**
	 *创建时间.
	 **/
	private Date createTime;
	/**
	 *创建者.
	 **/
	private String creatorId;
	/**
	 *修改时间.
	 **/
	private Date updateTime;
	/**
	 *修改者.
	 **/
	private String updatorId;

	private String creatorName;

	private String updatorName;

}
