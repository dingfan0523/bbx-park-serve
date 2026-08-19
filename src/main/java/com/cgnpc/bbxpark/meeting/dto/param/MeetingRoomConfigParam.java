package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 会议室配置内容入参数据模型
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/23 9:27
 */
@Data
public class MeetingRoomConfigParam implements Serializable {
    @ApiModelProperty(value = "主键id.")
    @NotNull(groups = UpdateGroup.class, message = "会议室id不能为空")
    private Long id;
    @ApiModelProperty(value = "提醒内容")
    @Length(max = 200)
    private String warnContent;
    @ApiModelProperty(value = "是否支持排座(0->支持;1->不支持)")
    @Max(value = 1)
    @Min(value = 0)
    private Integer rowSeat;
    @ApiModelProperty(value = "是否支持打印(0->支持;1->不支持)")
    @Max(value = 1)
    @Min(value = 0)
    private Integer print;
}
