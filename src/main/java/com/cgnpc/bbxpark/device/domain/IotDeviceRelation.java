package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description ioc设备关联数据模型实体
 * @author huangyongtao
 * @date 2025/3/3 15:40
 */
@Data
@TableName("bbx_iot_device_relation")
public class IotDeviceRelation extends BaseExEntity implements Serializable  {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*关联类型;(video：视频设备).
	**/
	private String relationType;
	/**
	*关联的设备id.
	**/
	private Long relationDeviceId;
	/**
	*设备id.
	**/
	private Long deviceId;

}
