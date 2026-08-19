package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DevicePointParam implements Serializable{
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "空间模型编码")
    private String sslcCode;
}