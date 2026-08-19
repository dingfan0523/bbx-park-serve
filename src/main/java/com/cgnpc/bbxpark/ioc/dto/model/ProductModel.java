package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "设备信息模型")
public class ProductModel {
    @ApiModelProperty(value = "IOC产品名称")
    private String pdName ;
    @ApiModelProperty(value = "产品编码")
    private String pdCode ;
    @ApiModelProperty(value = "产品厂家")
    private String pdBrand ;
    @ApiModelProperty(value = "产品型号")
    private String pdModel ;
    @ApiModelProperty(value = "产品尺寸")
    private String pdSize ;
    @ApiModelProperty(value = "产品单价")
    private Double pdUnitPrice ;
    @ApiModelProperty(value = "产品描述")
    private String pdDesc ;
    @ApiModelProperty(value = "产品图片集合")
    private List<String> pdImages;
}
