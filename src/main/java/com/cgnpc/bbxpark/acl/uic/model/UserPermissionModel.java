package com.cgnpc.bbxpark.acl.uic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户权限模型")
public class UserPermissionModel {
    @ApiModelProperty("资源ID")
    private String funCode;
    @ApiModelProperty("资源名称")
    private String funcaction;
    @ApiModelProperty("资源编码")
    private String menuCode;
    @ApiModelProperty("资源url")
    private String numUrl;
    @ApiModelProperty("父资源id")
    private String parentsId;
    @ApiModelProperty("排序号")
    private String orderNo;
}
