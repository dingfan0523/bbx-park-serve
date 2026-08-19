package com.cgnpc.bbxpark.acl.uic.service;

import com.cgnpc.bbxpark.acl.uic.model.OrgDepartmentNode;
import com.cgnpc.bbxpark.space.dto.model.DepartmentInfoModel;
import com.cgnpc.bbxpark.space.dto.model.OrgDeptTreeNode;

import java.util.List;
import java.util.Set;

public interface IDepartmentApiService {
    List<OrgDepartmentNode> getOrgTreeForOrgWidget(String orgId);

    List<OrgDepartmentNode> getSecondOrgList();

    OrgDepartmentNode getOrgByStaffNo(String staffNo);

    OrgDepartmentNode getOrgByNo(String deptNo);

    List<OrgDeptTreeNode> listOrgTree(Long organizationId);

    List<DepartmentInfoModel> findSubDepartments(String departmentId);

    /**
     * 根据部门id集合批量查询部门数据
     * @param deptIds 部门id集合
     * @return 部门数据
     */
    List<OrgDepartmentNode> getOrgsByOrgIds(Set<String> deptIds);
}
