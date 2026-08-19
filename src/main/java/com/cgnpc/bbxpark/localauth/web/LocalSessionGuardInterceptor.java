package com.cgnpc.bbxpark.localauth.web;

import com.cgnpc.bbxpark.localauth.service.AuthenticatedUserProvider;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Profile("local-auth")
public class LocalSessionGuardInterceptor implements HandlerInterceptor {

    private final AuthenticatedUserProvider authenticatedUser;

    public LocalSessionGuardInterceptor(AuthenticatedUserProvider authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        try {
            authenticatedUser.currentUserId();
            return true;
        } catch (UnauthenticatedException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录\",\"msg\":\"未登录\"}");
            return false;
        }
    }
}
