
package com.cgnpc.bbxpark.workorder.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value 工单统计模型
 * @author huangyongtao
 * @date 2025/4/17 17:05
 */
@Data
public class WorkOrderCountModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "告警总数")
    private Long totalNum;

    @ApiModelProperty(value = "完成的告警数量")
    private Long finishNum;

    @ApiModelProperty(value = "最多的工单来源")
    private String source;

    @ApiModelProperty(value = "最多的工单来源名称")
    private String sourceName;

    @ApiModelProperty(value = "最多的工单来源名称数量")
    private Long sourceNum;
}
