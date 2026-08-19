package com.cgnpc.bbxpark.common.utils;

import cn.hutool.core.util.StrUtil;
import com.cgnpc.cud.core.support.TokenHolder;
import com.cgnpc.cud.shiro.util.ContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class WebFrameworkUtils {
    private static final Logger log = LoggerFactory.getLogger(WebFrameworkUtils.class);
    public static final String REQUEST_HEADER_ATTRIBUTE_LOGIN_USER_ID = "x-user-id";
    public static final String REQUEST_HEADER_ATTRIBUTE_TENANT_ID = "x-tenant-id";

    public WebFrameworkUtils() {
    }

    public static String getHeaderUserId() {
        // PC
        String userId = ContextHolder.getPrincipalName();
        if (StrUtil.isEmpty(userId) && ContextHolder.getPrincipal() != null) {
            userId = ContextHolder.getPrincipal().getName();
        }

        // 移动端
        if (StrUtil.isEmpty(userId) && TokenHolder.getUserTokenDto() != null) {
            userId = TokenHolder.getUserTokenDto().getUserID();
        }

        return userId;
        /*IAppContext appContext = SpringUtils.getBean(IAppContext.class);
        if (Objects.nonNull(appContext) && Objects.nonNull(appContext.getPrincipal())) {
            return appContext.getPrincipal().getName();
        }
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        } else {
            String staffId = request.getHeader(REQUEST_HEADER_ATTRIBUTE_LOGIN_USER_ID);
            return StringUtils.isNotBlank(staffId) ? staffId
                    : (String) request.getAttribute(REQUEST_HEADER_ATTRIBUTE_LOGIN_USER_ID);
        }*/
    }

    public static Long getHeaderTenantId() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        } else {
            String tenantId = request.getHeader(REQUEST_HEADER_ATTRIBUTE_TENANT_ID);
            return StringUtils.isNotBlank(tenantId) ? Long.valueOf(tenantId)
                    : (Long)request.getAttribute(REQUEST_HEADER_ATTRIBUTE_TENANT_ID);
        }
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        } else {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)requestAttributes;
            return servletRequestAttributes.getRequest();
        }
    }
}
