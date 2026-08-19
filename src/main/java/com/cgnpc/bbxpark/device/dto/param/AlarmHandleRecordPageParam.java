package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AlarmHandleRecordPageParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4311195938824658344L;

    @ApiModelProperty(value = "id主键.")
    private Long id;

    @ApiModelProperty(value = "告警主键id.")
    private Long alarmId;

    @ApiModelProperty(value = "环节(1生成告警2告警确认3告警恢复).")
    private Integer link;

    @ApiModelProperty(value = "告警确认结果(1真实告警、2系统误报、3忽略告警).")
    private Integer alarmConfirmResult;

    @ApiModelProperty(value = "告警状态(1待确认、2已确认、3已结束).")
    private Integer alarmStatus;

    @ApiModelProperty(value = "告警来源(1规则管理、2场景编排).")
    private Integer alarmSource;

    @ApiModelProperty(value = "操作人.")
    private String operator;

    @ApiModelProperty(value = "操作时间.")
    private Date operateTime;

    @ApiModelProperty(value = "操作描述.")
    private String operateDesc;

}
