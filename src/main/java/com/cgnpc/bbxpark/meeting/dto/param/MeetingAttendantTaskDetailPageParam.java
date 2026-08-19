
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 会服任务详情分页参数模型
 * @author huangyongtao
 * @date 2024/12/23 15:44
 */
@Data
public class MeetingAttendantTaskDetailPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "会服任务id.")
    private Long taskId;

    @ApiModelProperty(value = "提交人id.")
    private String submitUid;

    @ApiModelProperty(value = "提交人名称.")
    private String submitUname;

    @ApiModelProperty(value = "提交人工号.")
    private String submitStaffid;

    @ApiModelProperty(value = "会服id.")
    private Long serviceId;

    @ApiModelProperty(value = "会服名称.")
    private String serviceName;

    @ApiModelProperty(value = "会服标准.")
    private String serviceStandard;

    @ApiModelProperty(value = "会服提醒.")
    private String serviceWarn;

    @ApiModelProperty(value = "会服说明.")
    private String serviceInstructions;

    @ApiModelProperty(value = "会服属性;(1->普通服务；2->默认服务).")
    private Integer serviceAttribute;

    @ApiModelProperty(value = "属性类型;(1->会议普通服务，2->视频会议调试服务，3->会议录音服务；4->会议排座服务，5->会议打印服务).")
    private Integer attributeType;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;


}
