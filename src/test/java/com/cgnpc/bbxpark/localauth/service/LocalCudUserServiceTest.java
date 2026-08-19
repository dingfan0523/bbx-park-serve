package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.pro.model.respvo.CudUserInfoVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalCudUserServiceTest {

    @Test
    void exposesAuthenticatedUserInFrontendCompatibleFormat() {
        LocalAuthProperties properties = new LocalAuthProperties();
        properties.setUserId("LOCAL001");
        properties.setUserName("本地用户");
        properties.setDeptId("LOCAL-DEPT");
        properties.setDeptName("本地测试部门");
        properties.setDeptNamePath("本地组织\\本地测试部门");
        AuthenticatedUserProvider authenticatedUser = () -> "LOCAL001";
        LocalCudUserService service = new LocalCudUserService(properties, authenticatedUser);

        CudUserInfoVO user = service.getUsersInfo(service.getUser());

        assertEquals("LOCAL001", user.getUserId());
        assertEquals("本地用户", user.getUserName());
        assertEquals("[LOCAL001]本地用户", user.getNowUserName());
        assertEquals("LOCAL-DEPT", user.getUserDeptId());
        assertEquals("本地组织\\本地测试部门", user.getUserDeptNamePath());
    }
}
