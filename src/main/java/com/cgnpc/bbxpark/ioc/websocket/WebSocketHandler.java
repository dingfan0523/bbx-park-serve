package com.cgnpc.bbxpark.ioc.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

/**
 * WebSocket处理器
 * 用于处理WebSocket连接、消息收发等
 * 支持租户隔离和认证校验
 */
@ServerEndpoint(value = "/ws/dtwin/{userId}", configurator = WebSocketConfigurator.class)
@Component
public class WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    
    // 注入依赖（通过WebSocketConfigurator注入）
    private static TenantConnectionManager tenantConnectionManager;
    private static TokenValidator tokenValidator;


    
    // 实例变量
    private String userId;
    private String tenantId;
    private Session session;
    
    /**
     * 连接建立成功时调用
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("userId") String pathUserId) {
        try {
            logger.info("=== WebSocket连接开始 ===");
            
            // 从配置中获取认证信息
            Map<String, Object> userProperties = config.getUserProperties();
            String accessToken = (String) userProperties.get("accessToken");

            logger.info("连接参数 - userId: {}, tenantId: {}, accessToken: {}",
                    pathUserId, userProperties.get("tenantId"), accessToken);

            // 校验用户是否已有连接，如有则关闭旧连接
            if (tenantConnectionManager.isConnected(pathUserId)) {
                logger.warn("用户{}已存在连接，关闭旧连接", pathUserId);
                Session oldSession = tenantConnectionManager.getSessionByUser(pathUserId);
                if (oldSession != null && oldSession.isOpen()) {
                    oldSession.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "新连接建立"));
                    logger.info("旧连接已关闭 - userId: {}", pathUserId);
                }
            }

            // 校验通过设置实例变量
            // 从路径参数获取userId
            this.userId = pathUserId;
            this.session = session;
            this.tenantId = (String) userProperties.get("tenantId");

            // 进行完整的认证验证
            if (!validateConnection(accessToken)) {
                logger.error("WebSocket认证失败 - userId: {}", userId);
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "认证失败"));
                return;
            }
            
            // 确保tenantId不为空再注册连接
//            if (tenantId == null || tenantId.isEmpty()) {
//                logger.error("WebSocket连接tenantId为空，拒绝连接 - userId: {}", userId);
//                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "租户ID不能为空"));
//                return;
//            }

            // 注册连接到租户管理器
            tenantConnectionManager.registerConnection(userId, tenantId, session);

            logger.info("WebSocket连接建立成功 - userId: {}, tenantId: {}, 当前租户连接数: {}",
                       userId, tenantId, tenantConnectionManager.getConnectionCountByTenant(tenantId));

            // 发送连接成功的标准化消息
            sendMessage(WebSocketMessage.connectSuccess(userId));
            logger.info("=== WebSocket连接完成 ===");
            
        } catch (Exception e) {
            logger.error("WebSocket连接建立失败 - userId: {}", userId, e);
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "连接建立失败"));
            } catch (IOException ioException) {
                logger.error("关闭WebSocket连接失败", ioException);
            }
        }
    }

    /**
     * 接收到客户端消息时调用
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.debug("来自用户{}的消息: {}", userId, message);

        try {
            // 解析标准化消息格式
            WebSocketMessage webSocketMessage = WebSocketMessage.fromJson(message);
            if (webSocketMessage == null) {
                sendMessage(WebSocketMessage.error("消息格式错误"));
                return;
            }

            // 根据消息类型处理
            switch (webSocketMessage.getType()) {
                case WebSocketMessage.MessageType.HEARTBEAT:
                    // 心跳消息，返回确认
                    sendMessage(WebSocketMessage.heartbeatResponse());
                    break;
                case WebSocketMessage.MessageType.DEVICE_STATUS:
                    // 设备状态查询
                    sendDeviceStatusToUser();
                    break;
                case WebSocketMessage.MessageType.BROADCAST:
                    // 广播消息给同租户的所有用户
                    broadcastToTenant(webSocketMessage.getMsg());
                    break;
                default:
                    // 默认处理，回显消息
                    sendMessage(new WebSocketMessage("echo", 
                        "服务器收到: " + webSocketMessage.getMsg()));
                    break;
            }
        } catch (Exception e) {
            logger.error("处理消息时发生错误 - userId: {}", userId, e);
            sendMessage(WebSocketMessage.error("消息处理失败"));
        }
    }

    /**
     * 连接关闭时调用
     */
    @OnClose
    public void onClose() {
        try {
            // 增加判空
            if (userId != null) {

                // 从租户管理器中移除连接
                tenantConnectionManager.removeConnection(userId);
            }

            // 只有当tenantId不为空时才查询连接数
            if (tenantId != null && !tenantId.isEmpty()) {
                logger.info("WebSocket连接关闭 - userId: {}, tenantId: {}, 剩余租户连接数: {}", 
                           userId, tenantId, tenantConnectionManager.getConnectionCountByTenant(tenantId));
            } else {
                logger.info("WebSocket连接关闭 - userId: {}, tenantId: null", userId);
            }
                        
        } catch (Exception e) {
            logger.error("WebSocket连接关闭处理异常 - userId: {}", userId, e);
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("WebSocket发生错误 - userId: {}", userId, error);
        try {
            sendMessage(WebSocketMessage.error("服务器内部错误"));
        } catch (Exception e) {
            logger.error("发送错误消息失败", e);
        }
    }

    // 静态方法用于外部调用
    
    /**
     * 验证连接的合法性
     * @param accessToken 访问令牌
     * @return true表示验证通过，false表示验证失败
     */
    private boolean validateConnection(String accessToken) {
        logger.info("开始验证WebSocket连接 - userId: {}, tenantId: {}", userId, tenantId);

        // 检查必要参数
        if (userId == null || userId.isEmpty()) {
            logger.warn("WebSocket连接缺少用户ID参数");
            return false;
        }

        if (tenantId == null || tenantId.isEmpty()) {
            logger.warn("WebSocket连接缺少租户ID参数");
            return false;
        }

        // 验证AccessToken
        if (accessToken == null || accessToken.isEmpty()) {
            logger.warn("WebSocket连接缺少AccessToken");
            return false;
        }

        // 调用TokenValidator进行token验证
        if (tokenValidator != null) {
            boolean tokenValid = tokenValidator.validateToken(accessToken, userId);
            if (!tokenValid) {
                logger.warn("WebSocket连接token验证失败 - userId: {}, token: [PROVIDED]", userId);
                return false;
            }
        } else {
            logger.warn("TokenValidator未初始化，跳过token验证");
        }
        
        logger.info("WebSocket连接验证通过 - userId: {}, tenantId: {}", userId, tenantId);
        return true;
    }
    
    /**
     * 发送消息给指定用户
     */
    public static void sendMessageToUser(String userId, WebSocketMessage message) {
        Session session = tenantConnectionManager.getSessionByUser(userId);
        if (session != null && session.isOpen()) {
            sendMessage(session, message);
        }
    }

    /**
     * 广播消息给指定租户的所有用户
     */
    public static void broadcastToTenant(String tenantId, WebSocketMessage message) {
        tenantConnectionManager.getUsersByTenant(tenantId).forEach(userId -> {
            sendMessageToUser(userId, message);
        });
    }

    /**
     * 广播消息给所有连接用户
     */
    public static void broadcastToAll(WebSocketMessage message) {
        // 遍历所有租户进行广播
        // 实际项目中可根据需要调整
    }

    /**
     * 发送消息给单个会话
     */
    private static void sendMessage(Session session, WebSocketMessage message) {
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    session.getBasicRemote().sendText(message.toJson());
                }
            } catch (IOException e) {
                logger.error("发送WebSocket消息失败", e);
            }
        }
    }

    /**
     * 发送消息给当前用户
     */
    private void sendMessage(WebSocketMessage message) {
        sendMessage(this.session, message);
    }

    /**
     * 广播消息给当前用户所在租户
     */
    private void broadcastToTenant(Object messageContent) {
        WebSocketMessage broadcastMessage = new WebSocketMessage(
            WebSocketMessage.MessageType.BROADCAST, 
            new BroadcastPayload(userId, messageContent)
        );
        broadcastToTenant(tenantId, broadcastMessage);
    }

    /**
     * 模拟发送设备状态给当前用户
     */
    private void sendDeviceStatusToUser() {
        DeviceStatus status = new DeviceStatus();
        status.setCameras(2);
        status.setNvr(1);
        status.setOnline(true);
        status.setLastUpdate(java.time.LocalDateTime.now().toString());
        
        sendMessage(WebSocketMessage.deviceStatus(status));
    }

    // 内部数据类
    
    /**
     * 广播消息负载
     */
    public static class BroadcastPayload {
        private String from;
        private Object content;
        
        public BroadcastPayload() {}
        
        public BroadcastPayload(String from, Object content) {
            this.from = from;
            this.content = content;
        }
        
        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
        public Object getContent() { return content; }
        public void setContent(Object content) { this.content = content; }
    }
    
    /**
     * 设备状态数据类
     */
    public static class DeviceStatus {
        private Integer cameras;
        private Integer nvr;
        private Boolean online;
        private String lastUpdate;
        
        // Getters and Setters
        public Integer getCameras() { return cameras; }
        public void setCameras(Integer cameras) { this.cameras = cameras; }
        public Integer getNvr() { return nvr; }
        public void setNvr(Integer nvr) { this.nvr = nvr; }
        public Boolean getOnline() { return online; }
        public void setOnline(Boolean online) { this.online = online; }
        public String getLastUpdate() { return lastUpdate; }
        public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    }
    
    // 依赖注入方法（由WebSocketConfigurator调用）
    public static void setTenantConnectionManager(TenantConnectionManager manager) {
        tenantConnectionManager = manager;
    }
    
    public static void setTokenValidator(TokenValidator validator) {
        tokenValidator = validator;
    }
}