package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 移动端-结束会议入参数据模型
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 17:09
 */
@Data
public class AppMeetingFinishParam implements Serializable {
    @ApiModelProperty(value = "会议id.")
    @NotNull(groups = UpdateGroup.class,message = "会议id不能为空")
    private Long id;
    @ApiModelProperty(value = "是否关闭设备:0->是;1->否")
    @NotNull(groups = UpdateGroup.class,message = "是否关闭设备不能为空")
    private Integer closeFlag;
    @ApiModelProperty(value = "结束会议类型（1->发起人结束会议;2->系统自动结束;3->会服结束会议）")
    private Integer endType;
}
