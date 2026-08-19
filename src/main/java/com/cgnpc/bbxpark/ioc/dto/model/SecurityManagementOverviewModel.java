package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "安全管理概览模型")
public class SecurityManagementOverviewModel {
    @ApiModelProperty(value = "施工数量")
    private Integer constructionCount;
    @ApiModelProperty(value = "高危品数量")
    private Integer highRiskCount;
}