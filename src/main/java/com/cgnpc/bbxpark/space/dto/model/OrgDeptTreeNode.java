package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrgDeptTreeNode implements Serializable {

	private static final long serialVersionUID = -8794532669513427366L;

	@ApiModelProperty(value = "ID标识.等于deptId或者orgId")
	private String id;

	@ApiModelProperty(value = "部门ID")
	private String deptId;

	@ApiModelProperty(value = "组织ID")
	private String orgId;

	@Length(max = 50)
	@ApiModelProperty(value = "节点名称.")
	private String name;

	@ApiModelProperty(value = "节点类型:org/dept")
	private String type;

	@ApiModelProperty(value = "父部门标识，传-1标识顶级部门.")
	private String parentId;

	@ApiModelProperty(value = "状态.")
	private Short status;

	@ApiModelProperty(value = "树状路径.")
	private String treePath;

	@ApiModelProperty(value = "子部门列表")
	private List<OrgDeptTreeNode> children;

	public String getTreeId() {
		return getId();
	}

	public String getTreeParentId() {
		return getParentId();
	}

	public void addChildren(OrgDeptTreeNode child) {
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(child);
	}
}
