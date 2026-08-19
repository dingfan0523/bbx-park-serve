
package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/***
 * @Description 包间预定数据模型实体
 * @author huangyongtao
 * @date 2024/7/30 14:25
 */
@Data
@TableName("bbx_compartment_reserve")
public class CompartmentReserve extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*包间id.
	**/
	private Long compartmentId;

	/**
	*包间名称.
	**/
	private String compartmentName;
	/**
	*预订人id.
	**/
	private String subscriberId;
	/**
	*预订人名称.
	**/
	private String subscriberName;
	/**
	*预订人工号.
	**/
	private String subscriberStaffid;

	/**
	 *套餐id.
	 **/
	private Long comboId;
	/**
	*套餐名称.
	**/
	private String comboName;

	/**
	 * 套餐价格
	 * */
	private BigDecimal comboPrice ;
	/**
	 * 套餐描述
	 **/
	private String comboDescription;
	/**
	*预定开始时间.
	**/
	private Date reserveStartTime;
	/**
	*预定结束时间.
	**/
	private Date reserveEndTime;
	/**
	*联系方式.
	**/
	private String phone;
	/**
	*预定状态.
	**/
	private Integer reserveStatus;
	/**
	*预定来源.
	**/
	private String reserveSource;
	/**
	*到店时间.
	**/
	private Date useTime;
	/**
	*取消时间.
	**/
	private Date cancelTime;
	/**
	*取消原因.
	**/
	private String cancelReason;
	/**
	*取消备注.
	**/
	private String cancelRemark;
	/**
	*删除状态(1->未删;0->已删).
	**/
	private Integer deleted;

}
