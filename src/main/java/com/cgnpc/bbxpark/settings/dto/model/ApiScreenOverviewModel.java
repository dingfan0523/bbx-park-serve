package com.cgnpc.bbxpark.settings.dto.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "大屏模块数据")
@Data
public class ApiScreenOverviewModel {
    @ApiModelProperty(value = "行政后勤数据")
    private JSONObject logisticsData;
    @ApiModelProperty(value = "综合节能数据")
    private JSONObject energyData;
    @ApiModelProperty(value = "应急消防数据")
    private JSONObject firefightingData;
}
