
package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 设备状态记录
 * @author huangyongtao
 * @date 2025/2/21 17:03
 */
@Data
@TableName("bbx_device_health_record")
public class DeviceHealthRecord implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 空间id
     */
    private Long spaceId;
	/**
	*设备名称.
	**/
	private String deviceName;
    /**
     *在线:1->是;0->否.
     **/
    private Integer online;
    /**
     *告警:1->是;0->否.
     **/
    private Integer alarm;
    /**
     * 记录时间
     */
    private Date recordTime;
    private Long tenantId;
}
