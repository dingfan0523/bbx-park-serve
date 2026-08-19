package com.cgnpc.bbxpark.energy.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 支路设备
 */
@Data
public class BranchDeviceParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    /** 设备id */
    @ApiModelProperty(value = "设备id")
    private Long deviceId ;
    /** 设备名称 */
    @ApiModelProperty(value = "设备名称")
    private String deviceName ;
    /** 空间位置名称 */
    @ApiModelProperty(value = "空间位置名称")
    private String spaceName ;
    /** 空间位置id */
    @ApiModelProperty(value = "空间位置id")
    private Long spaceId ;
}
