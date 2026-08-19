package com.cgnpc.bbxpark.traffic.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToIntegerConverter;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 司机表导入模型
 */
@Data
public class DwdVehicleDriverInfoParam{

    /** 驾驶员 */
    @ExcelProperty(value = "驾驶员", index = 0)
    private String driverName;

    /** 驾驶员班组 */
    @ExcelProperty(value = "驾驶员班组", index = 1)
    private String driverTeam;

    /** 电话 */
    @ExcelProperty(value = "电话", index = 2)
    private String phone;

    /** 性别 */
    @ExcelProperty(value = "性别", index = 3)
    private String gender;

    /** 年龄 */
    @ExcelProperty(value = "年龄", index = 4, converter = StringToIntegerConverter.class)
    private Integer age;

    /** 工作时间 */
    @ExcelProperty(value = "工作时间", index = 5)
    private String workHoursStr;

    /** 入职时间 */
    @ExcelProperty(value = "入职时间", index = 6)
    private String entryTimeStr;

    /** 广核工龄 */
    @ExcelProperty(value = "广核工龄", index = 7, converter = StringToDoubleConverter.class)
    private Double gnhWorkYears;

    /** 国家工龄 */
    @ExcelProperty(value = "国家工龄", index = 8, converter = StringToDoubleConverter.class)
    private Double nationWorkYears;

    /** 工作状态 */
    @ExcelProperty(value = "工作状态", index = 9)
    private String workStatus;

    /** 驾驶证号 */
    @ExcelProperty(value = "驾驶证号", index = 10)
    private String driverLicenseNo;

    /** 驾驶证有效结束日期 */
    @ExcelProperty(value = "驾驶证有效结束日期", index = 11)
    private String licenseExpireTimeStr;

}
