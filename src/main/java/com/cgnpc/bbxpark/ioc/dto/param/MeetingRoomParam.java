package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "会议室筛选参数")
public class MeetingRoomParam implements Serializable {
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "空间模型编码")
    private String sslcCode;
}

