
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 移动端-会议详情
 */
@Data
public class AppMeetingReserveDetailModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "会议室id.")
    private Long roomId;
    @ApiModelProperty(value = "会议室名称.")
    private String roomName;
    @ApiModelProperty(value = "会议室提醒内容")
    private String warnContent;
    @ApiModelProperty(value = "会议主题.")
    private String reserveName;
    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;
    @ApiModelProperty(value = "会议结束时间.")
    private Date endTime;
    @ApiModelProperty(value = "发起人id.")
    private String reserveUid;
    @ApiModelProperty(value = "发起人名称.")
    private String reserveUname;
    @ApiModelProperty(value = "发起人工号.")
    private String reserveStaffid;
    @ApiModelProperty(value = "发起人手机号.")
    private String reserveMobile;
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "空间位置名称")
    private String spaceName;
    @ApiModelProperty(value = "会议状态:10->待开始;20->进行中;30->已结束")
    private Integer status;
    @ApiModelProperty(value = "是否必须签到(1->必须签到;1->自愿签到)")
    private Integer mustSignFlag;
    @ApiModelProperty(value = "是否允许代签(1->允许;0->不允许)")
    private Integer behalfSignFlag;
    @ApiModelProperty(value = "是否允许补签(1->允许;0->不允许)")
    private Integer replenishSignFlag;
    @ApiModelProperty(value = "签到人数")
    private Integer signCount;
    @ApiModelProperty(value = "规则最后修改时间")
    private Date lastRuleTime;
    @ApiModelProperty(value = "会议签到列表")
    private List<MeetingSignModel> signList;
    @ApiModelProperty(value = "会议附件")
    private List<MeetingReserveFileModel> fileList;
    @ApiModelProperty(value = "是否系统结束")
    private Boolean sysFinishFlag = false;
    @ApiModelProperty(value = "是否发起人")
    private Boolean reserveFlag = false;
    @ApiModelProperty(value = "是否已签到标识")
    private Boolean signFlag = false;
    @ApiModelProperty(value = "涉密会议(1->是;0->否)")
    private Integer confidentiality;
    @ApiModelProperty(value = "排座列表")
    private List<MeetingReserveSeatModel> seatList;
    @ApiModelProperty(value = "会服列表")
    private List<MeetingAttendantTaskModel> taskList;
    @ApiModelProperty(value = "会服详情列表")
    private List<MeetingAttendantTaskDetailModel> taskDetailList;
    @ApiModelProperty(value = "排座(1->支持;0->不支持)")
    private Integer rowSeat;
    @ApiModelProperty(value = "打印(1->支持;0->不支持)")
    private Integer print;
    @ApiModelProperty(value = "会服状态;(1->未处理；2->已确认； 3->已完成).")
    private Integer serviceStatus = 1;
    @ApiModelProperty(value = "会服id")
    private Long taskId;
    @ApiModelProperty(value = " 会服类型;(1->会前布置；2->会中呼叫；3->会后清洁).")
    private Integer serviceType;
    @ApiModelProperty(value = "会服处理人id.")
    private String handleUid;
    @ApiModelProperty(value = "会服处理人名称.")
    private String handleUname;
    @ApiModelProperty(value = "会服处理人工号.")
    private String handleStaffid;
    @ApiModelProperty(value = "会服处理时间.")
    private Date handleTime;
    @ApiModelProperty(value = "会议是否即将开始提醒标识")
    private Boolean startFlag = false;

    @ApiModelProperty(value = "预约人部门id.")
    private String reserveDepartmentId;

    @ApiModelProperty(value = "预约人部门.")
    private String reserveDepartment;

    @ApiModelProperty(value = "会议摘要")
    private String summary;

    @ApiModelProperty(value = "会服留言")
    private String serveRemark;

    @ApiModelProperty(value = "参会人数")
    private Integer participantNumber;

    @ApiModelProperty(value = "实际参会人数")
    private Integer realParticipantNumber;

    @ApiModelProperty(value = "ordinary->普通会议;video->视频会议)")
    private String meetingType;

    @ApiModelProperty(value = "需要密码(1->是;0->否)")
    private Integer needPassword;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "保留音频(1->是;0->否)")
    private Integer retainedAudio;

    @ApiModelProperty(value = "参会方式(initiator->发起方(主会场);participator->参与方(分会场))")
    private String way;

    @ApiModelProperty(value = "会议实际开始时间.")
    private Date realStartTime;

    @ApiModelProperty(value = "会议实际结束时间.")
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

    @ApiModelProperty(value = "操作人id")
    private String operateUid;

    @ApiModelProperty(value = "操作人名称")
    private String operateUname;

    @ApiModelProperty(value = "操作人工号")
    private String operateStaffid;

    @ApiModelProperty(value = "操作人时间")
    private Date operateTime;

    @ApiModelProperty(value = "操作人原因")
    private String operateReason;

    @ApiModelProperty(value = "是否取消:1->是;0->否")
    private Integer cancelFlag;

    @ApiModelProperty(value = "是否显示重置结束时间按钮")
    private Boolean resetEndTimeFlag = true;

}
