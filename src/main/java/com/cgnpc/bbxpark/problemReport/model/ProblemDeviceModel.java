package com.cgnpc.bbxpark.problemReport.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/3/24
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDeviceModel implements Serializable {

    private static final long serialVersionUID = -4928217012804918804L;

    /**
     * 报事报修id
     */
    @ApiModelProperty(value = "报事报修id")
    private Long problemId;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    /**
     * 设备空间id
     */
    @ApiModelProperty(value = "设备空间id")
    private Long spaceId;

    /**
     * 设备空间位置
     */
    @ApiModelProperty(value = "设备空间位置")
    private String spaceName;

}
