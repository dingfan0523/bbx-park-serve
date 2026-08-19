package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "会议筛选参数")
public class MeetingPageParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "会议室id")
    private Long roomId;
    @ApiModelProperty(value = "会议名称")
    private String meetingName;
    @ApiModelProperty(value = "类型:special->专项会议;pilot->试点会议")
    private String type;

}

