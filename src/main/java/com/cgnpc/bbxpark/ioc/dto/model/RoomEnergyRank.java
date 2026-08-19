package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 15:13
 */
@Data
@ApiModel(value = "会议室节能排名")
public class RoomEnergyRank {
    @ApiModelProperty(value = "会议室名称")
    private String roomName;
    @ApiModelProperty(value = "节约能耗(KWH)")
    private Double energySavedKwh;
}
