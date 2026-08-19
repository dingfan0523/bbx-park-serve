package com.cgnpc.bbxpark.device.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_device")
public class DeviceInfo implements Serializable {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -3354807916203831267L;

	/**
	 *设备id.
	 **/
	private String deviceId;
	/**
	 *设备名称.
	 **/
	private String deviceName;
	/**
	 *纬度.
	 **/
	private String latitude;
	/**
	 *经度.
	 **/
	private String longitude;
	/**
	 *设备类型.
	 **/
    @TableField("\"model\"")
	private String model;
	/**
	 *设备离线时间.
	 **/
	private Long offlineTime;
	/**
	 *设备在线时间.
	 **/
	private Long onlineTime;
	/**
	 *父级id.
	 **/
	private String parentId;
	/**
	 *产品key.
	 **/
	private String productKey;
	/**
	 *设备密钥.
	 **/
	private String secret;
	/**
	 *设备状态.
	 **/
	private String state;
	/**
	 *设备别名.
	 **/
	private String deviceAlias;
	/**
	 *空间位置json.
	 **/
	private String spaceAddr;
	/**
	 *停用状态.
	 **/
	private Long stopState;
	/**
	 *空间id.
	 **/
	private Long spaceId;
	/**
	 *用户id.
	 **/
	private String uid;

	/**
	 * 模型名称
	 */
	private String modelName;

	/**
	 * 位姿
	 */
	private String positioning;

	/**
	 * 绑定状态(0:绑定，1:未绑定)
	 */
	private Long buildState;

	/**
	 * 设备名称
	 */
	private String name;

	/**
	 * 产品名称
	 */
	private String productName;

    private Long tenantId;
}
