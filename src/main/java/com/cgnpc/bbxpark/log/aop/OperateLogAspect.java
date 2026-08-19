package com.cgnpc.bbxpark.log.aop;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.json.JSONUtil;
import com.cgnpc.bbxpark.common.utils.StringUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.utils.WebUtil;
import com.cgnpc.bbxpark.log.properties.OperateLogConfig;
import com.cgnpc.bbxpark.log.service.IOperateLogService;
import com.cgnpc.bbxpark.log.vo.ActionTypeConfigItem;
import com.cgnpc.bbxpark.log.vo.ChildrenActionType;
import com.cgnpc.bbxpark.log.vo.ModuleNameConfigItem;
import com.cgnpc.bbxpark.log.vo.OperateLogParam;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Aspect
@AllArgsConstructor
@Slf4j
@Component
public class OperateLogAspect {

    public static final String STR_UNDEFINED = "未定义";
    @Autowired
    private IOperateLogService operateLogService;

    @Autowired
    private OperateLogConfig operateLogConfig;


    public static final short SUCCESS = 0;
    public static final short FAIL = 0;

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 可能也添加了 @ApiOperation 注解
        PostMapping postMapping = getMethodAnnotation(joinPoint,
                PostMapping.class
        );
        GetMapping getMapping = getMethodAnnotation(joinPoint,
                GetMapping.class
        );
        DeleteMapping deleteMapping = getMethodAnnotation(joinPoint,
                DeleteMapping.class
        );
        PutMapping putMapping = getMethodAnnotation(joinPoint,
                PutMapping.class
        );

        String moduleName = STR_UNDEFINED;
        String actionType = STR_UNDEFINED;

