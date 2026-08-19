
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议服务评价业务数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:27
 */
@Data
public class MeetingAttendantEvaluateModel implements Serializable {

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

}
