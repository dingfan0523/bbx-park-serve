
package com.cgnpc.bbxpark.workorder.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工单流转业务数据模型
 * @author lhy
 * @date 2024/08/26 09:53:42
 */
@Data
public class WorkOrderRomanModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "工单id.")
    private Long workOrderId;

    @ApiModelProperty(value = "流转状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50）")
    private Integer romanStatus;

    @ApiModelProperty(value = "操作人id.")
    private String operatorId;

    @ApiModelProperty(value = "操作人工号.")
    private String operatorStaffid;

    @ApiModelProperty(value = "操作人名称.")
    private String operatorName;

    @ApiModelProperty(value = "操作.")
    private String operator;

    @ApiModelProperty(value = "操作值.")
    private String operatorValue;

    @ApiModelProperty(value = "说明备注.")
    private String remark;

    @ApiModelProperty(value = "冗余字段1（分配人id）.")
    private String redundancyOne;

    @ApiModelProperty(value = "冗余字段2（分配人名称）.")
    private String redundancyTwo;

    @ApiModelProperty(value = "冗余字段5(处理图片).")
    private String redundancyFive;

    @ApiModelProperty(value = "冗余字段3（分配人工号）.")
    private String redundancyThree;

    @ApiModelProperty(value = "冗余字段4（处理结果）.")
    private String redundancyFour;

    @ApiModelProperty(value = "处理人手机号.")
    private String operatorMobile;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "创建人工号.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新工号.")
    private String updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "展示：1，不展示：0")
    private String appExhibition;
}