        try {
            //获取到ServletRequestAttributes 里面有
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            //获取到Request对象
            HttpServletRequest request = attrs.getRequest();
            if (operateLogConfig.getActionTypeConfig() != null) {
                for (ActionTypeConfigItem actionTypeConfigItem : operateLogConfig.getActionTypeConfig()) {
                    if (StringUtils.isNotBlank(actionTypeConfigItem.getControllerMethodName())
                            && joinPoint.getSignature()
                            .getName()
                            .equals(actionTypeConfigItem.getControllerMethodName())) {
                        actionType = actionTypeConfigItem.getActionType();
                        break;
                    }
                    if (actionTypeConfigItem.getMatcher() != null && actionTypeConfigItem.getMatcher()
                            .matches(request)) {
                        actionType = actionTypeConfigItem.getActionType();
                        break;
                    }
                }
            }
            if (operateLogConfig.getModuleNameConfig() != null) {
                for (ModuleNameConfigItem moduleNameConfigItem : operateLogConfig.getModuleNameConfig()) {
                    if (moduleNameConfigItem.getMatcher().matches(request)) {
                        moduleName = moduleNameConfigItem.getModuleName();
                        //子爱定义actionType
                        if (moduleNameConfigItem.getChildrenActionType() != null) {
                            for (ChildrenActionType childrenActionType : moduleNameConfigItem.getChildrenActionType()) {
                                if (childrenActionType.getMatcher().matches(request)) {
                                    actionType = childrenActionType.getActionType();
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }

        } catch (Exception e) {
            log.error("获取request异常", e);
        }

        Object[] args = joinPoint.getArgs();

        // 都没有这些注解，直接执行
        if (postMapping == null && deleteMapping == null && getMapping == null && putMapping == null) {
            return joinPoint.proceed();
        }
        // 记录开始时间
        LocalDateTime startTime = LocalDateTime.now();
        try {
            // 执行原有方法
            Object result = joinPoint.proceed();
            // 记录正常执行时的操作日志（未配置的模块不记录操作日志）
            if (!moduleName.equals(STR_UNDEFINED)) {
                this.log(joinPoint, startTime, moduleName, actionType, args, result, null);
            }
            return result;
        } catch (Throwable exception) {
            if (!moduleName.equals(STR_UNDEFINED)) {
                this.log(joinPoint, startTime, moduleName, actionType, args, null, exception);
            }
            throw exception;
        } finally {
//            clearThreadLocal();
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Annotation> T getMethodAnnotation(ProceedingJoinPoint joinPoint,
                                                                Class<T> annotationClass
    ) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(annotationClass);
    }

    private void log(ProceedingJoinPoint joinPoint,
                     LocalDateTime startTime,
                     String moduleName,
                     String actionType,
                     Object[] args,
                     Object result,
                     Throwable exception
    ) {
        try {

            // 真正记录操作日志
            this.log0(joinPoint, startTime, moduleName, actionType, args, result, exception);
        } catch (Throwable ex) {
            log.error(
                    "[log][记录操作日志时，发生异常，其中参数是 joinPoint({}) operateLog({}) apiOperation({}) result({}) ]",
                    joinPoint,
                    result,
                    exception,
                    ex
            );
        }
    }

    private void log0(ProceedingJoinPoint joinPoint,
                      LocalDateTime startTime,
                      String moduleName,
                      String actionType,
                      Object[] args,
                      Object result,
                      Throwable exception
    ) {

        try {
            OperateLogParam operateLogParam = new OperateLogParam();


            if (WebFrameworkUtils.getHeaderUserId() != null) {
                operateLogParam.setOperatorId(WebFrameworkUtils.getHeaderUserId());
            }
            operateLogParam.setTenantId(WebFrameworkUtils.getHeaderTenantId());

            //获取到ServletRequestAttributes 里面有
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            //获取到Request对象
            HttpServletRequest request = attrs.getRequest();

            LocalDateTime now = LocalDateTime.now();
            Duration between = Duration.between(startTime, now);
            long costTime = between.toMillis();

            operateLogParam.setOperateTime(new Date());

            operateLogParam.setModuleName(moduleName);
            operateLogParam.setActionType(actionType);
            if (exception == null) {
                operateLogParam.setStatus(SUCCESS);
            } else {
                operateLogParam.setStatus(FAIL);
            }
            if (exception != null) {
                operateLogParam.setFailureCause("");
                StringWriter sw = new StringWriter();
                try (PrintWriter pw = new PrintWriter(sw);) {
                    exception.printStackTrace(pw);
                }
                String errorInfo = sw.toString();
                operateLogParam.setExceptionInfo(errorInfo);
            }

            operateLogParam.setRequestIp(WebUtil.getIpFromRequest(request));
            UserAgent agent = WebUtil.getAgent(request);
            if (agent != null) {
                operateLogParam.setRequestBrowser(agent.getBrowser() + "");
                operateLogParam.setRequestBrowserVersion(agent.getVersion());
            }

            operateLogParam.setRequestUrl(request.getRequestURI());
            MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
            Method method = methodSig.getMethod();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            List<Object> annotatedArgs = new ArrayList<>();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                if (parameterAnnotations[i] == null) {
                    continue;
                }
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof RequestBody || annotation instanceof PathVariable || annotation instanceof RequestParam || annotation instanceof RequestHeader) {
                        annotatedArgs.add(args[i]);
                        break;
                    }
                }
            }

            String argsStr = JSONUtil.toJsonStr(annotatedArgs);
            if (argsStr.length() > 995) {
                argsStr = argsStr.substring(0, 995);
            }
            operateLogParam.setRequestParam(argsStr);

            operateLogParam.setRequestProcotol(request.getProtocol());
            operateLogParam.setRequestMethod(request.getMethod());
            operateLogParam.setTraceId("");
            operateLogParam.setCostTime(costTime);
            operateLogParam.setRemark("");
            operateLogParam.setIds(Lists.newArrayList());

            // 补全通用字段
            operateLogParam.setTraceId(null);

            // 补充用户信息

            operateLogService.save(operateLogParam);

        } catch (Exception e) {
            log.error("日志记录异常", e);
        }
    }
}
