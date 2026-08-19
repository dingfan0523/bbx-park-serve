package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "告警模型")
public class AlarmModel {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "告警名称")
    private String alarmName;
    @ApiModelProperty(value = "告警级别")
    private String alarmLevel;
    @ApiModelProperty(value = "告警状态:1待确认、2已确认、3已结束")
    private Integer alarmStatus;
    @ApiModelProperty(value = "告警时间")
    private Date alarmFirstTime;
}
