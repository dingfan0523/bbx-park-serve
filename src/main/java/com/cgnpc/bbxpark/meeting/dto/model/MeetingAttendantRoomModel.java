
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会服人员-会议室业务数据模型
 */
@Data
public class MeetingAttendantRoomModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "会议室id")
    private Long roomId;
    @ApiModelProperty(value = "会议室名称")
    private String roomName;
    @ApiModelProperty(value = "状态:1->正常状态;2->等待新增状态;3->等待删除状态")
    private Integer status;
}
