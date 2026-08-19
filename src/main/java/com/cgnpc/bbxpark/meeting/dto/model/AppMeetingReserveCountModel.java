package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端-会议数量统计模型
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 11:32
 */
@Data
public class AppMeetingReserveCountModel implements Serializable {
    @ApiModelProperty(value = "今日会议数量")
    private Integer todayCount;
    @ApiModelProperty(value = "未来两日会议数量")
    private Integer twoDayCount;
}
