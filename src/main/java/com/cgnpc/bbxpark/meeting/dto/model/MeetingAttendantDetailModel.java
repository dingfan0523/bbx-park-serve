
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 会服人员任务业务数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:25
 */
@Data
public class MeetingAttendantDetailModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "用户id.")
    private String userId;
    @ApiModelProperty(value = "用户名称.")
    private String userName;
    @ApiModelProperty(value = "用户工号.")
    private String userStaffid;
    @ApiModelProperty(value = "会议室id集合")
    private List<Long> roomIdList;
    @ApiModelProperty(value = "会议室名称集合")
    private List<String> roomNameList;
    @ApiModelProperty(value = "会议室名称集合")
    private List<MeetingAttendantRoomModel> roomList;
}
