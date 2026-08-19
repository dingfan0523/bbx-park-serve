package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "产品列表模型")
public class ProductListModel {
    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "产品名称")
    private String pdName;

    @ApiModelProperty(value = "产品编码")
    private String pdCode;
}
