
package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备控制（操作）日志数据模型实体
 */
@Data
@TableName("bbx_device_operation_log")
public class DeviceOperationLog extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*设备id.
	**/
	private String deviceId;
	/**
	*类型(1规则,2场景,3能力清单).
	**/
	private Long type;
	/**
	*控制报文.
	**/
	private String params;
	/**
	*返回报文.
	**/
	private String response;

	private String createUserName;

	private String deviceName;

	private Long spaceId;

}
