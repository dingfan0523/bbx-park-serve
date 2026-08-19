package com.cgnpc.bbxpark.message.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知公告数据模型实体
 */
@Data
@TableName("sys_message_notice")
public class MessageNotice extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3379643041586565790L;

	/**
	*类型标识.
	**/
	private Long typeId;

	/**
	*消息标识.
	**/
	private Long msgId;

	/**
	*通知公告类型，0通知1公告.
	**/
	private Integer type;
	/**
	*优先级，0低1中2高.
	**/
	private Integer priority;
	/**
	*发布级别，0系统级1组织级2部门级3租户级4应用级.
	**/
	private Integer level;
	/**
	*有效状态，0有效1无效2已过期.
	**/
	private Integer effectiveStatus;
	/**
	*有效日期.
	**/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date effectiveDate;

}
