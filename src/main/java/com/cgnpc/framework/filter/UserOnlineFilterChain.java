package com.cgnpc.framework.filter;

import cn.hutool.core.util.StrUtil;
import com.cgnpc.cud.cache.redis.RedisUtil;
import com.cgnpc.cud.core.common.util.DateUtils;
import com.cgnpc.cud.core.common.util.StringUtils;
import com.cgnpc.cud.core.support.TokenHolder;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/******************************
 * 用途说明: 记录用户在线状态，区分deviceType
 * 作者姓名: P633860
 * 创建时间: 2024/3/5
 ******************************/
@Component
public class UserOnlineFilterChain extends OncePerRequestFilter {

    @SuppressWarnings("all")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);

            /**活跃用户redis记录十分钟*/
            Map<String, Object> oumap           = new HashMap<>();
            String userAgent                    = request.getHeader("User-Agent").toLowerCase();
            String origin                       = request.getHeader("origin");
            List<String> deviceMeid = Arrays.asList("mobile","8011","mobileapp","newmobileapp");
            String userId = TokenHolder.getUserTokenDto().getUserID();
            if (StringUtils.isEmpty(userId)){
                Principal principal   = (Principal) SecurityUtils.getSubject().getPrincipal();
                userId = principal.getName();
            }
            String user_online_key              = StringUtils.format("online_users:{}", userId);
            Map<String, Object> getOumap        = new HashMap<>();
            boolean is_different_device_sources = false;

            if (RedisUtil.get(user_online_key) != null){
                getOumap = (Map<String, Object>) RedisUtil.get(user_online_key);
            }
            if (deviceMeid.stream().anyMatch(userAgent::contains) || (StrUtil.isNotBlank(origin) && deviceMeid.stream().anyMatch(origin::contains))){
                if (!getOumap.isEmpty() &&getOumap.get("mobile") != null){
                    long timestamp = Long.valueOf(getOumap.get("mobile").toString());
                    if (currentTimestamp() > timestamp){
                        oumap.put("mobile",delayedTimestamp());
                        is_different_device_sources = true;
                    }
                } else {
                    oumap.put("mobile",delayedTimestamp());
                    is_different_device_sources = true;
                }

                if (!getOumap.isEmpty() &&getOumap.get("pc") != null){
                    oumap.put("pc",getOumap.get("pc"));
                }
            } else {
                if (!getOumap.isEmpty() &&getOumap.get("pc") != null){
                    long timestamp = Long.valueOf(getOumap.get("pc").toString());
                    if (currentTimestamp() > timestamp){
                        oumap.put("pc",delayedTimestamp());
                        is_different_device_sources = true;
                    }
                } else {
                    oumap.put("pc",delayedTimestamp());
                    is_different_device_sources = true;
                }
                if (!getOumap.isEmpty() &&getOumap.get("mobile") != null){
                    oumap.put("mobile",getOumap.get("mobile"));
                }
            }
            if (RedisUtil.get(user_online_key) == null || is_different_device_sources){
                oumap.put("userId",userId);
                RedisUtil.set(user_online_key,oumap,600);
            }

        } catch (Exception ignored) { }
    }


    /**
     * 返回延迟十分钟 YYYYMMDDHHMMSS
     */
    public static long delayedTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime delayed = now.plusMinutes(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.YYYYMMDDHHMMSS);
        return Long.parseLong(delayed.format(formatter));
    }

    /**
     *  返回当前时间
     */
    public static long currentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.YYYYMMDDHHMMSS);
        return Long.parseLong(now.format(formatter));
    }

}
