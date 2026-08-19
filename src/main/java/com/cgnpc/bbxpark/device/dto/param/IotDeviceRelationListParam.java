package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description ioc设备关联列表参数模型
 * @author huangyongtao
 * @date 2025/3/3 15:39
 */
@Data
public class IotDeviceRelationListParam implements Serializable {
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

   @ApiModelProperty(value = "设备id.")
    private Long deviceId;

   @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
