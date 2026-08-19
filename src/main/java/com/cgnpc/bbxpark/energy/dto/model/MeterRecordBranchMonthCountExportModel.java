package com.cgnpc.bbxpark.energy.dto.model;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * @Description 支路能耗趋势月导出模型
 * @author huangyongtao
 * @date 2025/4/24 13:53
 */
@Data
public class MeterRecordBranchMonthCountExportModel {

    @ApiModelProperty(value = "时间")
    @ExcelProperty(value = "时间", index = 1)
    private String time;

    @ApiModelProperty(value = "本用量")
    @ExcelProperty(value = "本月", index = 2)
    private Double value = 0d;

    @ApiModelProperty(value = "上用量")
    @ExcelProperty(value = "上月", index = 3)
    private Double lastValue = 0d;

    @ApiModelProperty(value = "去年本用量")
    @ExcelProperty(value = "去年本月", index = 4)
    private Double lastYearValue = 0d;

}
