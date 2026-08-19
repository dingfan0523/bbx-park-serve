package com.cgnpc.bbxpark.traffic.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆信息导入模型
 *
 */
@Data
public class DwdVehicleInfoParam{

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号.")
    @ExcelProperty(value = "车牌号", index = 0)
    private String licensePlate;

    /**
     * 车型划分
     */
    @ApiModelProperty(value = "车型划分.")
    @ExcelProperty(value = "车型划分", index = 1)
    private String vehicleType;

    /**
     * 车架号
     */
    @ApiModelProperty(value = "车架号.")
    @ExcelProperty(value = "车架号", index = 2)
    private String vin;

    /**
     * 车辆型号
     */
    @ApiModelProperty(value = "车辆型号.")
    @ExcelProperty(value = "车辆型号", index = 3)
    private String vehicleModel;

    /**
     * 车辆单位
     */
    @ApiModelProperty(value = "车辆单位.")
    @ExcelProperty(value = "车辆单位", index = 4)
    private String department;

    /**
     * 车辆使用状态
     */
    @ApiModelProperty(value = "车辆使用状态.")
    @ExcelProperty(value = "车辆使用状态", index = 5)
    private String useStatus;

    /**
     * 车辆运行状态
     */
    @ApiModelProperty(value = "车辆运行状态.")
    @ExcelProperty(value = "车辆运行状态", index = 6)
    private String runStatus;

    /**
     * 行驶里程
     */
    @ApiModelProperty(value = "行驶里程.")
    @ExcelProperty(value = "行驶里程", index = 7, converter = StringToDoubleConverter.class)
    private Double mileage;

    /**
     * 下次年审日期
     */
    @ApiModelProperty(value = "下次年审日期.")
    @ExcelProperty(value = "下次年审日期", index = 8)
    private String nextInspectDateStr;

}
