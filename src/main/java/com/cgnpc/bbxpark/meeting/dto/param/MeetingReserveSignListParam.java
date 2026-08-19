
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/***
 * @Description 会议预约签到列表参数模型
 * @author huangyongtao
 * @date 2024/8/23 15:32
 */
@Data
public class MeetingReserveSignListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议预约id.")
    private Long reserveId;

    @ApiModelProperty(value = "签到人id.")
    private String signUid;

    @ApiModelProperty(value = "签到人名称.")
    private String signUname;

    @ApiModelProperty(value = "签到人工号.")
    private String signStaffid;

    @ApiModelProperty(value = "签到时间.")
    private Date signTime;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
