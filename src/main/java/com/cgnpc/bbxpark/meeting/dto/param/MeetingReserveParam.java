
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
/***
 * @Description 会议预约入参数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:33
 */
@Data
public class MeetingReserveParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @Length(max = 100)
    @ApiModelProperty(value = "会议主题.")
    private String reserveName;

    @Length(max = 100)
    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;

    @ApiModelProperty(value = "会议结束时间.")
    private Date endTime;

    @ApiModelProperty(value = "预约人id.")
    private String reserveUid;

    @Length(max = 50)
    @ApiModelProperty(value = "预约人名称.")
    private String reserveUname;

    @Length(max = 50)
    @ApiModelProperty(value = "预约人工号.")
    private String reserveStaffid;

    @ApiModelProperty(value = "会议实际开始时间.")
    private Date realStartTime;

    @ApiModelProperty(value = "会议实际结束时间.")
    private Date realEndTime;

    @ApiModelProperty(value = "会议无效;1：否；0：是.")
    private Integer inValid;

    @Length(max = 255)
    @ApiModelProperty(value = "会议无效备注.")
    private String inValidRemark;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "二维码内容")
    private String qrcode;

    @ApiModelProperty(value = "ordinary->普通会议;video->视频会议)")
    private String meetingType;
}
