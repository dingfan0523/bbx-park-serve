
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName(value = "bbx_restaurant_inventory_record")
public class RestaurantInventoryRecord extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*仓库名称.
	**/
	private String godown;
	/**
	*物料大类.
	**/
	private String category;
    /**
     *物料中类.
     **/
    private String mediumCategory;
	/**
	*物料名称.
	**/
	private String name;
	/**
	*库存单位
	**/
	private String stockUnit;
	/**
	*库存数量.
	**/
	private Double stockQuantity;

    /**
     *文件id
     **/
    private Long fileId;
}
