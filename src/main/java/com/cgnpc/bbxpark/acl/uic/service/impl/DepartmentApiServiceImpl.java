package com.cgnpc.bbxpark.acl.uic.service.impl;

import cn.hutool.json.JSONUtil;
import com.cgnpc.bbxpark.acl.uic.model.OrgDepartmentNode;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.acl.uic.utils.HttpUtil;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.StringUtils;
import com.cgnpc.bbxpark.space.dto.model.DepartmentInfoModel;
import com.cgnpc.bbxpark.space.dto.model.OrgDeptTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepartmentApiServiceImpl implements IDepartmentApiService {
    @Autowired
    private HttpUtil httpUtil;
    @Value("${aep.appcenter.appKey}")
    private String appKey;
    @Value("${hrcenter.department.root}")
    private String rootOrgId;
    @Value("${hrcenter.department.cangnan}")
    private String cangnanOrgId;

    @Override
    public List<OrgDepartmentNode> getOrgTreeForOrgWidget(String orgId) {
        orgId = StringUtils.isEmpty(orgId) ? rootOrgId : orgId;
        String url = "/hrcenter/getOrgTreeForOrgWidget";
        Map<String,Object> param = new HashMap<>(4);
        param.put("orgId",orgId);
        Object object = httpUtil.post(url,param);
        return JSONUtil.toList(JSONUtil.parseArray(object), OrgDepartmentNode.class);
    }

    @Override
    public List<OrgDepartmentNode> getSecondOrgList() {
        return getOrgTreeForOrgWidget(cangnanOrgId);
    }

    @Override
    public OrgDepartmentNode getOrgByStaffNo(String staffNo) {
        String url = "/hrcenter/getOrgByStaffNo";
        Map<String,Object> param = new HashMap<>(4);
        param.put("staffNo",staffNo);
        Object object = httpUtil.post(url,param);
        List<OrgDepartmentNode> list = JSONUtil.toList(JSONUtil.parseArray(object),OrgDepartmentNode.class);
        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }


    @Override
    public OrgDepartmentNode getOrgByNo(String deptNo) {
        String url = "/hrcenter/getDeptInfo";
        Map<String,Object> param = new HashMap<>(4);
        param.put("deptNo",deptNo);
        param.put("key",appKey);
        Object object = httpUtil.postBody(url,param);
        return JSONUtil.toBean(JSONUtil.toJsonStr(object),OrgDepartmentNode.class);
    }

    @Override
    public List<OrgDeptTreeNode> listOrgTree(Long organizationId) {
        return Collections.emptyList();
    }

    @Override
    public List<DepartmentInfoModel> findSubDepartments(String departmentId) {
        List<OrgDepartmentNode> list = this.getOrgTreeForOrgWidget(departmentId);
        return list.stream().map(dept->{
            DepartmentInfoModel model = new DepartmentInfoModel();
            model.setId(dept.getDeptNo());
            model.setName(dept.getOrgName());
            model.setParentId(dept.getParentOrgId());
            model.setTreePath(dept.getDeptIdPath());
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrgDepartmentNode> getOrgsByOrgIds(Set<String> deptIds) {
        if(CollectionUtils.isEmpty(deptIds)){
            return Collections.emptyList();
        }
        String orgIds = String.join(",",deptIds);
        String url = "/hrcenter/getOrgsByOrgIds";
        Map<String,Object> param = new HashMap<>(4);
        param.put("orgIds",orgIds);
        Object object = httpUtil.post(url,param);
        return JSONUtil.toList(JSONUtil.parseArray(object), OrgDepartmentNode.class);
    }
}
