
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName(value = "bbx_restaurant_card_record")
public class RestaurantCardRecord extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*一卡通号.
	**/
	private String cardNo;
	/**
	*姓名.
	**/
	private String name;
    /**
     *餐厅名称.
     **/
    private String restaurantName;
	/**
	*发生时间.
	**/
	private Date occurTime;
	/**
	*餐交易额
	**/
	private Double tradeAmount;
	/**
	*账号余额.
	**/
	private Double accountBalance;
	/**
	*用卡次数.
	**/
	private Integer cardUseNum;
	/**
	*工号.
	**/
	private String staff;
	/**
	*pos号
	**/
	private String pos;

    /**
     *文件id
     **/
    private Long fileId;
}
