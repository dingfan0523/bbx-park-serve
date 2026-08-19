package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AlarmIgnoreConfigListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3783146022941430720L;

    @ApiModelProperty(value = "id主键.")
    private Long id;

    @ApiModelProperty(value = "告警设备DN.")
    private String deviceDn;

    @ApiModelProperty(value = "忽略告警起始时间.")
    private Date ignoreStartTime;

    @ApiModelProperty(value = "忽略告警终止时间.")
    private Date ignoreEndTime;

    @ApiModelProperty(value = "启用状态(0启用、停用).")
    private Integer enableStatus;

    @ApiModelProperty(value = "设置人.")
    private String operator;

    @ApiModelProperty(value = "设置时间.")
    private Date operateTime;

    @ApiModelProperty(value = "id主键集合.")
    private List<Long> ids;
}
