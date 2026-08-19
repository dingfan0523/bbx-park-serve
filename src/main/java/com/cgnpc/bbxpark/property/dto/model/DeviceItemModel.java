package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/11/5 9:44
 */
@Data
public class DeviceItemModel implements Serializable {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;
    @ApiModelProperty(value = "设备编码.")
    private String deviceCode;
    @ApiModelProperty(value = "所属空间名称.")
    private String spaceName;
    @ApiModelProperty(value = "设备分类；1：弱电设备；2：CIT设备；3：固定资产.")
    private Integer deviceCategory;
    @ApiModelProperty(value = "删除状态(0->已删;1->未删).")
    private Integer deleted = 1;
}
