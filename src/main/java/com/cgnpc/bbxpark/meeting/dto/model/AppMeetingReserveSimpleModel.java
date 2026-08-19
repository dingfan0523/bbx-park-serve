package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 移动端-简单的会议
 * @author dingfan
 * @version 1.0
 * @date 2024/9/29 10:06
 */
@Data
public class AppMeetingReserveSimpleModel implements Serializable {
    @ApiModelProperty(value = "会议id.")
    private Long id;
    @ApiModelProperty(value = "规则最后修改时间")
    private Date lastRuleTime;
}
