
package com.cgnpc.bbxpark.meeting.dto.model;

import com.cgnpc.bbxpark.meeting.dto.model.common.CommonInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 会议室详情业务数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:23
 */
@Data
public class MeetingRoomDetailModel extends CommonInfo implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会议室图片")
    private String imageUrl;

    @ApiModelProperty(value = "会议室容量.")
    private Integer roomVolume;

    @ApiModelProperty(value = "空间位置名称(集团系统)")
    private String areaName;

    @ApiModelProperty(value = "提醒内容")
    private String warnContent;

    @ApiModelProperty(value = "排座(0->支持;1->不支持)")
    private Integer rowSeat;

    @ApiModelProperty(value = "打印(0->支持;1->不支持))")
    private Integer print;

    @ApiModelProperty(value = "会服人员集合")
    private List<SimpleMeetingAttendantModel> attendantList;
}
