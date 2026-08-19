package com.cgnpc.bbxpark.traffic.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToDoubleConverter;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToIntegerConverter;
import lombok.Data;

import java.util.Date;

/**
 * 车辆维修信息导入模板
 *
 */
@Data
public class VehicleApplyParam {
    @ExcelProperty(value = "乘车人", index = 0)
    private String passenger;
    @ExcelProperty(value = "部门", index = 1)
    private String department;
    @ExcelProperty(value = "乘坐人数", index = 2, converter = StringToIntegerConverter.class)
    private Integer passengerNum;
    @ExcelProperty(value = "联系方式", index = 3)
    private String contact;
    @ExcelProperty(value = "起始点", index = 4)
    private String startPoint;
    @ExcelProperty(value = "终点", index = 5)
    private String endPoint;
    @ExcelProperty(value = "叫车原因", index = 6)
    private String callReason;
    @ExcelProperty(value = "电召电话", index = 7)
    private String dispatchTel;
    @ExcelProperty(value = "驾驶员（常规运输）", index = 8)
    private String driverRegular;
    @ExcelProperty(value = "电召手机", index = 9)
    private String dispatchMobile;
    @ExcelProperty(value = "车牌号（常规）", index = 10)
    private String plateRegular;
    @ExcelProperty(value = "手机号", index = 11)
    private String mobile;
    @ExcelProperty(value = "乘车人2", index = 12)
    private String passenger2;
    @ExcelProperty(value = "部门2", index = 13)
    private String department2;
    @ExcelProperty(value = "联系方式2", index = 14)
    private String contact2;
    @ExcelProperty(value = "用车类型", index = 15)
    private String carType;
    @ExcelProperty(value = "宿舍点", index = 16)
    private String dormitory;
    @ExcelProperty(value = "厂区位置", index = 17)
    private String factoryLocation;
    @ExcelProperty(value = "用车时间（开元/天丰园）", index = 18)
    private String useTime1Str;
    @ExcelProperty(value = "用车时间（半山半岛/天和/南宋）", index = 19)
    private String useTime2Str;
    @ExcelProperty(value = "驾驶员", index = 20)
    private String driver;
    @ExcelProperty(value = "驾驶员联系方式", index = 21)
    private String driverContact;
    @ExcelProperty(value = "车牌号", index = 22)
    private String plateNum;
    @ExcelProperty(value = "运货皮卡1", index = 23)
    private String truckPickup1;
    @ExcelProperty(value = "运货皮卡2", index = 24)
    private String truckPickup2;
    @ExcelProperty(value = "车辆忙闲1", index = 25)
    private String vehicleStatus1;
    @ExcelProperty(value = "车辆忙闲2", index = 26)
    private String vehicleStatus2;
    @ExcelProperty(value = "车辆归属", index = 27)
    private String vehicleOwner;
    @ExcelProperty(value = "驾驶员（工业运输）", index = 28)
    private String driverIndustrial;
    @ExcelProperty(value = "驾驶员联系方式（工业）", index = 29)
    private String driverContactIndustrial;
    @ExcelProperty(value = "车牌号（运货）", index = 30)
    private String plateTruck;
    @ExcelProperty(value = "车辆忙闲", index = 31)
    private String vehicleStatus;
    @ExcelProperty(value = "提交人", index = 32)
    private String submitter;
    @ExcelProperty(value = "是否为CNOC员工", index = 33)
    private String isCnocEmployee;
    @ExcelProperty(value = "提交人组织", index = 34)
    private String submitterOrg;
    @ExcelProperty(value = "是否为CNOC员工（夜间）", index = 35)
    private String isCnocEmployeeNight;
    @ExcelProperty(value = "创建时间", index = 36)
    private String createTimeStr;
    @ExcelProperty(value = "驾驶员2", index = 37)
    private String driver2;
    @ExcelProperty(value = "修改时间", index = 38)
    private String updateTimeStr;
    @ExcelProperty(value = "当前审批节点", index = 39)
    private String approveNode;
    @ExcelProperty(value = "是否应急", index = 40)
    private String isEmergency;
    @ExcelProperty(value = "实例状态", index = 41)
    private String instanceStatus;
    @ExcelProperty(value = "起始点2", index = 42)
    private String startPoint2;
    @ExcelProperty(value = "审批结果", index = 43)
    private String approveResult;
    @ExcelProperty(value = "终点2", index = 44)
    private String endPoint2;
    @ExcelProperty(value = "驾驶员5", index = 45)
    private String driver5;
    @ExcelProperty(value = "拒绝原因说明", index = 46)
    private String rejectReason;
    @ExcelProperty(value = "是否派车", index = 47)
    private String isAssign;
    @ExcelProperty(value = "驾驶员3", index = 48)
    private String driver3;
}
