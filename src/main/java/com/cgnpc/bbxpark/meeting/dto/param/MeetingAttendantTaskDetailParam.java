
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
/***
 * @Description 会服任务详情入参数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:45
 */
@Data
public class MeetingAttendantTaskDetailParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键id.")
    private Long id;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "会服任务id.")
    private Long taskId;

    @ApiModelProperty(value = "提交人id.")
    private String submitUid;

    @Length(max = 100)
    @ApiModelProperty(value = "提交人名称.")
    private String submitUname;

    @Length(max = 100)
    @ApiModelProperty(value = "提交人工号.")
    private String submitStaffid;

    @ApiModelProperty(value = "会服id.")
    private Long serviceId;

    @Length(max = 255)
    @ApiModelProperty(value = "会服名称.")
    private String serviceName;

    @Length(max = 255)
    @ApiModelProperty(value = "会服标准.")
    private String serviceStandard;

    @Length(max = 255)
    @ApiModelProperty(value = "会服提醒.")
    private String serviceWarn;

    @Length(max = 255)
    @ApiModelProperty(value = "会服说明.")
    private String serviceInstructions;

    @ApiModelProperty(value = "会服属性;(1->普通服务；2->默认服务).")
    private Integer serviceAttribute;

    @ApiModelProperty(value = "属性类型;(1->会议普通服务，2->视频会议调试服务，3->会议录音服务；4->会议排座服务，5->会议打印服务).")
    private Integer attributeType;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

}
