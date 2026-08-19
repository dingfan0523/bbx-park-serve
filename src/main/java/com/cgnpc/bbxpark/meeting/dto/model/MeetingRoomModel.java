
package com.cgnpc.bbxpark.meeting.dto.model;

import com.cgnpc.bbxpark.meeting.dto.model.common.CommonInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 会议室业务数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:23
 */
@Data
public class MeetingRoomModel extends CommonInfo implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会议室容量.")
    private Integer roomVolume;

    @ApiModelProperty(value = "片区id")
    private String areaId;

    @ApiModelProperty(value = "空间位置名称(集团系统)")
    private String areaName;

    @ApiModelProperty(value = "提醒内容")
    private String warnContent;

    @ApiModelProperty(value = "排座(1->支持;0->不支持)")
    private Integer rowSeat;

    @ApiModelProperty(value = "打印(1->支持;0->不支持)")
    private Integer print;

    @ApiModelProperty(value = "使用中(1->是;0->否)")
    private Integer used;

    @ApiModelProperty(value = "会议室图片.")
    private String imageUrl;

    @ApiModelProperty(value = "是否清扫(1->是;0->否)")
    private Integer swept;

    @ApiModelProperty(value = "呼叫中(1->是;0->否)")
    private Integer calling;

    @ApiModelProperty(value = "会议预约集合")
    private List<MeetingReserveModel> meetingReserveModels;

    @ApiModelProperty(value = "会议临时预约集合")
    private List<MeetingTempReserveModel> meetingTempReserveModel;
}
