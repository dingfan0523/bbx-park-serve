package com.cgnpc.bbxpark.energy.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 支路设备关联表
 */
@Data
public class BranchDeviceModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    /** 主键id */
    @ApiModelProperty(value = "主键id")
    private Long id ;
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
    /** 支路的主键id */
    @ApiModelProperty(value = "支路的主键id")
    private Long branchId ;
    /** 是否删除（1:正常；0：已删除） */
    @ApiModelProperty(value = "是否删除（1:正常；0：已删除）")
    private Integer deleted ;
}
