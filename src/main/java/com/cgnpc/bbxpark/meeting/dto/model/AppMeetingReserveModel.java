
package com.cgnpc.bbxpark.meeting.dto.model;

import com.cgnpc.bbxpark.device.dto.model.AlarmInfoModel;
import com.cgnpc.bbxpark.space.dto.model.SpaceManagerModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会议预约业务数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:21
 */
@Data
public class AppMeetingReserveModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议主题.")
    private String reserveName;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

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

    @ApiModelProperty(value = "空间id")
    private Long spaceId;

    @ApiModelProperty(value = "空间位置名称")
    private String spaceName;

    @ApiModelProperty(value = "会议状态:10->待开始;20->进行中;30->已结束")
    private Integer status;

    @ApiModelProperty(value = "是否取消:1->是;0->否")
    private Integer cancelFlag;

    @ApiModelProperty(value = "签到人数量")
    private Long signCount = 0L;

    @ApiModelProperty(value = "草稿(1->是;0->否)")
    private Integer draft;

    @ApiModelProperty(value = "失败原因")
    private String failReason;

    @ApiModelProperty(value = "设备数量")
    private Long deviceNum= 0L;

    @ApiModelProperty(value = "设备告警信息")
    private List<AlarmInfoModel> alarmInfoModels;

    @ApiModelProperty(value = "会议室运维人员")
    private SpaceManagerModel spaceManagerModel;
}
