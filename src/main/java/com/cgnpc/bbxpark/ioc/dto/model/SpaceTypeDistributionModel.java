package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/3/24 8:39
 */
@Data
@ApiModel(value = "空间类型分布模型")
public class SpaceTypeDistributionModel implements Serializable {
    @ApiModelProperty(value = "空间类型")
    private Integer type;
    @ApiModelProperty(value = "数量")
    private Long count;

    public SpaceTypeDistributionModel(Integer type,Long count){
        this.type = type;
        this.count = count;
    }
}
