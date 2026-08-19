
package com.cgnpc.bbxpark.device.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_device_control_log")
public class DeviceControlLog extends BaseExEntity implements Serializable {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -3354807916203831267L;
	/**
	 *设备id.
	 **/
	private String channel;
	/**
	 *设备id.
	 **/
	private Long deviceId;
	/**
	 *设备名称.
	 **/
	private String deviceName;
	/**
	 *操作人姓名.
	 **/
	private String name;
	/**
	 *操作人工号.
	 **/
	private String staffid;
	/**
	 *操作结果.
	 **/
	private String result;
	/**
	 *删除状态(1->未删;0->已删).
	 **/
	private Integer deleted;
}
