package com.cgnpc.bbxpark.acl.uic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "员工信息模型")
public class StaffModel {

    @ApiModelProperty(value = "员工号", required = true)
    private String empId;

    @ApiModelProperty(value = "员工姓名", required = true)
    private String empName;

    @ApiModelProperty("姓名全拼")
    private String empNamePY;

    @ApiModelProperty("员工英文名")
    private String empNameEn;

    @ApiModelProperty(value = "编制部门ID", required = true)
    private String deptId;

    @ApiModelProperty(value = "编制部门名称", required = true)
    private String deptName;

    @ApiModelProperty("编制部门三字码")
    private String deptCode;

    @ApiModelProperty("编制部门三字码全路径")
    private String deptCodePath;

    @ApiModelProperty("编制部门名称全路径")
    private String deptNamePath;

    @ApiModelProperty("编制部门Id全路径")
    private String deptIdPath;

    @ApiModelProperty("工作部门ID")
    private String workDeptId;

    @ApiModelProperty("工作部门名称")
    private String workDeptName;

    @ApiModelProperty("工作部门三字码")
    private String workDeptCode;

    @ApiModelProperty("工作部门三字码全路径")
    private String workDeptCodePath;

    @ApiModelProperty("工作部门名称全路径")
    private String workDeptNamePath;

    @ApiModelProperty("工作部门Id全路径")
    private String workDeptIdPath;

    @ApiModelProperty("员工职位编码")
    private String staffPosiNo;

    @ApiModelProperty("员工职位名称")
    private String staffPosiName;

    @ApiModelProperty("员工技术职称编码")
    private String staffTitle;

    @ApiModelProperty("员工技术职称名称")
    private String staffTitleName;

    @ApiModelProperty("员工技术级别编码")
    private String politicRankNo;

    @ApiModelProperty("员工技术岗位级别名称")
    private String politicRankName;

    @ApiModelProperty("员工企业职级编码")
    private String staffPoliticPost;

    @ApiModelProperty("员工企业职级名称")
    private String staffPoliticPostName;

    @ApiModelProperty("手机号")
    private String mobilePhone1;

    @ApiModelProperty("性别")
    private String staffSex;
}
