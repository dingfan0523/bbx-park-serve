
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
/***
 * @Description 会议服务评价入参数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:37
 */
@Data
public class MeetingAttendantEvaluateParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "会议预约id.")
    private Long reserveId;

    @ApiModelProperty(value = "分数.")
    private Integer score;

    @Length(max = 255)
    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "操作人id.")
    private String operateUid;

    @Length(max = 100)
    @ApiModelProperty(value = "操作人名称.")
    private String operateUname;

    @Length(max = 100)
    @ApiModelProperty(value = "操作人工号.")
    private String operateStaffid;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
