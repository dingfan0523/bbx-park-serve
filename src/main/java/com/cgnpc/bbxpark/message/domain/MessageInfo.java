package com.cgnpc.bbxpark.message.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息内容数据模型实体
 */
@Data
@TableName("sys_message_info")
public class MessageInfo extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4826019795744690193L;
	/**
	*类型标识.
	**/
	private Long typeId;
	/**
	*消息标题.
	**/
	private String title;
	/**
	*消息摘要.
	**/
	private String summary;
	/**
	*消息内容.
	**/
	private String content;
	/**
	*模版标识.
	**/
	private Long templateId;
	/**
	*模版参数，格式为code、value对象数组.
	**/
	private String templateParam;
	/**
	*业务办理地址.
	**/
	private String linkUrl;
	/**
	*消息状态，0未发布1已发布2已撤销3已删除.
	**/
	private Integer status;
	/**
	*阅读量.
	**/
	private Integer readCount;
	/**
	*签收量.
	**/
	private Integer confirmCount;
	/**
	*回复量.
	**/
	private Integer replyCount;
	/**
	*点赞量.
	**/
	private Integer likeCount;
	/**
	 * 发送设置，1立即发送、2定时发送
	 */
	private Long sendingSettings;
	/**
	 * 封面图片地址
	 */
	private String cover;


	/**
	*收藏量.
	**/
	private Integer collectCount;
	/**
	*接收者范围，0全员1特定用户2分组3角色4组织5当前部门6当前部门及子部门7租户.
	**/
	private Integer receiverScope;
	/**
	*接收者标识.
	**/
	private String receiverId;
	/**
	*接收者数量.
	**/
	private Integer receiverCount;
	/**
	*发布者，0为系统.
	**/
	private String publisher;
	/**
	*发布时间.
	**/
	private Date publishTime;
	/**
	*扩展信息.
	**/
	private String extra;
	/**
	*描述.
	**/
	private String description;
	/**
	*备注.
	**/
	private String remark;
    private Long businessId;
}
