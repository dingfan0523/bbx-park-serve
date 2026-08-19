package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.pro.api.ICudUserService;
import com.cgnpc.pro.model.resdto.HrcenterDto;
import com.cgnpc.pro.model.respvo.CudUserCenterVO;
import com.cgnpc.pro.model.respvo.CudUserInfoVO;
import com.cgnpc.pro.model.respvo.UserModel;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("cudUserService")
@Primary
@Profile("local-auth")
public class LocalCudUserService implements ICudUserService {

    private final LocalAuthProperties properties;
    private final AuthenticatedUserProvider authenticatedUser;

    public LocalCudUserService(LocalAuthProperties properties, AuthenticatedUserProvider authenticatedUser) {
        this.properties = properties;
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public String getUser(String... args) {
        return authenticatedUser.currentUserId();
    }

    @Override
    public String getUserRealName(String... args) {
        return properties.getUserName();
    }

    @Override
    public CudUserInfoVO getUsersInfo(String userId, String... args) {
        CudUserInfoVO user = new CudUserInfoVO();
        user.setUserId(properties.getUserId());
        user.setUserName(properties.getUserName());
        user.setNowUserName(getNowUser());
        user.setUserDeptId(properties.getDeptId());
        user.setUserDeptIdPath(properties.getDeptId());
        user.setUserDeptName(properties.getDeptName());
        user.setUserDeptNamePath(properties.getDeptNamePath());
        user.setWorkDeptId(properties.getDeptId());
        user.setWorkDeptIdPath(properties.getDeptId());
        user.setWorkDeptName(properties.getDeptName());
        user.setWorkDeptNamePath(properties.getDeptNamePath());
        user.setUserStatus("1");
        return user;
    }

    @Override
    public String getNowUser(String... args) {
        return "[" + properties.getUserId() + "]" + properties.getUserName();
    }

    @Override
    public String getUserName(String userId, String... args) {
        return properties.getUserName();
    }

    @Override
    public String getUserInfo(String userId, String... args) {
        return getNowUser();
    }

    @Override
    public String getUserPhoneNum(String userId, String... args) {
        return "";
    }

    @Override
    public List<UserModel> getUsersByUserCenter(String userIds, String... args) {
        if (userIds == null || !userIds.contains(properties.getUserId())) {
            return Collections.emptyList();
        }
        UserModel user = new UserModel();
        user.setUserID(properties.getUserId());
        user.setUserName(properties.getUserName());
        user.setDeptID(properties.getDeptId());
        user.setDeptName(properties.getDeptName());
        user.setDeptPath(properties.getDeptNamePath());
        return Collections.singletonList(user);
    }

    @Override
    public List<CudUserCenterVO> getStaffsByOrgId(HrcenterDto request, String... args) {
        return new ArrayList<>();
    }

    @Override
    public List<CudUserInfoVO> getUserList(HrcenterDto request, String... args) {
        return Collections.singletonList(getUsersInfo(properties.getUserId()));
    }
}
