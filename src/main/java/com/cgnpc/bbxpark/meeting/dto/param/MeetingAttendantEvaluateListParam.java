
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议服务评价列表参数模型
 * @author huangyongtao
 * @date 2024/12/23 15:35
 */
@Data
public class MeetingAttendantEvaluateListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会议预约id.")
    private Long reserveId;

    @ApiModelProperty(value = "分数.")
    private Integer score;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "操作人id.")
    private String operateUid;

    @ApiModelProperty(value = "操作人名称.")
    private String operateUname;

    @ApiModelProperty(value = "操作人工号.")
    private String operateStaffid;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
