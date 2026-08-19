package com.cgnpc.bbxpark.traffic.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆维修信息导入模板
 *
 */
@Data
public class DwdVehicleRepairParam {

    /** 车牌号 */
    @ExcelProperty(value = "车牌号", index = 0)
    private String plateNum;

    /** 车辆所属单位 */
    @ExcelProperty(value = "车辆所属单位", index = 1)
    private String vehicleDept;

    /** 作业单号 */
    @ExcelProperty(value = "作业单号", index = 2)
    private String workOrderNo;

    /** 派工员 */
    @ExcelProperty(value = "派工员", index = 3)
    private String dispatcher;

    /** 报修人 */
    @ExcelProperty(value = "报修人", index = 4)
    private String reporter;

    /** 申报日期 */
    @ExcelProperty(value = "申报日期", index = 5)
    private String applyDateStr;

    /** 维修状态 */
    @ExcelProperty(value = "维修状态", index = 6)
    private String repairStatus;

    /** 当前处理人 */
    @ExcelProperty(value = "当前处理人", index = 7)
    private String currentHandler;

    /** 确认完成时间 */
    @ExcelProperty(value = "确认完成时间", index = 8)
    private String confirmFinishTimeStr;

    /** 送车人 */
    @ExcelProperty(value = "送车人", index = 9)
    private String carDeliverer;

    /** 维修项目名称 */
    @ExcelProperty(value = "维修项目名称", index = 10)
    private String repairItemName;

    /** 结算方 */
    @ExcelProperty(value = "结算方", index = 11)
    private String settlementParty;

    /** 品目编码 */
    @ExcelProperty(value = "品目编码", index = 12)
    private String itemCode;

    /** 品目名称 */
    @ExcelProperty(value = "品目名称", index = 13)
    private String itemName;

    /** 品目类型 */
    @ExcelProperty(value = "品目类型", index = 14)
    private String itemType;

    /** 品牌 */
    @ExcelProperty(value = "品牌", index = 15)
    private String brand;

    /** 规格信息 */
    @ExcelProperty(value = "规格信息", index = 16)
    private String specInfo;

    /** 型号 */
    @ExcelProperty(value = "型号", index = 17)
    private String model;

    /** 单位 */
    @ExcelProperty(value = "单位", index = 18)
    private String unit;

    /** 适用车型 */
    @ExcelProperty(value = "适用车型", index = 19)
    private String applicableVehicle;

    /** 来源 */
    @ExcelProperty(value = "来源", index = 20)
    private String source;

    /** 产地 */
    @ExcelProperty(value = "产地", index = 21)
    private String produceArea;

    /** 配件标识 */
    @ExcelProperty(value = "配件标识", index = 22)
    private String partMark;

    /** 数量 */
    @ExcelProperty(value = "数量", index = 23, converter = StringToDoubleConverter.class)
    private Double quantity;
}
