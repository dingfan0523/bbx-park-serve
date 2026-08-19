package com.cgnpc.bbxpark.ioc.dto.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "设备点位模型")
public class DevicePointModel {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;
    @ApiModelProperty(value = "产品id")
    private Long productId;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @ApiModelProperty(value = "设备编码")
    private String deviceCode;
    @ApiModelProperty(value = "设备等级;（10：关键；20：重要；30：一般）.")
    private Integer deviceLevel;
    @ApiModelProperty(value = "设备空间id")
    private Long spaceId;
    @ApiModelProperty(value = "设备位置")
    private String spaceName;
    @ApiModelProperty(value = "设备分组编码")
    private String groupCode;
    @ApiModelProperty(value = "设备分组名称")
    private String groupName;
    @ApiModelProperty(value = "抄表类型(water->水表;electric->电表)")
    private String readingType;
    @ApiModelProperty(value = "抄表值")
    private BigDecimal readingValue;
    @ApiModelProperty(value = "抄表时间")
    private Date readingTime;
    @ApiModelProperty(value = "抄表图片")
    private String readingImg;
    @ApiModelProperty(value = "设备在线状态(0->离线;1->在线)")
    private String deviceStatus = "1";;
    @ApiModelProperty(value = "设备告警状态(0->否;1->是)")
    private String alarmStatus = "0";
    @ApiModelProperty(value = "存在报事报修工单(0->否;1->是)")
    private String workOrderStatus = "0";
    @ApiModelProperty(value = "设备点位数据(json)")
    private String positioning;
    @ApiModelProperty(value = "设备告警列表")
    private List<AlarmModel> alarms;
    @ApiModelProperty(value = "物联设备识别码")
    private String iotDeviceDn;
    @ApiModelProperty(value = "物理设备在线状态（1->在线;0->离线）")
    private Integer iotDeviceStatus;
}
