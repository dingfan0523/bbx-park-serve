package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 会议更改结束时间入参数据模型
 * @author dingfan
 * @version 1.0
 * @date 2024/9/23 20:15
 */
@Data
public class MeetingReserveTimeParam implements Serializable {
    @ApiModelProperty(value = "会议id.")
    @NotNull(groups = UpdateGroup.class,message = "会议id不能为空")
    private Long id;
    @ApiModelProperty(value = "会议实际结束时间.")
    @NotNull(groups = UpdateGroup.class,message = "实际结束时间不能为空")
    private Date realEndTime;
}
