
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 会议室会服业务数据模型
 * @author huangyongtao
 * @date 2025/1/8 15:23
 */
@Data
public class MeetingRoomAttendantModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "使用中(1->是;0->否)")
    private Integer used;

    @ApiModelProperty(value = "是否清扫(1->是;0->否)")
    private Integer swept;

    @ApiModelProperty(value = "呼叫中(1->是;0->否)")
    private Integer calling;

    @ApiModelProperty(value = "会议预约会服信息集合")
    private List<MeetingReserveAttendantModel> meetingReserveAttendantModelList;

}
