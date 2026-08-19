package com.cgnpc.bbxpark.device.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/***
 * @value ioc设备分页参数模型
 * @author huangyongtao
 * @date 2025/2/21 17:11
 */
@Data
public class IocDevicePageParam extends CudPageDto implements Serializable{
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

    @ApiModelProperty(value = "所属空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "所属空间ID集合.")
    private List<Long> spaceIds;

    @ApiModelProperty(value = "所属部门id.")
    private String departmentId;

    @ApiModelProperty(value = "所属部门名称.")
    private String departmentName;

    @ApiModelProperty(value = "设备等级;（10：关键；20：重要；30：一般）.")
    private Integer deviceLevel;

    @ApiModelProperty(value = "启用状态;（0->否;1->是）.")
    private Integer enableStatus;

    @ApiModelProperty(value = "上线状态;（0->否;1->是）.")
    private Integer onlineStatus;

    @ApiModelProperty(value = "下线备注.")
    private String onlineRemark;

    @ApiModelProperty(value = "设备类型;（1：单体设备；2：母子设备）.")
    private Integer deviceType;

    @ApiModelProperty(value = "母子设备类型;（1:母设备；2：子设备）.")
    private Integer deviceComplexType;

    @ApiModelProperty(value = "母设备id.")
    private Long deviceComplexId;

    @ApiModelProperty(value = "抄表设备;（0->否;1->是）.")
    private Integer readingDevice;

    @ApiModelProperty(value = "抄表类型;（water：水表；electricity：电表；gas：燃气表）.")
    private String readingType;

    @ApiModelProperty(value = "抄表类型集合;（water：水表；electricity：电表；gas：燃气表）.")
    private List<String> readingTypes;

    @ApiModelProperty(value = "抄表编码.")
    private String readingCode;

    @ApiModelProperty(value = "抄表倍率.")
    private Integer readingRate;

    @ApiModelProperty(value = "抄表值.")
    private BigDecimal readingValue;

    @ApiModelProperty(value = "抄表时间.")
    private Date readingTime;

    @ApiModelProperty(value = "物联设备平台;（0：非物联网设备；1：自有平台；2：统建平台；3：安消平台）.")
    private Integer iotDevicePlatform;

    @ApiModelProperty(value = "物联设备识别码.")
    private String iotDeviceDn;

    @ApiModelProperty(value = "物理设备在线状态;（0->在线;1->离线）.")
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

    @ApiModelProperty(value = "责任人员名称.")
    private String dutyUname;

    @ApiModelProperty(value = "责任人联系电话.")
    private String dutyMobile;

    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "删除状态;删除状态(0->已删;1->未删).")
    private Integer deleted = 1;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "设备id集合")
    private List<Long> deviceIdList;

    @ApiModelProperty(value = "排除的设备id集合")
    private List<Long> noDeviceIdList;

    @ApiModelProperty(value = "设备分组id")
    private Long groupId;

    @ApiModelProperty(value = "设备分组编码")
    private String groupCode;

    @ApiModelProperty(value = "设备标签id(存在)")
    private Long labelId;

    @ApiModelProperty(value = "设备标签id(不存在)")
    private Long noLabelId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "是否需要权限")
    private Boolean auth = true;

    @ApiModelProperty(value = "告警状态;（0->正常;1->异常）.")
    private List<Integer> alarmStatusList;

    @ApiModelProperty(value = "告警状态;（0->正常;1->异常）.")
    private Integer alarmStatus;

    @ApiModelProperty(value = "维保状态;（0->否;1->是）.")
    private List<Integer> secureStatusList;

    @ApiModelProperty(value = "物理设备在线状态;（0->在线;1->离线）.")
    private List<Integer> iotDeviceStatusList;

    @ApiModelProperty(value = "启用状态;（0->否;1->是）.")
    private List<Integer> enableStatusList;

    @ApiModelProperty(value = "物联设备平台;（0：非物联网设备；1：自有平台；2：统建平台；3：安消平台）.")
    private List<Integer> iotDevicePlatformList;

    @ApiModelProperty(value = "设备等级;（10：关键；20：重要；30：一般）.")
    private List<Integer> deviceLevelList;

    @ApiModelProperty(value = "当前日期.")
    private Date localTime;

    @ApiModelProperty(value = "是否重点(0->否;1->是).")
    private Integer keyArea;

    @ApiModelProperty(value = "设备分类；1：弱电设备；2：CIT设备；3：固定资产.")
    private Integer deviceCategory;
}
