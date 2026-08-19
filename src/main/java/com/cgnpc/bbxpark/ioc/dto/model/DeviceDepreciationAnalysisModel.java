package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel(value = "设备折旧分析")
public class DeviceDepreciationAnalysisModel {

    @ApiModelProperty(value = "警戒线数量阈值")
    private Integer warningThreshold;

    @ApiModelProperty(value = "折旧分布列表")
    private List<DepreciationRange> ranges;

    @Data
    @ApiModel(value = "折旧范围")
    public static class DepreciationRange {
        @ApiModelProperty(value = "折旧范围")
        private String range;
        @ApiModelProperty(value = "设备数量")
        private Integer count;
    }
}
