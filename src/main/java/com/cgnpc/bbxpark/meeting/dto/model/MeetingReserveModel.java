
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会议预约业务数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:21
 */
@Data
public class MeetingReserveModel implements Serializable {

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

    @ApiModelProperty(value = "实际开始类型:1->参会人签到;2->会议到达开始时间")
    private Integer realStartType;

    @ApiModelProperty(value = "实际结束类型:1->发起人结束会议;2->系统自动结束;3->会服结束会议")
    private Integer realEndType;

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

    @ApiModelProperty(value = "签到人数量")
    private Long signCount;

    @ApiModelProperty(value = "会议状态:10->待开始;20->进行中;30->已结束")
    private Integer status;

    @ApiModelProperty(value = "是否取消:1->是;0->否")
    private Integer cancelFlag;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "空间位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "空间位置名称")
    private String spaceName;

    @ApiModelProperty(value = "签到标识")
    private Boolean signFlag;

}
