package com.cgnpc.bbxpark.acl.uic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "部门树模型")
public class OrgDepartmentNode {
    @ApiModelProperty("部门状态")
    private String orgStatus;
    @ApiModelProperty("是否展开")
    private String expanded;
    @ApiModelProperty("父部门名称")
    private String parentOrgName;
    @ApiModelProperty("叶节点")
    private String leafNodes;
    @ApiModelProperty("是否是叶子节点")
    private String isLeaf;
    @ApiModelProperty("部门级别")
    private String deptRank;
    @ApiModelProperty("部门名称")
    private String orgName;
    @ApiModelProperty("部门名称")
    private String deptName;
    @ApiModelProperty("部门名称全路径")
    private String deptNamePath;
    @ApiModelProperty("部门三字码全路径")
    private String deptCodePath;
    @ApiModelProperty("公司名称")
    private String corpName;
    @ApiModelProperty("部门编码")
    private String deptNo;
    @ApiModelProperty("部门简称")
    private String orgNameShort;
    @ApiModelProperty("父部门编码")
    private String parentOrgId;
    @ApiModelProperty("部门三字码")
    private String orgCode;
    @ApiModelProperty("部门编码全路径")
    private String deptIdPath;
    @ApiModelProperty("部门排序号")
    private String orgSeq;
    @ApiModelProperty("部门级别编码")
    private String deptRankId;
}
