
package com.cgnpc.bbxpark.ioc.dto.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工单主业务数据模型
 */
@Data
public class WorkOrderSimpleModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "工单编码.")
    private String code;

    @ApiModelProperty(value = "工单名称.")
    private String name;

    @ApiModelProperty(value = "工单来源（人工上报：person；告警触发：alarm）.")
    private String source;

    @ApiModelProperty(value = "工单来源（人工上报：person；告警触发：alarm）.")
    private String sourceDesc;

    @ApiModelProperty(value = "处理人名称.")
    private String processedPersonName;

    @ApiModelProperty(value = "处理人工号")
    private String processedPersonStaffid;

    @ApiModelProperty(value = "处理人id")
    private String processedPersonId;

    @ApiModelProperty(value = "工单状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50）")
    private Integer status;

    @ApiModelProperty(value = "工单状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50）")
    private String statusDesc;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    /** 空间名称 */
    @ApiModelProperty(value = "空间位置")
    private String spaceName;

    /** 空间名称 */
    @ApiModelProperty(value = "空间位置id")
    private String spaceId;

}
