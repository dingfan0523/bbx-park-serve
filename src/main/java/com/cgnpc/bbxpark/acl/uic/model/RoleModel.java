package com.cgnpc.bbxpark.acl.uic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "角色模型")
public class RoleModel {
    @ApiModelProperty("应用ID")
    private String appId;
    @ApiModelProperty("角色ID")
    private String roleId;
    @ApiModelProperty("角色编码")
    private String roleCode;
    @ApiModelProperty("角色描述")
    private String roleDesc;
    @ApiModelProperty("角色名称")
    private String roleName;
}
