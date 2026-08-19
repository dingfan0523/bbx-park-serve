package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 会议设为无效入参数据模型
 * @author dingfan
 * @version 1.0
 * @date 2024/9/23 20:24
 */
@Data
public class MeetingReserveInValidParam implements Serializable {
    @ApiModelProperty(value = "会议id.")
    @NotNull(groups = UpdateGroup.class,message = "会议id不能为空")
    private Long id;
    @ApiModelProperty(value = "无效原因")
    @NotNull(groups = UpdateGroup.class,message = "无效原因不能为空")
    private String operateReason;
}
