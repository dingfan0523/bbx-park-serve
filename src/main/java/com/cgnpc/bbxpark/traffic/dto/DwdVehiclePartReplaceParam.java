package com.cgnpc.bbxpark.traffic.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToIntegerConverter;
import lombok.Data;

import java.util.Date;

/**
 * 车辆配件更换记录导出模型
 *
 */
@Data
public class DwdVehiclePartReplaceParam{

    /** 车牌号 */
    @ExcelProperty(value = "车牌号", index = 0)
    private String plateNum;

    /** 车辆单位 */
    @ExcelProperty(value = "车辆单位", index = 1)
    private String vehicleDept;

    /** 申请日期 */
    @ExcelProperty(value = "申请日期", index = 2)
    private String applyDateStr;

    /** 公里数(千米) */
    @ExcelProperty(value = "公里数(千米)", index = 3, converter = StringToDoubleConverter.class)
    private Double mileage;

    /** 车型 */
    @ExcelProperty(value = "车型", index = 4)
    private String vehicleType;

    /** 更换数量(个) */
    @ExcelProperty(value = "更换数量(个)", index = 5, converter = StringToIntegerConverter.class)
    private Integer replaceCount;

    /** 申报人 */
    @ExcelProperty(value = "申报人", index = 6)
    private String reporter;

    /** 更换日期 */
    @ExcelProperty(value = "更换日期", index = 7)
    private String replaceDateStr;

    /** 车管签名 */
    @ExcelProperty(value = "车管签名", index = 8)
    private String vehicleAdminSign;

    /** 更换原因 */
    @ExcelProperty(value = "更换原因", index = 9)
    private String replaceReason;

    /** 送车人 */
    @ExcelProperty(value = "送车人", index = 10)
    private String carDeliverer;

    /** 流程状态 */
    @ExcelProperty(value = "流程状态", index = 11)
    private String processStatus;

    /** 当前处理人工号 */
    @ExcelProperty(value = "当前处理人工号", index = 12)
    private String currentHandlerId;

    /** 备注 */
    @ExcelProperty(value = "备注", index = 13)
    private String remark;

}
