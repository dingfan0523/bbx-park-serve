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
public class MeetingRoomModel {
    @ApiModelProperty(value = "会议室ID")
    private String id;
    @ApiModelProperty(value = "会议室名称")
    private String roomName;
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "模型编码")
    private String sslcCode;
    @ApiModelProperty(value = "会议室图片")
    private String imageUrl;
    @ApiModelProperty(value = "使用状态(0->空闲;1->占用;2->临时占用)")
    private Integer used;
    @ApiModelProperty(value = "容量")
    private Integer roomVolume;
    @ApiModelProperty(value = "设备数量")
    private Long deviceCount;
    @ApiModelProperty(value = "照明状态(0->关;1->开)")
    private Integer lightStatus;
}
