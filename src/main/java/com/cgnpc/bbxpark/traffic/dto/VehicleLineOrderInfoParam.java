package com.cgnpc.bbxpark.traffic.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.restaurant.service.impl.StringToIntegerConverter;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆维修信息导入模板
 *
 */
@Data
public class VehicleLineOrderInfoParam {
    @ExcelProperty(value = "运行组织机构", index = 0)
    private String runOrg;
    @ExcelProperty(value = "线路分组", index = 1)
    private String lineGroup;
    @ExcelProperty(value = "线路名称", index = 2)
    private String lineName;
    @ExcelProperty(value = "线路方向", index = 3)
    private String lineDirection;
    @ExcelProperty(value = "任务日期", index = 4)
    private String taskDateStr;
    @ExcelProperty(value = "发车时刻", index = 5)
    private String departTime;
    @ExcelProperty(value = "预定人数", index = 6, converter = StringToIntegerConverter.class)
    private Integer reserveNum;
    @ExcelProperty(value = "未支付人数", index = 7, converter = StringToIntegerConverter.class)
    private Integer unpaidNum;
    @ExcelProperty(value = "已支付人数", index = 8, converter = StringToIntegerConverter.class)
    private Integer paidNum;
    @ExcelProperty(value = "金额合计", index = 9)
    private String totalAmount;
    @ExcelProperty(value = "订车人", index = 10)
    private String orderUser;
    @ExcelProperty(value = "订车人单位", index = 11)
    private String orderUserDept;
    @ExcelProperty(value = "预留电话", index = 12)
    private String reservePhone;
    @ExcelProperty(value = "上车站点", index = 13)
    private String startStation;
    @ExcelProperty(value = "下车站点", index = 14)
    private String endStation;
    @ExcelProperty(value = "乘坐人数", index = 15, converter = StringToIntegerConverter.class)
    private Integer rideNum;
    @ExcelProperty(value = "儿童人数", index = 16, converter = StringToIntegerConverter.class)
    private Integer childNum;
    @ExcelProperty(value = "订单金额", index = 17)
    private String orderAmount;
    @ExcelProperty(value = "订单状态", index = 18)
    private String orderStatus;
    @ExcelProperty(value = "支付方式", index = 19)
    private String payType;
    @ExcelProperty(value = "实际支付时间", index = 20)
    private String actualPayTimeStr;
    @ExcelProperty(value = "备注", index = 21)
    private String remark;
    @ExcelProperty(value = "航班/列车信息", index = 22)
    private String flightTrainInfo;
}
