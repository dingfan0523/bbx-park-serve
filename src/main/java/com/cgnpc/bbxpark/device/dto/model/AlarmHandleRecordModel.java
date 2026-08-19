package com.cgnpc.bbxpark.device.dto.model;

import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警处置记录业务数据模型
 */
@Data
public class AlarmHandleRecordModel extends BaseExEntity implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4031581630876351087L;

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

    @ApiModelProperty(value = "操作人id.")
    private String operator;

    @ApiModelProperty(value = "操作人工号")
    private String operatorStaffid;

    @ApiModelProperty(value = "操作人账号.")
    private String account;
    @ApiModelProperty(value = "操作人名称.")
    private String operatorName;

    @ApiModelProperty(value = "操作时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date operateTime;

    @ApiModelProperty(value = "操作备注.")
    private String operateDesc;
    @ApiModelProperty(value = "是否转工单.Y-是；N-否")
    private String work;
    @ApiModelProperty(value = "操作描述.")
    private String linkDesc;
}
