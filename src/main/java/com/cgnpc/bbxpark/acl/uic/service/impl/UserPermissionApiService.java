package com.cgnpc.bbxpark.acl.uic.service.impl;

import cn.hutool.json.JSONUtil;
import com.cgnpc.bbxpark.acl.uic.model.RoleModel;
import com.cgnpc.bbxpark.acl.uic.model.UserPermissionModel;
import com.cgnpc.bbxpark.acl.uic.service.IUserPermissionApiService;
import com.cgnpc.bbxpark.acl.uic.utils.HttpUtil;
import com.cgnpc.pro.api.ICudUserService;
import com.cgnpc.report.auth.impl.UauPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserPermissionApiService implements IUserPermissionApiService {
    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private ICudUserService cudUserService;
    @Value("${aep.appcenter.appId}")
    private String appCode;

    @Override
    public Set<String> getPermissionCodeByCurrent() {
        List<UserPermissionModel> list = getPermissionByStaffNo(cudUserService.getUser());
        return list.stream().map(UserPermissionModel::getMenuCode).collect(Collectors.toSet());
    }

    @Override
    public List<UserPermissionModel> getPermissionByStaffNo(String staffNo) {
        String url = "/uauauth/resource/getAuthMenuByAppCodeAndLoginName";
        Map<String,Object> param = new HashMap<>(4);
        param.put("loginName",staffNo);
        param.put("appCode",appCode);
        Object object = httpUtil.post(url,param);
        return JSONUtil.toList(JSONUtil.parseArray(object), UserPermissionModel.class);
    }
}
