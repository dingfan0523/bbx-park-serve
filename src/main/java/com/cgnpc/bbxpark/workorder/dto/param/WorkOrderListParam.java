
package com.cgnpc.bbxpark.workorder.dto.param;



import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单主列表参数模型
 */
@Data
public class WorkOrderListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Integer id;

    @ApiModelProperty(value = "工单名称.")
    private String name;

    @ApiModelProperty(value = "工单编码.")
    private String code;

    @ApiModelProperty(value = "工单类型(报修工单:repair).")
    private String type;

    @ApiModelProperty(value = "工单来源（人工上报：person；告警触发：alarm）.")
    private String source;

    @ApiModelProperty(value = "工单描述.")
    private String desc;

    @ApiModelProperty(value = "问题图片.")
    private String problemPictureUrl;

    @ApiModelProperty(value = "处理人名称.")
    private String processedPersonName;

    @ApiModelProperty(value = "处理人id.")
    private String processedPersonId;

    @ApiModelProperty(value = "处理图片.")
    private String processedPictureUrl;

    @ApiModelProperty(value = "处理描述.")
    private String processedDesc;

    @ApiModelProperty(value = "工单状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50.")
    private Integer status;

    @ApiModelProperty(value = "指派人id.")
    private String assignPersonId;

    @ApiModelProperty(value = "指派人id名称.")
    private String assignPersonName;

    @ApiModelProperty(value = "退回原因.")
    private String returnReason;

    @ApiModelProperty(value = "关闭原因.")
    private String closeReason;

    @ApiModelProperty(value = "满意度.")
    private Integer satisfaction;

    @ApiModelProperty(value = "评价.")
    private String evaluateContent;

    @ApiModelProperty(value = "告警时间.")
    private Date alarmTime;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "删除状态(1->未删;0->已删).")
    private Boolean deleted;

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人id.")
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "主键集合.")
    private List<Integer> ids;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    private Integer dispatchType;

    @ApiModelProperty(value = "分配人名称.")
    private String allotUname;

    @ApiModelProperty(value = "是否转派.")
    private Boolean transferFlag;
}
