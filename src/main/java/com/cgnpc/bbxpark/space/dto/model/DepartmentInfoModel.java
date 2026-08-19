
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class DepartmentInfoModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3027256453865435317L;

    @NotNull
    @ApiModelProperty(value = "部门标识.")
    private String id;

    @Length(max = 200)
    @ApiModelProperty(value = "描述.")
    private String description;

    @ApiModelProperty(value = "失效时间，为空表示永久有效.")
    private Date expireTime;

    @ApiModelProperty(value = "部门层级.")
    private Integer level;

    @NotBlank
    @Length(max = 50)
    @ApiModelProperty(value = "部门名称.")
    private String name;

    @Length(max = 200)
    @ApiModelProperty(value = "部门编码.")
    private String code;

    @ApiModelProperty(value = "成员数量.")
    private Integer numbers;

    @ApiModelProperty(value = "组织标识.")
    private String organizationId;

    @ApiModelProperty(value = "父部门标识，传-1标识顶级部门.")
    private String parentId;

    @ApiModelProperty(value = "排序序号.")
    private Integer sortOrder;

    @ApiModelProperty(value = "状态.")
    private Short status;

    @Length(max = 200)
    @ApiModelProperty(value = "简介.")
    private String synopsis;

    @ApiModelProperty(value = "部门属性，0正常1临时.")
    private Short temporary;

    @ApiModelProperty(value = "树状路径.")
    private String treePath;
}
