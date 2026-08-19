
package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 设备点位
 * @author huangyongtao
 * @date 2025/2/21 17:03
 */
@Data
@TableName("bbx_device_point")
public class DevicePoint implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 设备id
     */
    private String deviceId;
	/**
	*设备名称.
	**/
	private String deviceName;
    /**
     * 点位数据
     */
    private String positioning;
    private Long tenantId;
}
