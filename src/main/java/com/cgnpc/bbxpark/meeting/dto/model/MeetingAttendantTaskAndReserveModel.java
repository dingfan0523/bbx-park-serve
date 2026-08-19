
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会服和预约的关联数据模型
 * @author huangyongtao
 * @date 2025/2/11 16:28
 */
@Data
public class MeetingAttendantTaskAndReserveModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会议id.")
    private Long reserveId;

    @ApiModelProperty(value = "实际参会人数.")
    private Integer realParticipantNumber;

    @ApiModelProperty(value = "处理人id.")
    private String handleUid;

    @ApiModelProperty(value = "评价分数")
    private Integer score = 0;

}
