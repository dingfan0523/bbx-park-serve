
package com.cgnpc.bbxpark.problemReport.param;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ProblemReportImportParam {
    @ApiModelProperty(value = "报修人姓名.")
    @ExcelProperty(index = 0)
	private String createBy;

    @ApiModelProperty(value = "报修日期.")
    @ExcelProperty(index = 1)
	private String createTimeStr;

    @ApiModelProperty(value = "维修区域.")
    @ExcelProperty(index = 2)
    private String spaceName;

    @ApiModelProperty(value = "故障需求描述.")
    @ExcelProperty(index = 3)
	private String problemDesc;

    @ApiModelProperty(value = "您对本次维修服务的满意度.")
    @ExcelProperty(index = 4)
	private String scoreStr;

    @ApiModelProperty(value = "不满意的原因.")
    @ExcelProperty(index = 5)
    private String evaluation;
}
