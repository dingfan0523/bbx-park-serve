package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 会议详情业务数据模型
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 9:01
 */
@Data
public class MeetingReserveDetailModel implements Serializable {
    @ApiModelProperty(value = "会议id.")
    private Long id;
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
    @ApiModelProperty(value = "预约人部门")
    private String reserveDepartment;
    @ApiModelProperty(value = "实际开始时间.")
    private Date realStartTime;
    @ApiModelProperty(value = "实际结束时间.")
    private Date realEndTime;
    @ApiModelProperty(value = "实际开始类型:1->参会人签到;2->会议到达开始时间")
    private Integer realStartType;
    @ApiModelProperty(value = "实际开始原因:1->参会人签到;2->会议到达开始时间")
    private String realStartReason;
    @ApiModelProperty(value = "实际结束类型:1->发起人结束会议;2->系统自动结束;3->会服结束会议")
    private Integer realEndType;
    @ApiModelProperty(value = "实际结束原因:1->发起人结束会议;2->系统自动结束;3->会服结束会议")
    private String realEndReason;
    @ApiModelProperty(value = "是否无效:1->是;0->否")
    private Integer inValidFlag;

    @ApiModelProperty(value = "操作人id.")
    private String operateUid;
    @ApiModelProperty(value = "操作人名称.")
    private String operateUname;
    @ApiModelProperty(value = "操作人工号.")
    private String operateStaffid;
    @ApiModelProperty(value = "操作时间.")
    private Date operateTime;
    @ApiModelProperty(value = "操作原因.")
    private String operateReason;

    @ApiModelProperty(value = "是否取消:1->是;0->否")
    private Integer cancelFlag;
    @ApiModelProperty(value = "取消时间")
    private Date cancelTime;

    @ApiModelProperty(value = "会议类型(ordinary->普通会议;video->视频会议)")
    private String meetingType;
    @ApiModelProperty(value = "会议摘要")
    private String summary;
    @ApiModelProperty(value = "参会方式(initiator->发起方(主会场);participator->参与方(分会场))")
    private String way;
    @ApiModelProperty(value = "涉密会议(1->是;0->否)")
    private Integer confidentiality;
    @ApiModelProperty(value = "会议密码")
    private String password;
    @ApiModelProperty(value = "支持排座(1->是;0->否)")
    private Integer rowSeat;
    @ApiModelProperty(value = "排座列表")
    private List<MeetingReserveSeatModel> seatList;
}
