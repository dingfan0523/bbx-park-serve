package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AlarmKafkaInfoPageParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3945627590408469908L;

    @ApiModelProperty(value = "id主键.")
    private Long id;

    @ApiModelProperty(value = "告警来源(1规则管理、2场景编排).")
    private Integer alarmSource;

    @ApiModelProperty(value = "规则ID/场景节点ID.")
    private String sourceId;

    @ApiModelProperty(value = "动作(1产生2恢复).")
    private Integer action;

    @ApiModelProperty(value = "告警设备DN.")
    private String deviceDn;

    @ApiModelProperty(value = "告警名称.")
    private String alarmName;

    @ApiModelProperty(value = "告警等级.")
    private String alarmLevel;

    @ApiModelProperty(value = "告警时间.")
    private Date alarmTime;

    @ApiModelProperty(value = "告警描述.")
    private String alarmDesc;

}
