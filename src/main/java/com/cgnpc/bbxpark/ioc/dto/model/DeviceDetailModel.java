package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "设备详情模型")
public class DeviceDetailModel {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @ApiModelProperty(value = "设备编码")
    private String deviceCode;
    @ApiModelProperty(value = "设备等级;（10：关键；20：重要；30：一般）.")
    private Integer deviceLevel;
    @ApiModelProperty(value = "设备位置")
    private String spaceName;
    @ApiModelProperty(value = "使用部门")
    private String useDeptName;
    @ApiModelProperty(value = "设备分组名称")
    private String groupName;
    @ApiModelProperty(value = "设备标签(多个以,分割)")
    private String labelNames;
    @ApiModelProperty(value = "维保到期日期.")
    private Date secureDate;
    @ApiModelProperty(value = "预计报废日期.")
    private Date scrapDate;

    @ApiModelProperty(value = "出厂编码.")
    private String workCode;
    @ApiModelProperty(value = "出厂日期.")
    private Date wordDate;
    @ApiModelProperty(value = "生产批次.")
    private String productionBatch;
    @ApiModelProperty(value = "设备类型;（1：单体设备；2：母子设备）.")
    private Integer deviceType;
    @ApiModelProperty(value = "抄表设备;（0->否;1->是）.")
    private Integer readingDevice = 1;
    @ApiModelProperty(value = "抄表类型;（water：水表；electricity：电表；gas：燃气表）.")
    private String readingType;
    @ApiModelProperty(value = "抄表编码.")
    private String readingCode;
    @ApiModelProperty(value = "抄表倍率.")
    private Integer readingRate;
    @ApiModelProperty(value = "设备图片集合")
    private List<String> images;

    @ApiModelProperty(value = "投用日期.")
    private Date useDate;
    @ApiModelProperty(value = "安装日期.")
    private Date fixDate;
    @ApiModelProperty(value = "安装单位.")
    private String fixUnit;
    @ApiModelProperty(value = "责任人员名称")
    private String dutyUname;
    @ApiModelProperty(value = "责任人联系电话")
    private String dutyMobile;
    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "设备在线状态(0->离线;1->在线)")
    private Integer deviceStatus;
    @ApiModelProperty(value = "设备告警状态(0->否;1->是)")
    private Integer alarmStatus;
    @ApiModelProperty(value = "在线状态更新时间")
    private Date deviceStatusTime;
    @ApiModelProperty(value = "告警状态更新时间")
    private Date alarmStatusTime;

    @ApiModelProperty(value = "产品信息")
    private ProductModel product;
}
