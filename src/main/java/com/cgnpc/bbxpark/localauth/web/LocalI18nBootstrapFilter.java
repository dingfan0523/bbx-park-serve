package com.cgnpc.bbxpark.localauth.web;

import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Profile("local-auth")
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class LocalI18nBootstrapFilter extends OncePerRequestFilter {

    private static final String PATH = "/sys/i18n/item/last-time";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!PATH.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":\"0\",\"customData\":{},\"data\":[{"
                + "\"category\":\"system\",\"time\":\"2022-03-09 14:37:15\","
                + "\"time1\":\"2022-03-09 14:37:15\",\"time2\":\"2022-03-09 14:37:15\"}],"
                + "\"message\":\"操作成功\",\"msg\":\"操作成功\"}");
    }
}
