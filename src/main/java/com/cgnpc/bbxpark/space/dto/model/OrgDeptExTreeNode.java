package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/9/24 9:23
 */
@Data
public class OrgDeptExTreeNode implements Serializable {
    @ApiModelProperty(value = "ID标识.等于deptId或者orgId")
    private String id;
    @ApiModelProperty(value = "部门ID")
    private String deptId;
    @ApiModelProperty(value = "组织ID")
    private String orgId;
    @ApiModelProperty(value = "节点名称.")
    @Length(max = 50)
    private String name;
    @ApiModelProperty(value = "节点类型:org/dept")
    private String type;
    @ApiModelProperty(value = "父部门标识，传-1标识顶级部门.")
    private String parentId;
    @ApiModelProperty(value = "状态.")
    private Short status;
    @ApiModelProperty(value = "树状路径.")
    private String treePath;
    @ApiModelProperty(value = "是否默认选中")
    private Boolean checked = false;
    @ApiModelProperty(value = "子部门列表")
    private List<OrgDeptExTreeNode> children;
}
