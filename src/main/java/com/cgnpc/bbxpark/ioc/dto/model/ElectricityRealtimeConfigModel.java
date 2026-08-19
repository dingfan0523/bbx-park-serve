package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "实时组态")
public class ElectricityRealtimeConfigModel {
    @ApiModelProperty(value = "支路名称")
    private String branchName;
    @ApiModelProperty(value = "支路编码")
    private String branchCode;
    @ApiModelProperty(value = "支路类型")
    private String branchType;
    @ApiModelProperty(value = "设备集合")
    private List<ElectricityDeviceModel> devices;
    @ApiModelProperty(value = "下级支路节点")
    private List<ElectricityRealtimeConfigModel> children;
    @Data
    public static class ElectricityDeviceModel {
        @ApiModelProperty(value = "设备名称")
        private String deviceName;
        @ApiModelProperty(value = "抄表编码")
        private String readingCode;
        @ApiModelProperty(value = "设备位置")
        private String spaceName;
        @ApiModelProperty(value = "抄表值")
        private BigDecimal readingValue;
        @ApiModelProperty(value = "是否正常")
        private Boolean normal;
    }
}
