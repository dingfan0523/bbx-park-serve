
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName(value = "bbx_restaurant_waste_record")
public class RestaurantWasteRecord extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*餐厅名称.
	**/
	private String name;
	/**
	*垃圾类型.
	**/
	private String type;
    /**
     *数量.
     **/
    private Double quantity;
    /**
     *处理时间.
     **/
    private Date handleTime;
	/**
	*处理单位
	**/
	private String handleUnit;

    /**
     *文件id
     **/
    private Long fileId;
}
