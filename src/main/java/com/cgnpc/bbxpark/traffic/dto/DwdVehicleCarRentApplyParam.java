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
 * 租车记录表导入模型
 */
@Data
public class DwdVehicleCarRentApplyParam {

    /** 实例标题 */
    @ExcelProperty(value = "实例标题", index = 0)
    private String instanceTitle;

    /** 申请人 */
    @ExcelProperty(value = "申请人", index = 1)
    private String applicant;

    /** 用车部门 */
    @ExcelProperty(value = "用车部门", index = 2)
    private String useDept;

    /** 申请日期 */
    @ExcelProperty(value = "申请日期", index = 3)
    private String applyDateStr;

    /** 租车类型 */
    @ExcelProperty(value = "租车类型", index = 4)
    private String rentType;

    /** 租车时间 */
    @ExcelProperty(value = "租车时间", index = 5)
    private String rentTime;

    /** 租车天数 */
    @ExcelProperty(value = "租车天数", index = 6, converter = StringToDoubleConverter.class)
    private Double rentDays;

    /** 具体明细 */
    @ExcelProperty(value = "具体明细", index = 7)
    private String detailInfo;

    /** 附件 */
    @ExcelProperty(value = "附件", index = 8)
    private String attachment;

    /** 用车人 */
    @ExcelProperty(value = "用车人", index = 9)
    private String carUser;

    /** 租车事由 */
    @ExcelProperty(value = "租车事由", index = 10)
    private String rentReason;

    /** 提交人 */
    @ExcelProperty(value = "提交人", index = 11)
    private String submitter;

    /** 提交人组织 */
    @ExcelProperty(value = "提交人组织", index = 12)
    private String submitterOrg;

    /** 创建时间 */
    @ExcelProperty(value = "创建时间", index = 13)
    private String createTimeStr;

    /** 修改时间 */
    @ExcelProperty(value = "修改时间", index = 14)
    private String updateTimeStr;

    /** 当前审批节点 */
    @ExcelProperty(value = "当前审批节点", index = 15)
    private String currentApproveNode;

    /** 实例状态 */
    @ExcelProperty(value = "实例状态", index = 16)
    private String instanceStatus;

    /** 审批结果 */
    @ExcelProperty(value = "审批结果", index = 17)
    private String approveResult;
}
