package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 维保设备关联入参数据模型
 * @author huangyongtao
 * @date 2025/10/16 16:25
 */
@Data
public class MaintainDeviceParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "维保id.")
    private Long maintainId;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "任务组.")
    private Integer taskGroup;
}
