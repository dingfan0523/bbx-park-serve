package com.cgnpc.bbxpark.traffic.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import lombok.Data;

/**
 * 车辆行驶记录导入模型
 */
@Data
public class DwdVehicleCarTaskRecordParam {

    /** 车辆单位 */
    @ExcelProperty(value = "车辆单位", index = 0)
    private String carCompany;

    /** 车牌号 */
    @ExcelProperty(value = "车牌号", index = 1)
    private String carPlate;

    /** 驾驶员 */
    @ExcelProperty(value = "驾驶员", index = 2)
    private String driverName;

    /** 任务类型 */
    @ExcelProperty(value = "任务类型", index = 3)
    private String taskType;

    /** 任务情况 */
    @ExcelProperty(value = "任务情况", index = 4)
    private String taskDetail;

    /** 出车时间 */
    @ExcelProperty(value = "出车时间", index = 5)
    private String departTimeStr;

    /** 出车前公里数（KM） */
    @ExcelProperty(value = "出车前公里数（KM）", index = 6)
    private String beforeMileage;

    /** 收车时间 */
    @ExcelProperty(value = "收车时间", index = 7)
    private String returnTimeStr;

    /** 收车后公里数（KM） */
    @ExcelProperty(value = "收车后公里数（KM）", index = 8)
    private String afterMileage;

    /** 单趟行驶里程 */
    @ExcelProperty(value = "单趟行驶里程", index = 9, converter = StringToDoubleConverter.class)
    private Double singleMileage;

}
