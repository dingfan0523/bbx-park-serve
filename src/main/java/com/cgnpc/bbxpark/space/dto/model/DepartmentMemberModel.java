
package com.cgnpc.bbxpark.space.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
public class DepartmentMemberModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4480347470115817323L;

    @NotNull
    @ApiModelProperty(value = "部门成员标识.")
    private Long id;

    @ApiModelProperty(value = "创建时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "创建者.")
    private Long createUserId;

    @NotNull
    @ApiModelProperty(value = "部门标识.")
    private String departmentId;

    @ApiModelProperty(value = "身份，0普通1主管.")
    private Short identity;

    @ApiModelProperty(value = "组织标识.")
    private Long organizationId;

    @Length(max = 20)
    @ApiModelProperty(value = "职位.")
    private String position;

    @ApiModelProperty(value = "排序序号.")
    private Integer sortOrder;

    @ApiModelProperty(value = "状态，0启用1禁用.")
    private Short status;

    @ApiModelProperty(value = "修改时间.")
    private Date modifyDate;

    @ApiModelProperty(value = "修改者.")
    private Long modifyUserId;

    @NotNull
    @ApiModelProperty(value = "用户标识.")
    private String userId;

    @ApiModelProperty(value = "部门名")
    private String departmentName;

    private String staffId;

    private String userName;

    private Short sex;

    private String roleName;

}
