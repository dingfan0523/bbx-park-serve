package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description iot设备数据校验数据模型
 * @author huangyongtao
 * @date 2025/2/21 17:07
 */
@Data
public class IotDeviceCheckModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

   @ApiModelProperty(value = "物联设备识别码.")
    private String iotDeviceDn;

   @ApiModelProperty(value = "物理设备在线状态;（0->在线;1->离线）.")
    private Integer iotDeviceStatus = 0;

   @ApiModelProperty(value = "物联产品类型.")
    private String iotProductCode;

}
