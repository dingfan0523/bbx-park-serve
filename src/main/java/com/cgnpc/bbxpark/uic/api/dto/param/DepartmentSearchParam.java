package com.cgnpc.bbxpark.uic.api.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "部门查询参数")
@Data
public class DepartmentSearchParam implements Serializable {
    @ApiModelProperty(value = "关键词")
    private String keyword;
    @ApiModelProperty(value = "部门id")
    private String orgId;
}
