
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会议预约签到业务数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:22
 */
@Data
public class MeetingReserveSignModel implements Serializable {

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

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;


}
