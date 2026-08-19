package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更改会议签到规则入参数据模型
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 15:58
 */
@Data
public class AppMeetingSignRuleParam implements Serializable {
    @ApiModelProperty(value = "会议id")
    @NotNull(groups = UpdateGroup.class,message = "会议id不能为空")
    private Long id;
    @ApiModelProperty(value = "是否必须签到(0->必须签到;1->自愿签到)")
    @Min(value = 0)
    @Max(value = 1)
    @NotNull(groups = UpdateGroup.class,message = "签到要求不能为空")
    private Integer mustSignFlag;
    @ApiModelProperty(value = "是否允许代签(0->允许;1->不允许)")
    @Min(value = 0)
    @Max(value = 1)
    @NotNull(groups = UpdateGroup.class,message = "代签到不能为空")
    private Integer behalfSignFlag;
    @ApiModelProperty(value = "是否允许补签(0->允许;1->不允许)")
    @Min(value = 0)
    @Max(value = 1)
    @NotNull(groups = UpdateGroup.class,message = "补签不能为空")
    private Integer replenishSignFlag;
}
