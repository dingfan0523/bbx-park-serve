package com.cgnpc.bbxpark.common.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * Cookie处理类
 */
public class CookieUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CookieUtils.class);

    /**
     * 获取登录用户id
     * @return 登录用户id
     */
    public static Long getHeaderUserId() {
        String userId = getUserId();
        return Long.valueOf(userId);
    }

    /**
     * 获取Cookie中的UserId
     *
     * @return
     */
    public static String getUserId() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
        Cookie[] cookies = request.getCookies();
        return getUserId(cookies);
    }

    /**
     * 获取Cookie中的UserId
     *
     * @param servletRequest ServletRequest
     * @return Cookie中的UserId
     */
    public static String getUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Cookie[] cookies = request.getCookies();
        return getUserId(cookies);
    }

    /**
     * 获取Cookie中的UserId
     *
     * @param cookies Cookie
     * @return Cookie中的UserId
     */
    public static String getUserId(Cookie[] cookies) {
        if (ArrayUtils.isNotEmpty(cookies)) {
            Optional<Cookie> cookieOptional = Arrays.stream(cookies).
                    filter(cookie -> StringUtils.equals(cookie.getName(), "user")).findFirst();
            if (cookieOptional.isPresent()) {
                Cookie cookie = cookieOptional.get();
                LOG.debug(String.format("doFilter : %s : %s ", cookie.getName(), cookie.getValue()));
                return cookie.getValue().toUpperCase();
            }
        }
        return "";
    }

    /**
     * 获取Cookie中的Token
     *
     * @param servletRequest ServletRequest
     * @return Cookie中的Token
     */
    public static String getToken(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Cookie[] cookies = request.getCookies();
        return getToken(cookies);
    }

    /**
     * 获取Cookie中的Token
     *
     * @param cookies Cookie
     * @return Cookie中的Token
     */
    public static String getToken(Cookie[] cookies) {
        if (ArrayUtils.isNotEmpty(cookies)) {
            Optional<Cookie> cookieOptional = Arrays.stream(cookies).
                    filter(cookie -> StringUtils.equals(cookie.getName(), "token")).findFirst();
            if (cookieOptional.isPresent()) {
                Cookie cookie = cookieOptional.get();
                LOG.debug(String.format("doFilter : %s : %s ", cookie.getName(), cookie.getValue()));
                return cookie.getValue();
            }
        }
        return "";
    }

    /**
     * 通过name获取Cookie中的value
     *
     * @param name key值
     * @return Cookie中的name对应的value
     */
    public static String getValueByName(String name) {
        String result = "";
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Cookie[] cookies = httpRequest.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            Optional<Cookie> option = Arrays.stream(cookies).filter(cookie -> StringUtils.equals(cookie.getName(),
                    "ddToken")).findFirst();
            if (option.isPresent()) {
                result = option.get().getValue();
            }
        }
        return result;
    }

    /**
     * 通过name设置Cookie中的value
     *
     * @param cookie 对象
     * @return Cookie中的name对应的value
     */
    public static void setCookie(Cookie cookie) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            Optional<Cookie> option = Arrays.stream(cookies).filter(model -> StringUtils.equals(model.getName(),
                    cookie.getName())).findFirst();
            if (option.isPresent()) {
                response.addCookie(cookie);
            }
        } else {
            response.addCookie(cookie);
        }
    }
}
