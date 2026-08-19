
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 会议临时预约列表参数模型
 * @author huangyongtao
 * @date 2025/1/6 15:53
 */
@Data
public class MeetingTempReserveListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @ApiModelProperty(value = "会议室id集合")
    private List<Long> roomIdList;

    @ApiModelProperty(value = "会议室名称.")
    private String roomName;

    @ApiModelProperty(value = "会议开始时间.")
    private Date startTime;

    @ApiModelProperty(value = "会议结束时间.")
    private Date endTime;

    @ApiModelProperty(value = "当前时间时间.")
    private Date localTime;

    @ApiModelProperty(value = "预约人id.")
    private String reserveUid;

    @ApiModelProperty(value = "预约人名称.")
    private String reserveUname;

    @ApiModelProperty(value = "预约人工号.")
    private String reserveStaffid;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
