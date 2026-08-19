package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/***
 * @Description 会议延时入参
 * @author huangyongtao
 * @date 2024/12/28 10:44
 */
@Data
public class MeetingReserveDelayParam implements Serializable {
    @ApiModelProperty(value = "会议id.")
    @NotNull(groups = UpdateGroup.class,message = "会议id不能为空")
    private Long id;
    @ApiModelProperty(value = "延时时间（分钟）")
    @NotNull(groups = UpdateGroup.class,message = "延时时间（分钟）")
    private Integer delayTime;
}
