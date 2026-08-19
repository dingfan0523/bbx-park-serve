package com.cgnpc.bbxpark.workorder.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/4/15
 * @desc 物业工单统计返回值
 */
@Data
public class WorkOrderStatisticsModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "新产生处理工单数量")
    private Integer newWorkOrderCount;

    @ApiModelProperty(value = "遗留工单数量")
    private Integer leftoverWorkOrderCount;

    @ApiModelProperty(value = "合计工单数量")
    private Integer totalWorkOrderCount;

    @ApiModelProperty(value = "完成工单数量")
    private Integer completeWorkOrderCount;

    @ApiModelProperty(value = "未完成工单数量")
    private Integer incompleteWorkOrderCount;



    @ApiModelProperty(value = "异常结束报事报修工单数量")
    private Integer abnormalPersonWorkOrder;

    @ApiModelProperty(value = "异常结束告警工单数量")
    private Integer abnormalAlarmWorkOrder;

    @ApiModelProperty(value = "异常结束抄表计划工单数量")
    private Integer abnormalMeterPlanWorkOrder;


    @ApiModelProperty(value = "正常结束报事报修工单数量")
    private Integer regularPersonWorkOrder;

    @ApiModelProperty(value = "正常结束告警工单数量")
    private Integer regularAlarmWorkOrder;

    @ApiModelProperty(value = "正常结束抄表计划工单数量")
    private Integer regularMeterPlanWorkOrder;



    @ApiModelProperty(value = "特别关注新产生工单数量")
    private Integer attentionNewWorkOrder;

    @ApiModelProperty(value = "特别关注遗留工单数量")
    private Integer attentionLeftoverWorkOrder;

    @ApiModelProperty(value = "特别关注合计工单数量")
    private Integer attentionTotalWorkOrder;

    @ApiModelProperty(value = "特别关注已完成工单数量")
    private Integer attentionCompleteWorkOrder;

    @ApiModelProperty(value = "特别关注未完成工单数量")
    private Integer attentionIncompleteWorkOrder;


    // 无参构造函数，将所有字段初始化为0
    public WorkOrderStatisticsModel() {
        this.newWorkOrderCount = 0;
        this.leftoverWorkOrderCount = 0;
        this.totalWorkOrderCount = 0;
        this.completeWorkOrderCount = 0;
        this.incompleteWorkOrderCount = 0;
        this.abnormalPersonWorkOrder = 0;
        this.abnormalAlarmWorkOrder = 0;
        this.abnormalMeterPlanWorkOrder = 0;
        this.regularPersonWorkOrder = 0;
        this.regularAlarmWorkOrder = 0;
        this.regularMeterPlanWorkOrder = 0;
        this.attentionNewWorkOrder = 0;
        this.attentionLeftoverWorkOrder = 0;
        this.attentionTotalWorkOrder = 0;
        this.attentionCompleteWorkOrder = 0;
        this.attentionIncompleteWorkOrder = 0;
    }
}
