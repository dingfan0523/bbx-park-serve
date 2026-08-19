
package com.cgnpc.bbxpark.property.dto.param;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class InventoryPlanParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3361529044177503834L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @Length(max = 255)
    @ApiModelProperty(value = "计划名称.")
    private String planName;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status = 1;

    @ApiModelProperty(value = "审核方式;0->否;1->是.")
    private Integer auditType = 0;

    @ApiModelProperty(value = "审核人id.")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String auditUid;

    @Length(max = 50)
    @ApiModelProperty(value = "审核人名称.")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String auditUname;

    @Length(max = 50)
    @ApiModelProperty(value = "审核人工号.")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String auditStaffid;

    @ApiModelProperty(value = "物业分组id.")
    private Long scheduleId;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    private Integer dispatchType;

    @ApiModelProperty(value = "处理人id.")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String handleUid;

    @Length(max = 50)
    @ApiModelProperty(value = "处理人名称.")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String handleUname;

    @Length(max = 50)
    @ApiModelProperty(value = "处理人工号.")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String handleStaffid;

    @ApiModelProperty(value = "计划的周期;1:周期；2：单次.")
    private Integer planPeriod = 1;

    @Length(max = 10)
    @ApiModelProperty(value = "周期类型;year：年；quarter：季度；month：月；week：周；day：日.")
    private String periodType;

    @ApiModelProperty(value = "周期的标识;1:第一天；2：最后一天.")
    private Integer periodSign;

    @ApiModelProperty(value = "周期的开始时间.")
    private Date periodStartTime;

    @ApiModelProperty(value = "计划开始的时间.")
    private Date planStartTime;

    @ApiModelProperty(value = "计划时长;单位小时.")
    private Integer planDuration;

    @Length(max = 255)
    @ApiModelProperty(value = "计划描述.")
    private String remark;

    @ApiModelProperty(value = "盘点计划明细信息.")
    private List<InventoryItemParam> inventoryItemParams;
}
