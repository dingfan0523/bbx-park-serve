package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description ioc设备关联业务数据模型
 * @author huangyongtao
 * @date 2025/3/3 15:41
 */
@Data
public class IotDeviceRelationModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

   @ApiModelProperty(value = "主键id.")
    private Long id;

   @ApiModelProperty(value = "关联类型;(video：视频设备).")
    private String relationType;

   @ApiModelProperty(value = "关联的设备id.")
    private Long relationDeviceId;

   @ApiModelProperty(value = "管理的设备名称.")
    private String relationDeviceName;

   @ApiModelProperty(value = "空间名称.")
    private String spacesName;

   @ApiModelProperty(value = "设备id.")
    private Long deviceId;
   
    @ApiModelProperty(value = "IOT设备dn")
    private String iotDeviceDn;


}
