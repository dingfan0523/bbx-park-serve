
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会服任务详情业务数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:27
 */
@Data
public class MeetingAttendantTaskDetailModel implements Serializable {

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

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "是否可用(1->是;0->否)")
    private Integer valid = 1;

}
