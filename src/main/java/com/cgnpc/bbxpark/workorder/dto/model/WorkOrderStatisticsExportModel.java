package com.cgnpc.bbxpark.workorder.dto.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/4/25
 * @desc 工单统计导出数据模型
 */
@Data
public class WorkOrderStatisticsExportModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    //月份
    @ExcelProperty(value = "月份" , index = 1)
    private String date;

    //报事报修
    @ExcelProperty(value = "报事报修" , index = 2)
    private Integer problemReeport;

    //设备告警
    @ExcelProperty(value = "设备告警" , index = 3)
    private Integer alarm;

    //抄表计划
    @ExcelProperty(value = "抄表计划" , index = 4)
    private Integer meterPlan;
}
