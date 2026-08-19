package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 15:53
 */
@Data
@ApiModel(value = "会议")
public class SimpleMeetingRoomModel {
    @ApiModelProperty(value = "会议室ID")
    private String id;
    @ApiModelProperty(value = "会议室名称")
    private String roomName;
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "模型编码")
    private String sslcCode;
}
