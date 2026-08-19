
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会服人员任务业务数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:25
 */
@Data
public class MeetingAttendantTaskPageModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议id.")
    private Long reserveId;

    @ApiModelProperty(value = "会议名称.")
    private String reserveName;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会服类型;(1->会前布置；2->会中呼叫；3->会后清洁).")
    private Integer serviceType;

    @ApiModelProperty(value = "会服状态;(1->未处理；2->已确认； 3->已完成).")
    private Integer serviceStatus;

    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;

    @ApiModelProperty(value = "会议结束时间.")
    private Date endTime;

    @ApiModelProperty(value = "最新呼叫时间")
    private Date callTime;

    @ApiModelProperty(value = "呼叫人员名称")
    private String callName;

    @ApiModelProperty(value = "呼叫时长（1分钟、5分钟、10分钟、15分钟、30分钟、1小时、2小时前）")
    private  Integer callDuration;

    @ApiModelProperty(value = "清扫(1->是;0->否)")
    private Integer swept;

    @ApiModelProperty(value = "使用中(1->是;0->否)")
    private Integer used;

    @ApiModelProperty(value = "会议类型(ordinary->普通会议;video->视频会议)")
    private String meetingType;

    @ApiModelProperty(value = "保留音频(1->是;0->否)")
    private Integer retainedAudio;

}
