package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/9/26 15:53
 */
@Data
public class MeetingSaveParam implements Serializable {
    @ApiModelProperty(value = "第三方会议室id.")
    private String thirdRoomId;
    @ApiModelProperty(value = "第三方会议id")
    private String thirdReserveId;
    @ApiModelProperty(value = "会议主题.")
    private String reserveName;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
