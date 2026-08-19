package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会议重置结束时间入参
 * @author huangyongtao
 * @date 2025/1/2 16:32
 */
@Data
public class AppMeetingResetEndTimeParam implements Serializable {
    @ApiModelProperty(value = "会议id.")
    @NotNull(groups = UpdateGroup.class,message = "会议id不能为空")
    private Long id;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "结束会议类型（1->发起人结束会议;2->系统自动结束;3->会服结束会议）")
    private Integer endType;
}
