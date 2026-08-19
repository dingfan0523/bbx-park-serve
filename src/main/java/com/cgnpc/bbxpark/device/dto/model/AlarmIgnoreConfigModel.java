package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警忽略配置业务数据模型
 */
@Data
public class AlarmIgnoreConfigModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3625089642341391603L;

    @ApiModelProperty(value = "id主键.")
    private Long id;

    @ApiModelProperty(value = "告警id.")
    private Long alarmInfoId;

    @ApiModelProperty(value = "告警设备DN.")
    private String deviceDn;

    @ApiModelProperty(value = "忽略类型(1-本次,2-时间段)")
    private String ignoreType;

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


}
