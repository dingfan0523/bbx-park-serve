package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会服人员完成情况统计模型
 * @author huangyongtao
 * @date 2025/1/9 14:50
 */
@Data
public class MeetingAttendantTaskCountModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务的会议数量")
    private Long reserveNum = 0L;

    @ApiModelProperty(value = "管理的会议室数量")
    private Long roomNum = 0L;

    @ApiModelProperty(value = "提供的会服次数")
    private Long taskNum = 0L;

    @ApiModelProperty(value = "服务过的人次")
    private Long personNum = 0L;

    @ApiModelProperty(value = "整体服务评分")
    private Double score = 0.0D;
}
