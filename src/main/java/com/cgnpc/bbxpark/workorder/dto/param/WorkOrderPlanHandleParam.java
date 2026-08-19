package com.cgnpc.bbxpark.workorder.dto.param;



import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/***
 * @Description 工单抄表计划处理参数
 * @author huangyongtao
 * @date 2025/3/31 11:18
 */
@Data
public class WorkOrderPlanHandleParam implements Serializable {
    @ApiModelProperty(value = "工单id.")
    @NotNull(message = "工单id不能为空")
    private Long id;

    @ApiModelProperty(value = "处理描述(备注).")
    private String processedDesc;

    @ApiModelProperty(value = "超时原因（1：工单生成为节假日；2：表有故障，等待 报修；3：个人原因（请假））")
    private Integer outReason;

    @ApiModelProperty(value = "抄表设备.")
    private List<WorkOrderDeviceParam> workOrderDeviceList;

    @ApiModelProperty(value = "任务集合.")
    private List<WorkTaskParam> workTaskParams;

    @ApiModelProperty(value = "任务项集合.")
    private List<WorkTaskItemParam> workTaskItemParams;

    @ApiModelProperty(value = "材料集合.")
    private List<WorkMaterialParam> workMaterialParams;
}
