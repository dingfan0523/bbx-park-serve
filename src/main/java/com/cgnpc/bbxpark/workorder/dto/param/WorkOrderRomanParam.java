
package com.cgnpc.bbxpark.workorder.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单流转入参数据模型
 * @author lhy
 * @date 2024/08/26 09:53:42
 */
@Data
public class WorkOrderRomanParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Integer id;

    @NotNull
    @ApiModelProperty(value = "工单id.")
    private Long workOrderId;

    @ApiModelProperty(value = "流转状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50）.")
    private Integer romanStatus;

    @ApiModelProperty(value = "操作人id.")
    private String operatorId;

    @Length(max = 255)
    @ApiModelProperty(value = "操作人名称.")
    private String operatorName;

    @Length(max = 32)
    @ApiModelProperty(value = "操作.")
    private String operator;

    @Length(max = 255)
    @ApiModelProperty(value = "说明备注.")
    private String remark;

    @Length(max = 255)
    @ApiModelProperty(value = "冗余字段1（操作：已指派）.")
    private String redundancyOne;

    @Length(max = 255)
    @ApiModelProperty(value = "冗余字段2（操作人id）.")
    private String redundancyTwo;

    @Length(max = 255)
    @ApiModelProperty(value = "冗余字段3（操作人名称）.")
    private String redundancyThree;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private String updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "主键id集合.")
    private List<Integer> ids;
}
