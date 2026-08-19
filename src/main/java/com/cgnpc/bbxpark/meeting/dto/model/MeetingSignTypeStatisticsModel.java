package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会议签到类型统计模型
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 11:32
 */
@Data
public class MeetingSignTypeStatisticsModel implements Serializable {
    @ApiModelProperty(value = "参会人数")
    private Integer total;
    @ApiModelProperty(value = "正常签到数量")
    private Integer normalCount;
    @ApiModelProperty(value = "补签数量")
    private Integer repairCount;
    @ApiModelProperty(value = "代签到数量")
    private Integer behalfCount;
    @ApiModelProperty(value = "代补签数量")
    private Integer behalfRepairCount;
    @ApiModelProperty(value = "未签到数量")
    private Integer notCount;
}
