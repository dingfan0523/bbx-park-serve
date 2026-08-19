package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "楼层负责人列表模型")
public class ManagerModel {
    @ApiModelProperty(value = "人员类型")
    private Integer type;
    @ApiModelProperty(value = "工号")
    private String staffId;
    @ApiModelProperty(value = "名称")
    private String staffName;
    @ApiModelProperty(value = "联系方式")
    private String phone;
}