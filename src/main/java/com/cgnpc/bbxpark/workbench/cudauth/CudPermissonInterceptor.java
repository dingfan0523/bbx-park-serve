package com.cgnpc.bbxpark.workbench.cudauth;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cgnpc.bbxpark.workbench.application.IPermissonApplication;
import com.cgnpc.bbxpark.workbench.cudauth.utils.UserPermissionUtils;
import com.cgnpc.pro.api.ICudUserService;
import com.cgnpc.pro.auth.application.exception.CudAuthBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Set;

/**
 * @Author P629041
 * @Description 通用权限拦截器 优先级排在认证拦截通过后再调用
 * @Date 14:36 2023/5/24
 **/
@Slf4j
@Component
public class CudPermissonInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private IPermissonApplication iPermissonApplication;

    @Autowired(required = false)
    private ICudUserService cudUserService;

    /**
     * 登录模式
     **/
    @Value("${cud.login-mode:auth2}")
    private String loginMode;

    /**
     * 缓存key
     */
    public final static String cacheKey = ":userPermission:";

    /**
     * 解耦版本标识
     */
    public static final String PRO_LOGIN = "pro";

    /**
     * 内网版本标识
     */
    public static final String AEP_LOGIN = "aep";

    /**
     * 内部调用标识
     */
    private final String CUD_MENU_CODE = "CUD";

    /**
     * 请求头参数
     */
    private final String POST_HEAD = "menuCode";

    /**
     * 放行方法
     */
    private static final String EXCLUDE_METHOD = "OPTIONS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //跨域请求会首先发一个option请求，直接返回正常状态并通过拦截器
        if (EXCLUDE_METHOD.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        //获取到Header请求头里 menuCode
        String menuCode = request.getHeader(POST_HEAD);
        if(StrUtil.isBlank(menuCode)){
            throw new CudAuthBusinessException(POST_HEAD +" is null！");
        }

        //内部调用放行
        if (CUD_MENU_CODE.equals(menuCode)) {
            log.info("CudPermissonInterceptor cud local ：SUCCESS");
            return true;
        }
        //获取用户权限信息
        String userId = cudUserService.getUser();
        Set<String> permissionSet = getCacheUserPermissions(userId);
        if (CollUtil.isEmpty(permissionSet)) {
            permissionSet = iPermissonApplication.selectPermissions(userId);
        }
        if (!Objects.isNull(permissionSet) && permissionSet.contains(menuCode)) {
            log.info("CudPermissonInterceptor validate result：SUCCESS");
            return true;
        }

        log.info("请求url地址:{}；访问menuCode：{}", request.getRequestURI(), menuCode);
        throw new CudAuthBusinessException("抱歉！您没有相关权限！请联系管理员！");
    }

    /**
     * @Author P629041
     * @Description 获取用户权限
     * @Date 10:20 2024/1/4
     * @Param [userId]
     * @return java.util.Set<java.lang.String>
     **/
    private Set<String> getCacheUserPermissions(String userId){
        //确认登录模式
        String key = PRO_LOGIN.equals(loginMode) ? PRO_LOGIN : AEP_LOGIN;
        return UserPermissionUtils.cacheGet(key + cacheKey + userId);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }

}