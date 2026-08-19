
package com.cgnpc.bbxpark.message.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 消息模版数据模型实体
 * @author huangyongtao
 * @date 2024/10/24 16:05
 */
@Data
@TableName("bbx_message_template")
public class MessageTemplate extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
	/**
	*模版编码.
	**/
	private String code;
	/**
	*消息标题.
	**/
	private String title;
	/**
	*消息类型;restaurant：智慧餐厅；order：智慧工单；complaint：投诉建议；meeting：智慧会议；alarm：告警消息.
	**/
	private String type;
	/**
	*消息内容.
	**/
	private String content;
	/**
	*模版状态;0：启用；1：禁用.
	**/
	private Integer status;
	/**
	*推送渠道;多个以英文逗号隔开 info：站内信；ding：钉钉；sms：短信；mail:邮件.
	**/
	private String pushChannel;
	/**
	*是否推送;0：启用；1：禁用.
	**/
	private Integer pushFlag;

}
