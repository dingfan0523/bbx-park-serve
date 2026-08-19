package com.cgnpc.bbxpark.traffic.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import lombok.Data;

import java.util.Date;

/**
 * 车辆保养信息导入模型
 */
@Data
public class DwdVehicleMaintainParam{

    /** 车牌号 */
    @ExcelProperty(value = "车牌号", index = 0)
    private String plateNum;

    /** 车辆单位 */
    @ExcelProperty(value = "车辆单位", index = 1)
    private String vehicleDept;

    /** 车辆类型 */
    @ExcelProperty(value = "车辆类型", index = 2)
    private String vehicleType;

    /** 上次保养日期 */
    @ExcelProperty(value = "上次保养日期", index = 3)
    private String lastMaintainDateStr;

    /** 保养日期 */
    @ExcelProperty(value = "保养日期", index = 4)
    private String maintainDateStr;

    /** 进厂里程数 */
    @ExcelProperty(value = "进厂里程数", index = 5, converter = StringToDoubleConverter.class)
    private Double inMileage;

    /** 申报单位 */
    @ExcelProperty(value = "申报单位", index = 6)
    private String applyDept;

    /** 申报日期 */
    @ExcelProperty(value = "申报日期", index = 7)
    private String applyDateStr;

    /** 保养地点 */
    @ExcelProperty(value = "保养地点", index = 8)
    private String maintainLocation;

    /** 预估费用 */
    @ExcelProperty(value = "预估费用", index = 9, converter = StringToDoubleConverter.class)
    private Double estimateCost;

    /** 发起人 */
    @ExcelProperty(value = "发起人", index = 10)
    private String sponsor;

    /** 保养状态 */
    @ExcelProperty(value = "保养状态", index = 11)
    private String maintainStatus;

    /** 送车人 */
    @ExcelProperty(value = "送车人", index = 12)
    private String carDeliverer;

    /** 保养项目名称 */
    @ExcelProperty(value = "保养项目名称", index = 13)
    private String maintainItemName;

    /** 结算方 */
    @ExcelProperty(value = "结算方", index = 14)
    private String settlementParty;

    /** 品目编码 */
    @ExcelProperty(value = "品目编码", index = 15)
    private String itemCode;

    /** 品目名称 */
    @ExcelProperty(value = "品目名称", index = 16)
    private String itemName;

    /** 品目类型 */
    @ExcelProperty(value = "品目类型", index = 17)
    private String itemType;

    /** 品牌 */
    @ExcelProperty(value = "品牌", index = 18)
    private String brand;

    /** 规格信息 */
    @ExcelProperty(value = "规格信息", index = 19)
    private String specInfo;

    /** 型号 */
    @ExcelProperty(value = "型号", index = 20)
    private String model;

    /** 单位 */
    @ExcelProperty(value = "单位", index = 21)
    private String unit;

    /** 适用车型 */
    @ExcelProperty(value = "适用车型", index = 22)
    private String applicableVehicle;

    /** 来源 */
    @ExcelProperty(value = "来源", index = 23)
    private String source;

    /** 产地 */
    @ExcelProperty(value = "产地", index = 24)
    private String produceArea;

    /** 数量 */
    @ExcelProperty(value = "数量", index = 25, converter = StringToDoubleConverter.class)
    private Double quantity;

    /** 单价 */
    @ExcelProperty(value = "单价", index = 26, converter = StringToDoubleConverter.class)
    private Double price;

    /** 品目金额 */
    @ExcelProperty(value = "品目金额", index = 27, converter = StringToDoubleConverter.class)
    private Double itemAmount;

}
