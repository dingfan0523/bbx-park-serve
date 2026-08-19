
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会服顶部统计数据
 * @author huangyongtao
 * @date 2025/2/10 10:55
 */
@Data
public class MeetingAttendantTaskTopCountModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提供的会服次数")
    private Long attendantTaskNum = 0L;

    @ApiModelProperty(value = "服务的会议场次")
    private Long meetingNum = 0L;

    @ApiModelProperty(value = "本次服务会议的人数")
    private Long personNum = 0L;

    @ApiModelProperty(value = "上次服务会议的人数")
    private Long lastPersonNum = 0L;

    @ApiModelProperty(value = "上上次服务会议的人数")
    private Long lastLastPersonNum = 0L;

    @ApiModelProperty(value = "服务的平均分")
    private String averageScore = "0.00";

    @ApiModelProperty(value = "服务的平均分Double类型")
    private Double averageScoreDouble = 0d;

    @ApiModelProperty(value = "服务会议人数环比")
    private String meetingPersonChain = "0.00%";

    @ApiModelProperty(value = "会前布置的比例")
    private String taskBeforeRate = "0.00%";

    @ApiModelProperty(value = "会中呼叫的比例")
    private String taskInRate = "0.00%";

    @ApiModelProperty(value = "会后清洁的比例")
    private String taskAfterRate = "0.00%";

    @ApiModelProperty(value = "会服过期的比例")
    private String taskExpireRate = "0.00%";

    @ApiModelProperty(value = "会服完成的比例")
    private String taskCompleteRate = "0.00%";

    @ApiModelProperty(value = "评分高于4分的比例")
    private String scoreHighRate = "0.00%";

    @ApiModelProperty(value = "评分2-4分的比例")
    private String scoreInRate = "0.00%";

    @ApiModelProperty(value = "评分低于2分的比例")
    private String scoreLowRate = "0.00%";

}
