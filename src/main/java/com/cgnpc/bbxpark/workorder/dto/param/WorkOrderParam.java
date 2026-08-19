
package com.cgnpc.bbxpark.workorder.dto.param;



import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单主入参数据模型
 */
@Data
public class WorkOrderParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @NotBlank
    @Length(max = 30,message = "工单名称不超过30个字符")
    @ApiModelProperty(value = "工单名称.")
    private String name;

    @NotBlank
    @Length(max = 30,message = "工单编码不超过30个字符")
    @ApiModelProperty(value = "工单编码.")
    private String code;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "工单类型(报修工单:repair).")
    private String type;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "工单来源（人工上报：person；告警触发：alarm）.")
    private String source;

    @Length(max = 200,message = "工单描述不超过200个字符")
    @ApiModelProperty(value = "工单描述.")
    private String remark;

    @Length(max = 512)
    @ApiModelProperty(value = "问题图片集合.")
    private List<String> problemPictureUrlList;

    @Length(max = 64)
    @ApiModelProperty(value = "处理人名称.")
    private String processedPersonName;

    @ApiModelProperty(value = "处理人id.")
    private String processedPersonId;

    @Length(max = 512)
    @ApiModelProperty(value = "处理图片集合.")
    private List<String> processedPictureUrlList;

    @Length(max = 255)
    @ApiModelProperty(value = "处理描述.")
    private String processedDesc;

    @ApiModelProperty(value = "工单状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50.")
    private Integer status;

    @Length(max = 255)
    @ApiModelProperty(value = "退回原因.")
    private String returnReason;

    @Length(max = 255)
    @ApiModelProperty(value = "关闭原因.")
    private String closeReason;

    @ApiModelProperty(value = "满意度.")
    private Integer satisfaction;

    @Length(max = 255)
    @ApiModelProperty(value = "评价.")
    private String evaluateContent;

    @ApiModelProperty(value = "告警时间.")
    private Date alarmTime;

    @ApiModelProperty(value = "设备集合.")
    private List<WorkOrderDeviceParam> workOrderDeviceParams;

    @ApiModelProperty(value = "转派人id")
    private String transferUid;

    @ApiModelProperty(value = "转派人名称.")
    private String transferUname;

    @ApiModelProperty(value = "转派人工号.")
    private String transferStaffid;

    @ApiModelProperty(value = "审核人id.")
    private String auditUid;

    @ApiModelProperty(value = "审核人名称.")
    private String auditUname;

    @ApiModelProperty(value = "审核人工号.")
    private String auditStaffid;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    private Integer dispatchType;

    @ApiModelProperty(value = "分配人名称.")
    private String allotUname;

    @ApiModelProperty(value = "是否转派.")
    private Boolean transferFlag;

}
