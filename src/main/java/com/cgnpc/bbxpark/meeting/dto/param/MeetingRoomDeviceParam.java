package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 会议室关联设备入参数据模型
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/23 9:27
 */
@Data
public class MeetingRoomDeviceParam {
    @ApiModelProperty(value = "主键id.")
    @NotNull(groups = UpdateGroup.class, message = "会议室id不能为空")
    private Long id;
    @ApiModelProperty(value = "设备id集合")
    private List<Long> deviceIdList;
}
