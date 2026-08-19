package com.cgnpc.bbxpark.device.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 告警业务压缩业务数据模型
 */
@Data
public class AlarmInfoModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3673980461963912994L;

    @ApiModelProperty(value = "id主键.")
    private Long id;

    @ApiModelProperty(value = "告警名称.")
    private String alarmName;
    /**
     *告警级别.
     **/
    @ApiModelProperty(value = "告警级别.字典：AlarmInfoLevel ")
    private String alarmLevel;

    @ApiModelProperty(value = "告警唯一标识(设备DN+规则ID/场景节点ID).")
    private String alarmUnique;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "告警设备DN.")
    private String deviceDn;

    @ApiModelProperty(value = "告警状态(1待确认、2已确认、3已结束)-字典：AlarmInfoStatus.")
    private Integer alarmStatus;

    @ApiModelProperty(value = "告警确认结果(1真实告警、2系统误报、3忽略告警).-字典：alarmConfirmResult")
    private Integer alarmConfirmResult;

    @ApiModelProperty(value = "首次告警时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date alarmFirstTime;

    @ApiModelProperty(value = "末次告警时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date alarmLastTime;

    @ApiModelProperty(value = "告警次数.")
    private Integer alarmCount;

    @ApiModelProperty(value = "告警来源(1规则管理、2场景编排).")
    private Integer alarmSource;

    @ApiModelProperty(value = "告警描述.")
    private String alarmDesc;

    @ApiModelProperty(value = "告警结束类型(1设备自动恢复告警2设备停用3系统误报4忽略告警5手动结束).")
    private Integer alarmEndType;

    @ApiModelProperty(value = "告警恢复描述.")
    private String alarmRecoveryDesc;

    @ApiModelProperty(value = "告警类型.")
    private String alarmRuleType;

    @ApiModelProperty(value = "告警操作记录集合.")
    private List<AlarmHandleRecordModel> handleRecordModelList;

    @ApiModelProperty(value = "空间位置.")
    private String spaceAddr;

    @ApiModelProperty(value = "空间位置id")
    private String spaceAddrId;

    @ApiModelProperty(value = "告警设备")
    private String alarmDevice;

    @ApiModelProperty(value = "告警设备集合")
    private List<AlarmDeviceModel> alarmDeviceList;

    @ApiModelProperty(value = "告警结束时间")
    private Date alarmEndTime;

    @ApiModelProperty(value = "告警是否转工单(0否1是)")
    private Short wrokOrder;

    @ApiModelProperty(value = "是否有视频联动信息(0否1是)")
    private Short haveVideo;
}
