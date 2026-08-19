
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * 会服人员-会议室入参数据模型
 */
@Data
public class MeetingAttendantRoomParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "会议室id集合")
    private List<Long> roomIdList;
}
