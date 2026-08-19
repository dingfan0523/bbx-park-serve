package com.cgnpc.dingtalk.component;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgnpc.cud.cache.redis.RedisUtil;
import com.cgnpc.cud.psc.client.sdk.config.WfAuthConfig;
import com.cgnpc.dingtalk.model.DingTalkWorkNoticeInputVO;
import com.cgnpc.dingtalk.model.WarningExceptionVO;
import com.cgnpc.dingtalk.model.WorkNotice;
import com.cgnpc.dingtalk.service.AsyncWarningMsgService;
import com.cgnpc.dingtalk.util.PathMatchingResourcePatternResolverUtil;
import com.cgnpc.mobile.config.DtalkProperties;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;


/**
 * 用途说明: 获取调用的接口
 * 作者姓名: P633860
 * 创建时间: 2023/6/25
 */

@Component
@Aspect
@AllArgsConstructor
@SuppressWarnings("all")
public class WarningExceptionAspect {

    private final AsyncWarningMsgService asyncWarningMsgService;
    private final WfAuthConfig wfAuthConfig;
    private final DtalkProperties dtalkProperties;

    @Pointcut("execution(* com.cgn.psc.service.*.*(..)) || execution(* com.cgnpc.cud.psc.client.sdk.service.*.*(..))")

    public void warning() {
    }

    @Around("warning()")
    public Object handleControllerMethod(ProceedingJoinPoint point) {

        Object result = null;
        HttpServletRequest request = null;
        long startTime = System.currentTimeMillis();
        try {
            result = point.proceed();
            if (dtalkProperties != null && dtalkProperties.getEnable()) {
                request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String resultJsonString = JSON.toJSONString(result);
                JSONObject data = JSON.parseObject(resultJsonString);
                MethodSignature methodSignature = (MethodSignature) point.getSignature();
                Method method = methodSignature.getMethod();
                String[] parameterNames = methodSignature.getParameterNames();
                Object[] param = point.getArgs();
                StringBuffer str = new StringBuffer();
                if (parameterNames != null) {
                    for (int i = 0; i < parameterNames.length; i++) {
                        if (null != param[i]) {
                            str.append(param[i].toString());
                        }
                    }
                }
                WarningExceptionVO vo = new WarningExceptionVO();
                vo.setUrl(request.getRequestURL().toString());
                vo.setMethod(request.getMethod());
                vo.setFunction(method.getName());
                vo.setParams(str.toString());
                vo.setTime(new Date());
                vo.setCode(data.getString("code"));
                vo.setResult(result.toString());

                // 执行时长
                long useTime = System.currentTimeMillis() - startTime;

                //String function = wevo.getFunction(); //获取方法名
                String[] urlArray = vo.getUrl().split("/");
                String function = urlArray[urlArray.length - 1];//使用请求url的方法名
                String completeApiUrl = "";
                for (Object apiObj : PathMatchingResourcePatternResolverUtil.getApiRequests()) {
                    if (apiObj.toString().indexOf(function) > 0) {
                        completeApiUrl = wfAuthConfig.getAuthInfo().getPscUrl() + apiObj.toString();
                        break;
                    }
                }
                if (StrUtil.isBlank(completeApiUrl) && buildApiRequestLine().get(function) != null) {
                    completeApiUrl = wfAuthConfig.getAuthInfo().getPscUrl() + buildApiRequestLine().get(function);
                }
                if ("queryTask".equals(vo.getFunction()) || "submitProcess".equals(vo.getFunction())) {
                    RedisUtil.healthStatus();
                }
                if (StrUtil.isNotBlank(completeApiUrl)) {
                    List<String> successCodes = Arrays.asList("200", "0");
                    //请求>=1分钟，钉钉发送消息通知
                    if (useTime >= 60000 || !successCodes.contains(vo.getCode())) {
                        String warningCacheKey = "warning:psc:" + completeApiUrl;
                        if (RedisUtil.get(warningCacheKey) == null) {
                            DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO = new DingTalkWorkNoticeInputVO();
                            dingTalkWorkNoticeInputVO.setWorkNotice(WorkNotice.builder()
                                    .title(completeApiUrl)
                                    .content("请求接口：" + completeApiUrl +
                                            "\n响应时间：" + useTime + "ms" +
                                            "\n请求类型：" + vo.getMethod() +
                                            "\n请求参数：" + vo.getParams() +
                                            "\n返回参数：" + resultJsonString).build());
                            asyncWarningMsgService.sendWorkNotice(dingTalkWorkNoticeInputVO);
                            RedisUtil.set(warningCacheKey, 1, 3600);
                        }
                    }
                }
            }
        } catch (Throwable e) {

        }

        return result;
    }

    /**
     * 请求接口名与psc接口名不一致，构建对应关系
     *
     * @return
     */
    private HashMap buildApiRequestLine() {
        HashMap map = new HashMap<>();
        map.put("searchPSCProcessContent", "/flowDesigner/getProcModelById");
        map.put("pubProcDraftUpVersion", "/flowDesigner/getProcModelById");
        return map;
    }

}
