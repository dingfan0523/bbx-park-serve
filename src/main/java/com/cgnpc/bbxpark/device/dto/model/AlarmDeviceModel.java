package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/3/12
 * @desc 告警关联设备返回数据
 */
@Data
public class AlarmDeviceModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3673980461963912994L;

    @ApiModelProperty(value = "id主键.")
    private Long id;

    @ApiModelProperty(value = "告警id")
    private Long alarmId;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    /**
     * 设备ID，关联设备信息表的ID
     */
    @ApiModelProperty(value = "设备ID，关联设备信息表的ID")
    private Long deviceId;

    /**
     * 空间ID，关联空间信息表的ID
     */
    @ApiModelProperty(value = "空间ID，关联空间信息表的ID")
    private Long spaceId;

    /**
     * 空间名称
     */
    @ApiModelProperty(value = "空间名称")
    private String spaceName;
}
