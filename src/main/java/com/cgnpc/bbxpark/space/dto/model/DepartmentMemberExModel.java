package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/9/24 9:37
 */
@Data
public class DepartmentMemberExModel implements Serializable {
    @ApiModelProperty("员工号")
    private String empId;
    @ApiModelProperty("员工姓名")
    private String empName;
    @ApiModelProperty("姓名全拼")
    private String empNamePY;
    @ApiModelProperty("员工英文名")
    private String empNameEn;
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
    @ApiModelProperty("工作部门Id 全路径")
    private String workDeptIdPath;
    @ApiModelProperty("员工手机号")
    private String mobilePhone1;
    @ApiModelProperty("性别")
    private String staffSex;
    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled = false;
}
