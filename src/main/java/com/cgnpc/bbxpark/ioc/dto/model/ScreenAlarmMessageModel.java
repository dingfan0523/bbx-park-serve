package com.cgnpc.bbxpark.ioc.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel(value = "大屏告警消息通知接口")
@Data
public class ScreenAlarmMessageModel {
    @ApiModelProperty(value = "告警id")
    private Long id;
    @ApiModelProperty(value = "告警名称.")
    private String alarmName;
    @ApiModelProperty(value = "告警级别.字典：AlarmInfoLevel ")
    private String alarmLevel;
    @ApiModelProperty(value = "告警设备")
    private String alarmDevice;
    @ApiModelProperty(value = "首次告警时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date alarmFirstTime;
    @ApiModelProperty(value = "告警描述.")
    private String alarmDesc;
    @ApiModelProperty(value = "空间位置.")
    private String spaceAddr;
    @ApiModelProperty(value = "关联摄像头设备集合")
    private List<RelationDevice> relationDevices = new ArrayList<>();

    @Data
    public static class RelationDevice{
        @ApiModelProperty(value = "关联设备dn.")
        private String iotDeviceDn;
        @ApiModelProperty(value = "关联设备名称.")
        private String relationDeviceName;
        @ApiModelProperty(value = "设备位置")
        private String spacesName;
    }
}
