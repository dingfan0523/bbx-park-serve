
package com.cgnpc.bbxpark.workorder.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 工单关联设备数据模型实体
 */
@Data
@TableName("bbx_work_order_device")
public class WorkOrderDevice extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*设备id.
	**/
	private Long deviceId;
	/**
	*设备名称.
	**/
	private String deviceName;
	/**
	 * 设备DN
	 */
	private String deviceDn;
	/**
	*设备历史状态.
	**/
	private Boolean deviceHisState;
	/**
	*空间id.
	**/
	private Long spaceId;
	/**
	*空间全路径.
	**/
	private String spaceFullPath;
	/**
	*工单id.
	**/
	private Long workOrderId;

	/**
	 *抄表类型;（water：水表；electricity：电表；gas：燃气表）.
	 **/
	private String readingType;

	/**
	 *抄表编码
	 **/
	private String readingCode;

	/**
	 *抄表倍率
	 **/
	private Integer readingRate;

	/**
	 *抄表值
	 **/
	private BigDecimal readingValue;

	/**
	 *抄表异常状态（1->是;0->否）
	 **/
	private Integer readingErrorStatus;

    /**
     * 异常说明
     */
	private String readingErrorRemark;

}
