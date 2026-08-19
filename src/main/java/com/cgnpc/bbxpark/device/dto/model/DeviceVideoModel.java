package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/***
 * @Description 视频设备
 * @author huangyongtao
 * @date 2025/8/20 16:30
 */
@Data
public class DeviceVideoModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "设备编码.")
    private String deviceCode;

    @ApiModelProperty(value = "所属空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "上线状态;（0->是;1->否）.")
    private Integer onlineStatus;

    @ApiModelProperty(value = "物联设备平台;（0：非物联网设备；1：自有平台；2：统建平台；3：安消平台）.")
    private Integer iotDevicePlatform;

    @ApiModelProperty(value = "物联设备识别码.")
    private String iotDeviceDn;

    @ApiModelProperty(value = "物联设备在线状态;（0->在线;1->离线）.")
    private Integer iotDeviceStatus;

    @ApiModelProperty(value = "是否重点(0->是;1->否).")
    private Integer keyArea;

}