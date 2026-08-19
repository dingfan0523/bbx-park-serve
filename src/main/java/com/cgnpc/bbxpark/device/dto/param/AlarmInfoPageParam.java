package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class AlarmInfoPageParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3698654368883129426L;

    @ApiModelProperty(value = "id主键.")
    private Long id;

    @ApiModelProperty(value = "告警名称.")
    private String alarmName;

    @ApiModelProperty(value = "告警级别.")
    private String alarmLevel;

    @ApiModelProperty(value = "告警唯一标识(设备DN+规则ID/场景节点ID).")
    private String alarmUnique;

    @ApiModelProperty(value = "告警设备DN.")
    private String deviceDn;

    @ApiModelProperty(value = "告警状态(1待确认、2已确认、3已结束).")
    private Integer alarmStatus;

    @ApiModelProperty(value = "告警确认结果(1真实告警、2系统误报、3忽略告警).")
    private Integer alarmConfirmResult;

    @ApiModelProperty(value = "首次告警时间.")
    private Date alarmFirstTime;

    @ApiModelProperty(value = "末次告警时间.")
    private Date alarmLastTime;

    @ApiModelProperty(value = "告警次数.")
    private Integer alarmCount;

    @ApiModelProperty(value = "告警来源(1规则管理、2场景编排).")
    private Integer alarmSource;

    @ApiModelProperty(value = "告警描述.")
    private String alarmDesc;

    @ApiModelProperty(value = "告警结束类型(1设备自动恢复告警2设备停用3系统误报4忽略告警).")
    private Integer alarmEndType;

    @ApiModelProperty(value = "告警恢复描述.")
    private String alarmRecoveryDesc;

}
