package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;

/**
 * 会议室-设备分页参数模型
 * @author dingfan
 * @version 1.0
 * @date 2024/9/23 10:06
 */
@Data
public class MeetingRoomDeviceListParam implements Serializable {
    @ApiModelProperty(value = "会议室id")
    @NotNull(groups = {Default.class},message = "会议室id不能为空")
    private String roomId;
}
