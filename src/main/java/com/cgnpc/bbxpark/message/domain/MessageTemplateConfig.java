
package com.cgnpc.bbxpark.message.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 消息模版配置数据模型实体
 * @author huangyongtao
 * @date 2024/10/24 16:07
 */
@Data
@TableName("bbx_message_template_config")
public class MessageTemplateConfig  extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*模版id.
	**/
	private Long templateId;
	/**
	*类型;person：人员；role：角色.
	**/
	private String type;
	/**
	*推送id.
	**/
	private String pushId;
}
