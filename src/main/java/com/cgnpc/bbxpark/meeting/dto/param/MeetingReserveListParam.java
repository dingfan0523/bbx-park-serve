
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会议预约列表参数模型
 * @author huangyongtao
 * @date 2024/8/23 15:24
 */
@Data
public class MeetingReserveListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @ApiModelProperty(value = "会议室id集合")
    private List<Long> roomIdList;

    @ApiModelProperty(value = "会议主题.")
    private String reserveName;

    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;

    @ApiModelProperty(value = "会议结束时间.")
    private Date endTime;

    @ApiModelProperty(value = "预约人id.")
    private String reserveUid;

    @ApiModelProperty(value = "预约人名称.")
    private String reserveUname;

    @ApiModelProperty(value = "预约人工号.")
    private String reserveStaffid;

    @ApiModelProperty(value = "会议实际开始时间.")
    private Date realStartTime;

    @ApiModelProperty(value = "会议实际结束时间.")
    private Date realEndTime;

    @ApiModelProperty(value = "会议无效;0：否；1：是.")
    private Integer inValid;

    @ApiModelProperty(value = "会议无效备注.")
    private String inValidRemark;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "是否取消:1->是;0->否")
    private Integer cancelFlag;


}
