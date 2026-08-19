package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/***
 * @Description 设备告警统计模型
 * @author huangyongtao
 * @date 2025/4/17 17:05
 */
@Data
public class AlarmInfoCountModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "告警总数")
    private Long totalNum;

    @ApiModelProperty(value = "完成的告警数量")
    private Long finishNum;

    @ApiModelProperty(value = "最多的告警名称")
    private String alarmName;

    @ApiModelProperty(value = "最多的告警名称数量")
    private Long alarmNameNum;

    @ApiModelProperty(value = "未结束的告警数量")
    private Long noHandleNum;

    @ApiModelProperty(value = "未结束的告警时间")
    private Date alarmTime;
}
