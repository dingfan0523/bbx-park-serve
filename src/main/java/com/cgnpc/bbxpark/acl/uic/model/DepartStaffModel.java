package com.cgnpc.bbxpark.acl.uic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "部门人员模型")
public class DepartStaffModel {

        @ApiModelProperty("员工号")
        private String staffNo;

        @ApiModelProperty("员工姓名")
        private String staffName;

        @ApiModelProperty("员工拼音")
        private String staffNamePy;

        @ApiModelProperty("姓名简称")
        private String staffShortPy;

        @ApiModelProperty("办公电话1")
        private String phoneOffice;

        @ApiModelProperty("办公电话2")
        private String phoneDorm;

        @ApiModelProperty("移动电话1")
        private String uniocomphone;

        @ApiModelProperty("移动电话2")
        private String phoneCity;

        @ApiModelProperty("移动电话3")
        private String phoneBy;

        @ApiModelProperty("办公地址")
        private String officeAddr;

        @ApiModelProperty("性别")
        private String staffSex;

        @ApiModelProperty("邮箱")
        private String outerEmail;

        @ApiModelProperty("部门")
        private String dept;

        @ApiModelProperty("部门名称")
        private String deptName;

        @ApiModelProperty("部门编码")
        private String deptNo;

        @ApiModelProperty("部门id")
        private String staffDeptId;

        @ApiModelProperty("部门类型")
        private String staffClass;

        @ApiModelProperty("部门地址")
        private String dormAddr;

        @ApiModelProperty("部门id路径")
        private String deptIdPath;

        @ApiModelProperty("部门名称路径")
        private String deptNamePath;

        @ApiModelProperty("部门编码路径")
        private String deptCodePath;

        @ApiModelProperty("编制部门id路径")
        private String workDeptIdPath;

        @ApiModelProperty("编制部门名称路径")
        private String workDeptNamePath;

        @ApiModelProperty("编制部门编码路径")
        private String workDeptCodePath;
}
