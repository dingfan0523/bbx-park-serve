package com.cgnpc.bbxpark.ioc.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * WebSocket配置器
 * 用于注入Spring Bean到WebSocket端点
 */
@Component
public class WebSocketConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfigurator.class);
    
    private static volatile ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebSocketConfigurator.applicationContext = applicationContext;
    }
    
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 将Spring上下文传递给WebSocket端点
        sec.getUserProperties().put("applicationContext", applicationContext);
        
        // 从URL查询参数获取认证信息
        String accessToken = getQueryParam(request, "accessToken");
        String tenantId = getQueryParam(request, "tenantId");
        
        // 从路径参数获取userId（用于WebSocket路径 /ws/dtwin/{userId}）
        // userId会在WebSocketHandler中通过@PathParam获取
        
        // 传递认证信息到WebSocket端点
        if (accessToken != null) {
            sec.getUserProperties().put("accessToken", accessToken);
        }
        if (tenantId != null) {
            sec.getUserProperties().put("tenantId", tenantId);
        }
        
        logger.info("WebSocket握手配置 - accessToken: {}, tenantId: {}", 
                   accessToken != null ? accessToken : "null",
                   tenantId != null ? tenantId : "null");
    }
    
    /**
     * 从URL查询参数中获取指定参数的值
     */
    private String getQueryParam(HandshakeRequest request, String paramName) {
        String queryString = request.getRequestURI().getQuery();
        if (queryString != null) {
            String[] params = queryString.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals(paramName)) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }
    
    /**
     * 获取Spring Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
    
    /**
     * 获取Spring Bean（按名称）
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
}