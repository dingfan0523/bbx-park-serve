
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName(value = "bbx_restaurant_inbound_record")
public class RestaurantInboundRecord extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*仓库名称.
	**/
	private String godown;
	/**
	*供应商名称.
	**/
	private String supplier;
    /**
     *入库时间.
     **/
    private Date inboundTime;
	/**
	*物料类型.
	**/
	private String category;
    /**
     *物料名称.
     **/
    private String name;
	/**
	*入库单位
	**/
	private String inboundUnit;
	/**
	*入库数量.
	**/
	private Double inboundQuantity;

    /**
     *文件id
     **/
    private Long fileId;
}
