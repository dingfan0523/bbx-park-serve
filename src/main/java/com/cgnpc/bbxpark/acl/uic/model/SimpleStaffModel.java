package com.cgnpc.bbxpark.acl.uic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "简单的员工模型")
public class SimpleStaffModel {
    @ApiModelProperty("人员编号")
    private String loginName;
    @ApiModelProperty("人员编号")
    private String userName;
}
