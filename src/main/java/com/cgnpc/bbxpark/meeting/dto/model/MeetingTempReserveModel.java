
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会议临时预约业务数据模型
 * @author huangyongtao
 * @date 2025/1/6 15:49
 */
@Data
public class MeetingTempReserveModel implements Serializable {

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

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

}
