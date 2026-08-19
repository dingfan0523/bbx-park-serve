package com.cgnpc.bbxpark.ioc.dto.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:21
 */
@Data
@ApiModel(value = "每日员工参会签到行为分析")
public class DailySignAnalysis {
    @ApiModelProperty(value = "日期(MM-DD)")
    //使用的是fastjson，这个注解jackson才有用
//    @JsonFormat(pattern = "MM-dd", timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    @JSONField(format = "MM-dd")
    private Date date;
    @ApiModelProperty(value = "正常签到率(%)")
    private Double normalSignRate;
    @ApiModelProperty(value = "异常签到率(%)")
    private Double abnormalSignRate;
}
