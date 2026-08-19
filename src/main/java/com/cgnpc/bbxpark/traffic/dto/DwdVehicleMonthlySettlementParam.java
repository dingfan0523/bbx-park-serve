package com.cgnpc.bbxpark.traffic.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import lombok.Data;

import java.io.Serializable;

/**
 * 车辆费用结算导入模型
 */
@Data
public class DwdVehicleMonthlySettlementParam implements Serializable {

    /** 车号 */
    @ExcelProperty(value = "车牌号", index = 1)
    private String carPlate;

    /** 车辆型号 */
    @ExcelProperty(value = "车型", index = 2)
    private String vehicleModel;

    /** 购买日期 */
    @ExcelProperty(value = "购买日期", index = 3)
    private String purchaseDateStr;

    /** 月初公里数 */
    @ExcelProperty(value = "月初公里数", index = 4, converter = StringToDoubleConverter.class)
    private Double startMonthMileage;

    /** 月末公里数 */
    @ExcelProperty(value = "月末公里数", index = 5, converter = StringToDoubleConverter.class)
    private Double endMonthMileage;

    /** 月行驶公里数 */
    @ExcelProperty(value = "月行驶公里数", index = 6, converter = StringToDoubleConverter.class)
    private Double monthMileage;

    /** 包月公里数 */
    @ExcelProperty(value = "包月公里数", index = 7, converter = StringToDoubleConverter.class)
    private Double monthlyPackageMileage;

    /** 月租费（元） */
    @ExcelProperty(value = "月租费（元）", index = 8, converter = StringToDoubleConverter.class)
    private Double monthlyRent;

    /** 当月15日油价 */
    @ExcelProperty(value = "当月15日油价", index = 12, converter = StringToDoubleConverter.class)
    private Double oilPrice;

    /** 油耗 */
    @ExcelProperty(value = "油耗", index = 13, converter = StringToDoubleConverter.class)
    private Double fuelConsumption;

    /** 燃油补差 */
    @ExcelProperty(value = "燃油补差", index = 14, converter = StringToDoubleConverter.class)
    private Double fuelSubsidy;

    /** 住宿费 */
    @ExcelProperty(value = "住宿费", index = 15, converter = StringToDoubleConverter.class)
    private Double accommodationFee;

    /** 差旅补贴 */
    @ExcelProperty(value = "差旅补贴", index = 16, converter = StringToDoubleConverter.class)
    private Double travelAllowance;

    /** 物资配置费 */
    @ExcelProperty(value = "物资配置费", index = 17, converter = StringToDoubleConverter.class)
    private Double materialConfigFee;

    /** 停车费 */
    @ExcelProperty(value = "停车费", index = 18, converter = StringToDoubleConverter.class)
    private Double parkingFee;

    /** 路桥费 */
    @ExcelProperty(value = "路桥费", index = 19, converter = StringToDoubleConverter.class)
    private Double tollFee;

    /** 路桥停车住宿税费 */
    @ExcelProperty(value = "路桥停车住宿税费", index = 20, converter = StringToDoubleConverter.class)
    private Double tollParkingAccommodationTax;
}
