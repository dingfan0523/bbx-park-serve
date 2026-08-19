package com.cgnpc.bbxpark.acl.uic.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.cgnpc.bbxpark.acl.uic.model.RoleModel;
import com.cgnpc.bbxpark.acl.uic.model.SimpleStaffModel;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.utils.HttpUtil;
import com.cgnpc.pro.api.ICudUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleApiService implements IRoleApiService {
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private ICudUserService cudUserService;
    @Value("${aep.appcenter.appId}")
    private String appCode;

    @Override
    public List<RoleModel> findRoles() {
        String url = "/uauauth/role/getRoleByAppCode";
        Map<String,Object> param = new HashMap<>(4);
        param.put("appCode",appCode);
        Object object = httpUtil.post(url,param);
        return JSONUtil.toList(JSONUtil.parseArray(object), RoleModel.class);
    }

    @Override
    public RoleModel getRoleById(String roleId) {
        List<RoleModel> list = findRoles();
        if(CollectionUtil.isEmpty(list)){
            return null;
        }
        return list.stream().filter(r->r.getRoleId().equals(roleId)).findFirst().orElse(null);
    }

    @Override
    public List<RoleModel> getRoleByStaffNo(String staffNo) {
        String url = "/uauauth/role/getRoleByLoginNameAndAppCode";
        Map<String,Object> param = new HashMap<>(4);
        param.put("loginName",staffNo);
        param.put("appCode",appCode);
        Object object = httpUtil.post(url,param);
        return JSONUtil.toList(JSONUtil.parseArray(object), RoleModel.class);
    }

    @Override
    public List<RoleModel> getRoleByCurrent() {
        return getRoleByStaffNo(cudUserService.getUser());
    }

    @Override
    public List<SimpleStaffModel> findUserByRole(String roleCode) {
        String url = "/uauauth/user/getUserByAppCodeAndRoleCode";
        Map<String,Object> param = new HashMap<>(4);
        param.put("roleCode",roleCode);
        param.put("appCode",appCode);
        Object object = httpUtil.post(url,param);
        return JSONUtil.toList(JSONUtil.parseArray(object), SimpleStaffModel.class);
    }

    public List<String> findStaffNoByRoleCode(String roleCode){
        List<SimpleStaffModel> staffModels = this.findUserByRole(roleCode);
        if (CollUtil.isNotEmpty(staffModels)){
            return staffModels.stream().map(SimpleStaffModel::getLoginName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean hasRole(String staffNo, String roleCode) {
        List<RoleModel> roles = getRoleByStaffNo(staffNo);
        if(CollectionUtil.isEmpty(roles)){
            return false;
        }
        List<String> roleCodes = roles.stream().map(RoleModel::getRoleCode).collect(Collectors.toList());
        return roleCodes.contains(roleCode);
    }
}
