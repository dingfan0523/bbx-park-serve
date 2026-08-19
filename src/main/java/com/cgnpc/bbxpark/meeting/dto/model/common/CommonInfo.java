package com.cgnpc.bbxpark.meeting.dto.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 公用业务数据模型
 *
 * 设备信息及空间信息
 * @author dingfan
 * @version 1.0
 * @date 2024/10/11 17:02
 */
@Data
public class CommonInfo {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "空间位置名称")
    private String spaceName;
    @ApiModelProperty(value = "是否有视频会议设备")
    private Boolean meetingDevice = false;
    @ApiModelProperty(value = "设备id集合")
    private List<Long> deviceIdList;
    @ApiModelProperty(value = "设备名称集合")
    private List<String> deviceNameList;
    @ApiModelProperty(value = "会服id集合")
    private List<Long> serviceIdList;
    @ApiModelProperty(value = "会服名称集合")
    private List<String> serviceNameList;
}
