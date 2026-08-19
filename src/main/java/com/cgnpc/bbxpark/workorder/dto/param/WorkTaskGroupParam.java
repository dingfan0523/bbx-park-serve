
package com.cgnpc.bbxpark.workorder.dto.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 工单任务业务数据模型
 * @author huangyongtao
 * @date 2025/11/7 17:00
 */
@Data
public class WorkTaskGroupParam implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务名称.")
    private String name;

    @ApiModelProperty(value = "任务组.")
    private Integer taskGroup = 0;

    @ApiModelProperty(value = "类型.")
    private Integer type;

    @ApiModelProperty(value = "任务数量.")
    private Long taskNum;

    @ApiModelProperty(value = "任务项数量.")
    private Long taskItemNum;

    @ApiModelProperty(value = "任务集合.")
    private List<WorkTaskParam> workTaskParams;

    @ApiModelProperty(value = "任务项集合.")
    private List<WorkTaskItemParam> workTaskItemParams;
}
