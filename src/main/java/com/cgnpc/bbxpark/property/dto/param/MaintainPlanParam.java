package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 维保计划管理入参数据模型
 * @author huangyongtao
 * @date 2025/10/16 15:25
 */
@Data
public class MaintainPlanParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @Length(max = 255)
    @ApiModelProperty(value = "计划名称.")
    @NotNull(message = "计划名称不能为空")
    private String planName;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status;

    @ApiModelProperty(value = "审核方式;0->否;1->是.")
    @NotNull(message = "审核方式不能为空")
    private Integer auditType;

    @ApiModelProperty(value = "审核人id.")
    private String auditUid;

    @Length(max = 50)
    @ApiModelProperty(value = "审核人名称.")
    private String auditUname;

    @Length(max = 50)
    @ApiModelProperty(value = "审核人工号.")
    private String auditStaffid;

    @ApiModelProperty(value = "物业分组id.")
    @NotNull(message = "物业分组不能为空")
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

    @ApiModelProperty(value = "计划的周期;1:周期；2：单次.")
    @NotNull(message = "计划周期不能为空")
    private Integer planPeriod;

    @Length(max = 10)
    @ApiModelProperty(value = "周期类型;year：年；quarter：季度；month：月；week：周；day：日.")
    private String periodType;

    @ApiModelProperty(value = "周期的标识;1:第一天；2：最后一天.")
    private Integer periodSign;

    @ApiModelProperty(value = "周期的开始时间.")
    @NotNull(message = "周期开始时间不能为空")
    private Date periodStartTime;

    @ApiModelProperty(value = "计划开始的时间.")
    private Date planStartTime;

    @ApiModelProperty(value = "计划时长;单位小时.")
    private Integer planDuration;

    @Length(max = 255)
    @ApiModelProperty(value = "计划描述.")
    private String remark;

    @ApiModelProperty(value = "计划任务集合")
    List<MaintainPlanTaskGroupParam> taskGroupParams;

    @ApiModelProperty(value = "计划材料集合")
    List<MaintainMaterialParam> materialParams;
}
