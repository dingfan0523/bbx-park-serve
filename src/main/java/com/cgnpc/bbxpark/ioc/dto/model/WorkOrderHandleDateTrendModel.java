package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏工单处理分析
 */
@Data
public class WorkOrderHandleDateTrendModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "时间（MM-dd）")
    private String time;

    @ApiModelProperty(value = "完成数量")
    private Integer totalNum;

    @ApiModelProperty(value = "平均分数")
    private Double avgScore;
}
