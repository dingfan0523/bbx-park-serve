package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 会议签到分页参数模型
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 10:34
 */
@Data
public class MeetingSignPageParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "会议id")
    @NotNull(groups = UpdateGroup.class, message = "会议id不能为空")
    private Long reserveId;
    @ApiModelProperty(value = "签到人名称")
    private String signUname;
    @ApiModelProperty(value = "签到类型:1->本人签到;2->补签;3->代签到;4->代补签")
    private Integer type;
}
