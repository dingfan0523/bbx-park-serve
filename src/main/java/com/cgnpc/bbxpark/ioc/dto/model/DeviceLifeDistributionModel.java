package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "设备寿命分布")
@Builder
public class DeviceLifeDistributionModel {
    @ApiModelProperty(value = "警戒线数量阈值")
    private Integer warningThreshold;
    @ApiModelProperty(value = "寿命分布列表")
    private List<LifeRange> ranges;

    @Data
    @ApiModel(value = "寿命范围")
    public static class LifeRange {
        @ApiModelProperty(value = "寿命范围")
        private String range;
        @ApiModelProperty(value = "设备数量")
        private Integer count;
    }
}
