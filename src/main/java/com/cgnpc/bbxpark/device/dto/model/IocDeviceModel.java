
package com.cgnpc.bbxpark.device.dto.model;

import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/***
 * @value ioc设备业务数据模型
 * @author huangyongtao
 * @date 2025/2/21 17:07
 */
@Data
public class IocDeviceModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "设备编码.")
    private String deviceCode;

    @ApiModelProperty(value = "产品id.")
    private Long productId;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "所属空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "所属空间名称.")
    private String spaceName;

    @ApiModelProperty(value = "所属部门id.")
    private String departmentId;

    @ApiModelProperty(value = "所属部门名称.")
    private String departmentName;

    @ApiModelProperty(value = "设备等级;（10：关键；20：重要；30：一般）.")
    private Integer deviceLevel = 30;

    @ApiModelProperty(value = "启用状态;（0->否;1->是）.")
    private Integer enableStatus = 1;

    @ApiModelProperty(value = "上线状态;（0->否;1->是）.")
    private Integer onlineStatus = 1;

    @ApiModelProperty(value = "下线备注.")
    private String onlineRemark;

    @ApiModelProperty(value = "设备类型;（1：单体设备；2：母子设备）.")
    private Integer deviceType = 1;

    @ApiModelProperty(value = "母子设备类型;（1:母设备；2：子设备）.")
    private Integer deviceComplexType;

    @ApiModelProperty(value = "母设备id.")
    private Long deviceComplexId;

    @ApiModelProperty(value = "抄表设备;（0->否;1->是）.")
    private Integer readingDevice = 1;

    @ApiModelProperty(value = "抄表类型;（water：水表；electricity：电表；gas：燃气表）.")
    private String readingType;

    @ApiModelProperty(value = "抄表编码.")
    private String readingCode;

    @ApiModelProperty(value = "抄表倍率.")
    private Integer readingRate;

    @ApiModelProperty(value = "抄表值.")
    private BigDecimal readingValue;

    @ApiModelProperty(value = "抄表时间.")
    private Date readingTime;

    @ApiModelProperty(value = "物联设备平台;（0：非物联网设备；1：自有平台；2：统建平台；3：安消平台）.")
    private Integer iotDevicePlatform = 0;

    @ApiModelProperty(value = "物联设备识别码.")
    private String iotDeviceDn;

    @ApiModelProperty(value = "物联设备在线状态;（0->在线;1->离线）.")
    private Integer iotDeviceStatus;

    @ApiModelProperty(value = "物联产品类型.")
    private String iotProductCode;

    @ApiModelProperty(value = "出厂编码.")
    private String workCode;

    @ApiModelProperty(value = "出厂日期.")
    private Date wordDate;

    @ApiModelProperty(value = "生产批次.")
    private String productionBatch;

    @ApiModelProperty(value = "维保到期日期.")
    private Date secureDate;

    @ApiModelProperty(value = "预计报废日期.")
    private Date scrapDate;

    @ApiModelProperty(value = "投用日期.")
    private Date useDate;

    @ApiModelProperty(value = "安装日期.")
    private Date fixDate;

    @ApiModelProperty(value = "安装单位.")
    private String fixUnit;

    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "设备分组集合.")
    private List<DeviceGroupRelModel> deviceGroupRelModel;

    @ApiModelProperty(value = "空间全路径.")
    private String spaceFullPath;

    @ApiModelProperty(value = "母子设备集合")
    private List<IocDeviceSimpleModel> iocDeviceSimpleModelList;

    @ApiModelProperty(value = "设备标签（多个以逗号隔开）.")
    private String labelNames;

    @ApiModelProperty(value = "设备分组（多个以逗号隔开）.")
    private String groupNames;

    @ApiModelProperty(value = "ioc产品信息.")
    private IocProductModel iocProductModel;

    @ApiModelProperty(value = "设备图片集合")
    private List<FileModel> fileModelList;

    @ApiModelProperty(value = "物联设备在线状态更新时间")
    private Date iotDeviceStatusTime;

    @ApiModelProperty(value = "设备告警状态;（0->正常;1->异常）.")
    private Integer alarmStatus;

    @ApiModelProperty(value = "维保状态;（0->否;1->是）.")
    private Integer secureStatus;

    @ApiModelProperty(value = "是否重点(0->否;1->是).")
    private Integer keyArea;


    @ApiModelProperty(value = "设备分类；1：弱电设备；2：CIT设备；3：固定资产.")
    private Integer deviceCategory;

    @ApiModelProperty(value = "规格型号")
    private String standardModel;

    @ApiModelProperty(value = "总折旧月数")
    private Integer depreciationMonth ;

    @ApiModelProperty(value = "责任部门id")
    private Long dutyDepartmentId;

    @ApiModelProperty(value = "责任部门名称")
    private String dutyDepartmentName;

    @ApiModelProperty(value = "责任人id")
    private Long dutyUid;

    @ApiModelProperty(value = "责任人员名称")
    private String dutyUname;

    @ApiModelProperty(value = "责任人联系电话")
    private String dutyMobile;

    @ApiModelProperty(value = "责任人工号")
    private String dutyStaffid;

    @ApiModelProperty(value = "使用人id")
    private Long useUid ;

    @ApiModelProperty(value = "使用人名称")
    private String useUname ;

    @ApiModelProperty(value = "使用工号")
    private String useStaffid;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商联系人id")
    private Long supplierPersonId;

    @ApiModelProperty(value = "供应商联系人")
    private String supplierPerson;

    @ApiModelProperty(value = "供应商联系电话.")
    private String supplierMobile;

    @ApiModelProperty(value = "抄表图片.")
    private String readingImg;

}
