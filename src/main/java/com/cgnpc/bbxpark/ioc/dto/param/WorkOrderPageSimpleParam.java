
package com.cgnpc.bbxpark.ioc.dto.param;


import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单主分页参数模型
 */
@Data
public class WorkOrderPageSimpleParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "工单名称.")
    private String name;

    @ApiModelProperty(value = "工单编码.")
    private String code;

    @ApiModelProperty(value = "工单来源（人工上报：person；告警触发：alarm）.")
    private String source;

    @ApiModelProperty(value = "工单来源集合")
    private List<String> sources;

    @ApiModelProperty(value = "工单状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50.")
    private Integer status;

    @ApiModelProperty(value = "生成开始时间.")
    // 接收前端参数时的转换格式（核心）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "生成结束时间.")
    // 接收前端参数时的转换格式（核心）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "空间位置id")
    private String spaceId;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "支路类型：water->水;electricity->电")
    private String branchType;

    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    @ApiModelProperty(value = "设备id集合")
    private List<Long> deviceIdList;
}
