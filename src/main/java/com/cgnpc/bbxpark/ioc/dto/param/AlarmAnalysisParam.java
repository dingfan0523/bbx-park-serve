package com.cgnpc.bbxpark.ioc.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "设备筛选参数")
public class AlarmAnalysisParam implements Serializable {
    @ApiModelProperty(value = "空间模型编码")
    private String sslcCode;
    @ApiModelProperty(value = "分组id")
    private Long groupId;
    @ApiModelProperty(value = "分组编码")
    private String groupCode;
}
