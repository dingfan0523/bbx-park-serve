
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会议签到业务数据模型
 * @author huangyongtao
 * @date 2024/8/23 15:22
 */
@Data
public class MeetingSignModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "类型")
    private Integer type;
    @ApiModelProperty(value = "签到人部门")
    private String signDepartment;
    @ApiModelProperty(value = "签到人id.")
    private String signUid;
    @ApiModelProperty(value = "签到人名称.")
    private String signUname;
    @ApiModelProperty(value = "签到人工号.")
    private String signStaffid;
    @ApiModelProperty(value = "签到时间.")
    private Date signTime;
    @ApiModelProperty(value = "会前邀请:1->是;0->否.")
    private Integer invited;
}
