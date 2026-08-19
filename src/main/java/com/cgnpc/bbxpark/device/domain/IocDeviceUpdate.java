
package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description ioc设备数据模型实体
 * @author huangyongtao
 * @date 2025/2/21 17:03
 */
@Data
@TableName("bbx_ioc_device")
public class IocDeviceUpdate implements Serializable {
    /**
	 /**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *标识.
	 **/
	@TableId(value = "id",type = IdType.AUTO)
	private Long id;

	/**
	 *物理设备在线状态;（0->在线;1->离线）.
	 **/
	private Integer iotDeviceStatus;

	/**
	 *物理设备在线状态更新时间
	 **/
	private Date iotDeviceStatusTime;


}
