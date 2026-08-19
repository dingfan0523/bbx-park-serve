
package com.cgnpc.bbxpark.workorder.dto.param;



import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单流转列表参数模型
 */
@Data
public class WorkOrderRomanListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "工单id.")
    private Long workOrderId;

    @ApiModelProperty(value = "流转状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50）.")
    private Integer romanStatus;

    @ApiModelProperty(value = "操作人id.")
    private String operatorId;

    @ApiModelProperty(value = "操作人名称.")
    private String operatorName;

    @ApiModelProperty(value = "操作.")
    private String operator;

    @ApiModelProperty(value = "说明备注.")
    private String remark;

    @ApiModelProperty(value = "冗余字段1（操作：已指派）.")
    private String redundancyOne;

    @ApiModelProperty(value = "冗余字段2（操作人id）.")
    private String redundancyTwo;

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
    private List<Long> ids;
}
