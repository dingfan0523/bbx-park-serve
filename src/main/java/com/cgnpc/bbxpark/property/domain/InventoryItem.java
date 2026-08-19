package com.cgnpc.bbxpark.property.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 盘点计划明细数据模型实体
 */
@Data
@TableName("bbx_inventory_item")
public class InventoryItem extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4979981056567685732L;

	/**
	*盘点计划id.
	**/
	private Long inventoryId;
	/**
	*类型.
	**/
	private Integer type;
	/**
	*相关联id(多个以英文逗号隔开).
	**/
	private String relatedId;
	/**
	*内容.
	**/
	private String content;
}
