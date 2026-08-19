package com.cgnpc.bbxpark.device.dto.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description ioc设备导出业务数据模型
 * @author huangyongtao
 * @date 2025/4/18 11:07
 */
@Data
public class IocDeviceExportModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    @ExcelIgnore
    private Long id;

    @ApiModelProperty(value = "设备名称.")
    @ExcelProperty(value = "设备名称", index = 0)
    private String deviceName;

    @ApiModelProperty(value = "设备编码.")
    @ExcelProperty(value = "设备编码", index = 1)
    private String deviceCode;

    @ApiModelProperty(value = "所属部门名称.")
    @ExcelProperty(value = "使用部门", index = 2)
    private String departmentName;

    @ApiModelProperty(value = "所属空间名称.")
    @ExcelProperty(value = "设备位置", index = 3)
    private String spaceName;

    @ApiModelProperty(value = "设备等级;（10：关键；20：重要；30：一般）.")
    @ExcelIgnore
    private Integer deviceLevel = 30;

    @ApiModelProperty(value = "设备等级;（10：关键；20：重要；30：一般）.")
    @ExcelProperty(value = "设备等级", index = 4)
    private String deviceLevelStr;

    @ApiModelProperty(value = "物联设备平台;（0：非物联网设备；1：自有平台；2：统建平台；3：安消平台）.")
    @ExcelIgnore
    private Integer iotDevicePlatform = 0;

    @ApiModelProperty(value = "物联设备平台;（0：非物联网设备；1：自有平台；2：统建平台；3：安消平台）.")
    @ExcelProperty(value = "物联设备", index = 5)
    private String iotDevicePlatformStr;

    @ApiModelProperty(value = "物联设备识别码.")
    @ExcelProperty(value = "设备识别码", index = 6)
    private String iotDeviceDn;

    @ApiModelProperty(value = "启用状态;（0->否;1->是）.")
    @ExcelIgnore
    private Integer enableStatus = 0;

    @ApiModelProperty(value = "启用状态;（0->否;1->是）.")
    @ExcelProperty(value = "启用状态", index = 7)
    private String enableStatusStr;

    @ApiModelProperty(value = "产品id.")
    @ExcelIgnore
    private Long productId;

    @ApiModelProperty(value = "产品名称")
    @ExcelProperty(value = "所属产品", index = 8)
    private String productName;

    @ApiModelProperty(value = "产品编码")
    @ExcelProperty(value = "产品编码", index = 9)
    private String pdCode ;

    @ApiModelProperty(value = "产品品牌")
    @ExcelProperty(value = "产品厂家", index = 10)
    private String pdBrand ;

    @ApiModelProperty(value = "产品型号")
    @ExcelProperty(value = "产品型号", index = 11)
    private String pdModel ;

    @ApiModelProperty(value = "产品尺寸")
    @ExcelProperty(value = "产品尺寸", index = 12)
    private String pdSize ;

    @ApiModelProperty(value = "产品单价")
    @ExcelProperty(value = "产品单价", index = 13)
    private Double pdUnitPrice ;

    @ApiModelProperty(value = "维保到期日期.")
    @ExcelIgnore
    private Date secureDate;

    @ApiModelProperty(value = "维保到期日期.")
    @ExcelProperty(value = "维保到期", index = 14)
    private String secureDateStr;

    @ApiModelProperty(value = "投用日期.")
    @ExcelIgnore
    private Date useDate;

    @ApiModelProperty(value = "投用日期.")
    @ExcelProperty(value = "投用日期", index = 15)
    private String useDateStr;

    @ApiModelProperty(value = "安装日期.")
    @ExcelIgnore
    private Date fixDate;

    @ApiModelProperty(value = "安装日期.")
    @ExcelProperty(value = "安装日期", index = 16)
    private String fixDateStr;

    @ApiModelProperty(value = "安装单位.")
    @ExcelProperty(value = "安装单位", index = 17)
    private String fixUnit;

    @ApiModelProperty(value = "责任人员名称.")
    @ExcelProperty(value = "责任人", index = 18)
    private String dutyUname;

    @ApiModelProperty(value = "责任人联系电话.")
    @ExcelProperty(value = "联系电话", index = 19)
    private String dutyMobile;

    @ApiModelProperty(value = "出厂编码.")
    @ExcelProperty(value = "出厂编码", index = 20)
    private String workCode;

    @ApiModelProperty(value = "出厂日期.")
    @ExcelIgnore
    private Date wordDate;

    @ApiModelProperty(value = "出厂日期.")
    @ExcelProperty(value = "出厂日期", index = 21)
    private String wordDateStr;

    @ApiModelProperty(value = "生产批次.")
    @ExcelProperty(value = "生产批次", index = 22)
    private String productionBatch;

    @ApiModelProperty(value = "设备类型;（1：单体设备；2：母子设备）.")
    @ExcelIgnore
    private Integer deviceType = 1;

    @ApiModelProperty(value = "设备类型;（1：单体设备；2：母子设备）.")
    @ExcelProperty(value = "设备类型", index = 23)
    private String deviceTypeStr;

    @ApiModelProperty(value = "抄表设备;（1->是;0->否）.")
    @ExcelIgnore
    private Integer readingDevice = 1;

    @ApiModelProperty(value = "抄表设备;（1->是;0->否）.")
    @ExcelProperty(value = "抄表设备", index = 24)
    private String readingDeviceStr;

    @ApiModelProperty(value = "抄表类型;（water：水表；electricity：电表；gas：燃气表）.")
    @ExcelIgnore
    private String readingType;

    @ApiModelProperty(value = "抄表类型;（water：水表；electricity：电表；gas：燃气表）.")
    @ExcelProperty(value = "抄表类型", index = 25)
    private String readingTypeStr;

    @ApiModelProperty(value = "抄表编码.")
    @ExcelProperty(value = "抄表编号", index = 26)
    private String readingCode;

    @ApiModelProperty(value = "抄表倍率.")
    @ExcelProperty(value = "倍率", index = 27)
    private Integer readingRate;

}
