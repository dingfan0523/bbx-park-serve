
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会议预约会服业务数据模型
 * @author huangyongtao
 * @date 2025/1/8 15:23
 */
@Data
public class MeetingReserveAttendantModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @ApiModelProperty(value = "会议主题.")
    private String reserveName;

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

    @ApiModelProperty(value = "会议状态:10->待开始;20->进行中;30->已结束")
    private Integer status;

    @ApiModelProperty(value = "会服留言")
    private String serveRemark;

    @ApiModelProperty(value = "会议类型(ordinary->普通会议;video->视频会议)")
    private String meetingType;

    @ApiModelProperty(value = "会议是否即将开始提醒标识")
    private Boolean startFlag = false;

    @ApiModelProperty(value = "会服状态;(1->未处理；2->已确认； 3->已完成).")
    private Integer serviceStatus;

    @ApiModelProperty(value = "会服是否有效;(1->有效；0->无效).")
    private Integer serviceValid;

    @ApiModelProperty(value = "会服任务id")
    private Long taskId;

    @ApiModelProperty(value = "会服列表")
    private List<MeetingAttendantTaskDetailModel> taskDetailModelList;

}
