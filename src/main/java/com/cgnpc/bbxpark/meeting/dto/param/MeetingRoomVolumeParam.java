package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 会议室容量入参数据模型
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/23 9:27
 */
@Data
public class MeetingRoomVolumeParam implements Serializable {
    @ApiModelProperty(value = "主键id.")
    @NotNull(groups = UpdateGroup.class, message = "会议室id不能为空")
    private Long id;
    @ApiModelProperty(value = "容纳人数.")
    @Min(value = 1)
    private Integer roomVolume;
}
