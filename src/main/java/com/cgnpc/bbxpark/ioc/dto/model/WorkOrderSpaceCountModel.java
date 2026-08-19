package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏工单区域分布
 */
@Data
public class WorkOrderSpaceCountModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空间名称")
    private String name;

    @ApiModelProperty(value = "空间id")
    private Long id;

    @ApiModelProperty(value = "所属楼层物模型编码")
    private String sslcCode;

    @ApiModelProperty(value = "工单数量")
    private Integer totalNum;

}
