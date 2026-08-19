package com.cgnpc.bbxpark.localauth.web;

import com.cgnpc.bbxpark.localauth.service.AuthenticatedUserProvider;
import org.apache.shiro.authz.UnauthenticatedException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalSessionGuardInterceptorTest {

    @Test
    void returns401BeforeProtectedControllerWhenSessionIsMissing() throws Exception {
        AuthenticatedUserProvider missingUser = () -> {
            throw new UnauthenticatedException("未登录");
        };
        LocalSessionGuardInterceptor interceptor = new LocalSessionGuardInterceptor(missingUser);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/user/getCurrentUser");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean continued = interceptor.preHandle(request, response, new Object());

        assertEquals(401, response.getStatus());
        assertFalse(continued);
    }

    @Test
    void letsProtectedControllerProceedWhenSessionExists() throws Exception {
        LocalSessionGuardInterceptor interceptor = new LocalSessionGuardInterceptor(() -> "LOCAL001");
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/user/getCurrentUser");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean continued = interceptor.preHandle(request, response, new Object());

        assertTrue(continued);
    }
}
