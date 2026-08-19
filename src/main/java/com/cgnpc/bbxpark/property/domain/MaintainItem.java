package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 维保项目数据模型实体
 * @author huangyongtao
 * @date 2025/10/16 15:15
 */
@Data
@TableName("bbx_maintain_item")
public class MaintainItem  extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*维保id.
	**/
	private Long maintainId;
	/**
	*名称.
	**/
	private String name;
	/**
	*内容.
	**/
	private String content;
	/**
	*任务组.
	**/
	private Integer taskGroup;
}