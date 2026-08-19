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
 * @date 2026/2/28 15:11
 */
@Data
@ApiModel(value = "节能方案执行趋势")
public class ExecutionTrend {
    @ApiModelProperty(value = "日期列表(格式: MM/DD)")
    //使用的是fastjson，这个注解jackson才有用
//    @JsonFormat(pattern = "MM-dd", timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    @JSONField(format = "MM-dd")
    private Date date;
    @ApiModelProperty(value = "执行次数")
    private Integer executeCount;
}
