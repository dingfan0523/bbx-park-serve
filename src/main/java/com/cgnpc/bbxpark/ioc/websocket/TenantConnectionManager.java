package com.cgnpc.bbxpark.ioc.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Set;

/**
 * 租户连接管理器
 * 用于管理按租户分组的WebSocket连接
 */
@Component
public class TenantConnectionManager {
    
    private static final Logger logger = LoggerFactory.getLogger(TenantConnectionManager.class);
    
    // 租户连接映射：tenantId -> Set<userId>
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<String>> tenantConnections = new ConcurrentHashMap<>();
    
    // 用户会话映射：userId -> Session
    private final ConcurrentHashMap<String, Session> userSessions = new ConcurrentHashMap<>();
    
    // 用户租户映射：userId -> tenantId
    private final ConcurrentHashMap<String, String> userTenants = new ConcurrentHashMap<>();
    
    /**
     * 注册用户的连接
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param session WebSocket会话
     */
    public void registerConnection(String userId, String tenantId, Session session) {
        try {
            // 更新用户会话映射
            userSessions.put(userId, session);
            
            // 更新用户租户映射
            userTenants.put(userId, tenantId);
            
            // 更新租户连接映射
            tenantConnections.computeIfAbsent(tenantId, k -> new CopyOnWriteArraySet<>()).add(userId);
            
            logger.info("用户连接已注册 - userId: {}, tenantId: {}, 当前租户连接数: {}", 
                       userId, tenantId, getConnectionCountByTenant(tenantId));
                       
        } catch (Exception e) {
            logger.error("注册用户连接失败 - userId: {}, tenantId: {}", userId, tenantId, e);
        }
    }
    
    /**
     * 移除用户的连接
     * @param userId 用户ID
     */
    public void removeConnection(String userId) {
        try {
            Session session = userSessions.remove(userId);
            String tenantId = userTenants.remove(userId);
            
            if (tenantId != null && tenantConnections.containsKey(tenantId)) {
                Set<String> tenantUsers = tenantConnections.get(tenantId);
                tenantUsers.remove(userId);
                
                // 如果租户没有其他连接，清理租户条目
                if (tenantUsers.isEmpty()) {
                    tenantConnections.remove(tenantId);
                }
                
                logger.info("用户连接已移除 - userId: {}, tenantId: {}, 剩余租户连接数: {}", 
                           userId, tenantId, getConnectionCountByTenant(tenantId));
            }
            
        } catch (Exception e) {
            logger.error("移除用户连接失败 - userId: {}", userId, e);
        }
    }
    
    /**
     * 获取指定租户的所有连接用户ID
     * @param tenantId 租户ID
     * @return 用户ID集合
     */
    public Set<String> getUsersByTenant(String tenantId) {
        return tenantConnections.getOrDefault(tenantId, new CopyOnWriteArraySet<>());
    }
    
    /**
     * 获取指定租户的连接数
     * @param tenantId 租户ID
     * @return 连接数
     */
    public int getConnectionCountByTenant(String tenantId) {
        Set<String> users = tenantConnections.get(tenantId);
        return users != null ? users.size() : 0;
    }
    
    /**
     * 获取用户对应的租户ID
     * @param userId 用户ID
     * @return 租户ID，如果用户不存在则返回null
     */
    public String getTenantIdByUser(String userId) {
        return userTenants.get(userId);
    }
    
    /**
     * 获取用户的会话
     * @param userId 用户ID
     * @return WebSocket会话，如果用户不存在则返回null
     */
    public Session getSessionByUser(String userId) {
        return userSessions.get(userId);
    }
    
    /**
     * 检查用户是否存在连接
     * @param userId 用户ID
     * @return true表示用户已连接，false表示未连接
     */
    public boolean isConnected(String userId) {
        return userSessions.containsKey(userId);
    }
    
    /**
     * 获取系统总连接数
     * @return 总连接数
     */
    public int getTotalConnectionCount() {
        return userSessions.size();
    }
    
    /**
     * 获取租户总数
     * @return 租户数量
     */
    public int getTenantCount() {
        return tenantConnections.size();
    }
    
    /**
     * 清理所有连接（系统关闭时使用）
     */
    public void clearAllConnections() {
        userSessions.clear();
        userTenants.clear();
        tenantConnections.clear();
        logger.info("所有WebSocket连接已清理");
    }
}