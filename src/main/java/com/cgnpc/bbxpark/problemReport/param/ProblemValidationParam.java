package com.cgnpc.bbxpark.problemReport.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/3/24
 * @desc 报事报修请求参数
 */
@Data
public class ProblemValidationParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4281780296969979158L;

    @ApiModelProperty(value = "报事报修id")
    @NotNull(message = "报事报修id不能为空")
    private Long problemId;

    @ApiModelProperty(value = "问题确认结果：1.转工单，2.无需处理")
    private Integer problemResultType;

//    @ApiModelProperty(value = "问题处理人id")
//    private Long processedPersonId;
//
//    @ApiModelProperty(value = "问题处理人名称")
//    private String processedPersonName;
//
//    @ApiModelProperty(value = "问题处理人工号")
//    private String processedPersonStaffid;

    @ApiModelProperty(value = "问题处理人电话")
    private String handleUmobile;

    @ApiModelProperty(value = "是否紧急：1:是，0:否")
    private Integer isUrgency;

    @ApiModelProperty(value = "问题确认备注")
    private String descr;

    @ApiModelProperty(value = "无需处理原因")
    private String cause;

    @ApiModelProperty(value = "评价")
    private String evaluation;

    @ApiModelProperty(value = "评分")
    private Integer score;


    @ApiModelProperty(value = "审核方式;0->否;1->是.")
    @NotNull(message = "审核方式不能为空")
    private Integer auditType = 1;

    @ApiModelProperty(value = "审核人id.")
    private String auditUid;

    @Length(max = 50)
    @ApiModelProperty(value = "审核人名称.")
    private String auditUname;

    @Length(max = 50)
    @ApiModelProperty(value = "审核人工号.")
    private String auditStaffid;

    @ApiModelProperty(value = "物业分组id.")
    @NotNull( message = "物业分组id不能为空")
    private Long scheduleId;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    @NotNull(message = "派单方式不能为空")
    private Integer dispatchType;

    @ApiModelProperty(value = "处理人id.")
    private String handleUid;

    @Length(max = 50)
    @ApiModelProperty(value = "处理人名称.")
    private String handleUname;

    @Length(max = 50)
    @ApiModelProperty(value = "处理人工号.")
    private String handleStaffid;
}
