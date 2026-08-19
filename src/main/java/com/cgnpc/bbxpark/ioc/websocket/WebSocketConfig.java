package com.cgnpc.bbxpark.ioc.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.PostConstruct;

/**
 * WebSocket配置类
 * 用于启用WebSocket支持和配置相关组件
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {

    @Autowired
    private TenantConnectionManager tenantConnectionManager;
    
    @Autowired
    private TokenValidator tokenValidator;

    /**
     * ServerEndpointExporter bean用于扫描所有带有@ServerEndpoint注解的bean
     * 这个Bean必须注册，否则WebSocket端点将不会被识别
     *
     * @return ServerEndpointExporter实例
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    
    /**
     * 初始化WebSocket组件依赖注入
     */
    @PostConstruct
    public void initWebSocketComponents() {
        // 为WebSocketHandler注入依赖
        WebSocketHandler.setTenantConnectionManager(tenantConnectionManager);
        WebSocketHandler.setTokenValidator(tokenValidator);
    }
}