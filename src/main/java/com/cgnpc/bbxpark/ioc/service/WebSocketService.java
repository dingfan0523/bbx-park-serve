package com.cgnpc.bbxpark.ioc.service;

import com.cgnpc.bbxpark.ioc.dto.model.ScreenMessageModel;
import com.cgnpc.bbxpark.ioc.websocket.TenantConnectionManager;
import com.cgnpc.bbxpark.ioc.websocket.WebSocketHandler;
import com.cgnpc.bbxpark.ioc.websocket.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket服务管理类
 * 提供从应用程序其他部分向WebSocket客户端发送消息的接口
 * 支持租户隔离和标准化消息格式
 */
@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
    
    @Autowired
    private TenantConnectionManager tenantConnectionManager;

    /**
     * 向指定用户发送消息
     *
     * @param userId  用户ID
     * @param message 要发送的消息内容
     * @param type    消息类型
     */
    public void sendMessageToUser(String userId, Object message, String type) {
        try {
            WebSocketMessage webSocketMessage = new WebSocketMessage(type, message);
            WebSocketHandler.sendMessageToUser(userId, webSocketMessage);
            logger.debug("向用户{}发送消息: {}", userId, webSocketMessage.toJson());
        } catch (Exception e) {
            logger.error("向用户{}发送消息失败", userId, e);
        }
    }
    
    /**
     * 向指定用户发送标准化消息
     *
     * @param userId  用户ID
     * @param webSocketMessage WebSocket消息对象
     */
    public void sendMessageToUser(String userId, WebSocketMessage webSocketMessage) {
        try {
            WebSocketHandler.sendMessageToUser(userId, webSocketMessage);
            logger.debug("向用户{}发送消息: {}", userId, webSocketMessage.toJson());
        } catch (Exception e) {
            logger.error("向用户{}发送消息失败", userId, e);
        }
    }

    /**
     * 向指定租户的所有用户广播消息
     *
     * @param tenantId 租户ID
     * @param message  要广播的消息内容
     * @param type     消息类型
     */
    public void broadcastToTenant(String tenantId, Object message, String type) {
        try {
            WebSocketMessage webSocketMessage = new WebSocketMessage(type, message);
            WebSocketHandler.broadcastToTenant(tenantId, webSocketMessage);
            logger.debug("向租户{}广播消息: {}", tenantId, webSocketMessage.toJson());
        } catch (Exception e) {
            logger.error("向租户{}广播消息失败", tenantId, e);
        }
    }
    
    /**
     * 向指定租户广播标准化消息
     *
     * @param tenantId 租户ID
     * @param webSocketMessage WebSocket消息对象
     */
    public void broadcastToTenant(String tenantId, WebSocketMessage webSocketMessage) {
        try {
            WebSocketHandler.broadcastToTenant(tenantId, webSocketMessage);
            logger.debug("向租户{}广播消息: {}", tenantId, webSocketMessage.toJson());
        } catch (Exception e) {
            logger.error("向租户{}广播消息失败", tenantId, e);
        }
    }

    /**
     * 广播消息给所有连接的用户
     *
     * @param message 要广播的消息内容
     * @param type    消息类型
     */
    public void broadcastToAll(Object message, String type) {
        try {
            WebSocketMessage webSocketMessage = new WebSocketMessage(type, message);
            // 遍历所有租户进行广播
            tenantConnectionManager.getTenantCount(); // 触发租户信息加载
            WebSocketHandler.broadcastToAll(webSocketMessage);
            logger.debug("广播全局消息: {}", webSocketMessage.toJson());
        } catch (Exception e) {
            logger.error("广播全局消息失败", e);
        }
    }
    
    /**
     * 发送系统通知
     *
     * @param tenantId 租户ID
     * @param notice   通知内容
     */
    public void sendSystemNotice(String tenantId, String notice) {
        broadcastToTenant(tenantId, notice, WebSocketMessage.MessageType.SYSTEM_NOTICE);
    }
    
    /**
     * 发送设备状态更新
     *
     * @param tenantId 租户ID
     * @param statusData 状态数据
     */
    public void sendDeviceStatusUpdate(String tenantId, Object statusData) {
        broadcastToTenant(tenantId, statusData, WebSocketMessage.MessageType.DEVICE_STATUS);
    }
    
    /**
     * 发送告警消息
     *
     * @param tenantId 租户ID
     * @param alarmData 告警数据
     */
    public void sendAlarmMessage(String tenantId, Object alarmData) {
        broadcastToTenant(tenantId, alarmData, WebSocketMessage.MessageType.ALARM);
    }
    
    /**
     * 发送数据更新消息
     *
     * @param tenantId 租户ID
     * @param dataType 数据类型
     * @param data     数据内容
     */
    public void sendDataUpdate(String tenantId, String dataType, Object data) {
        WebSocketMessage.DataUpdatePayload payload = 
            new WebSocketMessage.DataUpdatePayload(dataType, data);
        broadcastToTenant(tenantId, payload, WebSocketMessage.MessageType.DATA_UPDATE);
    }
    
    /**
     * 检查用户是否在线
     *
     * @param userId 用户ID
     * @return true表示在线，false表示离线
     */
    public boolean isUserOnline(String userId) {
        return tenantConnectionManager.isConnected(userId);
    }
    
    /**
     * 获取指定租户的在线用户数
     *
     * @param tenantId 租户ID
     * @return 在线用户数
     */
    public int getOnlineUserCount(String tenantId) {
        return tenantConnectionManager.getConnectionCountByTenant(tenantId);
    }
    
    /**
     * 获取系统总连接数
     *
     * @return 总连接数
     */
    public int getTotalConnectionCount() {
        return tenantConnectionManager.getTotalConnectionCount();
    }
    
    /**
     * 获取租户总数
     *
     * @return 租户数量
     */
    public int getTenantCount() {
        return tenantConnectionManager.getTenantCount();
    }

    /**
     * 发送设备状态更新消息
     *
     * @param userId      用户ID
     * @param deviceType  设备类型 (如 "camera", "nvr", "dvr")
     * @param deviceId    设备ID
     * @param status      设备状态
     */
    public void sendDeviceStatusUpdate(String userId, String deviceType, String deviceId, String status) {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("deviceType", deviceType);
        messageData.put("deviceId", deviceId);
        messageData.put("status", status);
        messageData.put("timestamp", System.currentTimeMillis());
        
        sendMessageToUser(userId, messageData, "device_status_update");
    }

    /**
     * 发送报警消息
     *
     * @param userId     用户ID
     * @param alarmType  报警类型
     * @param alarmInfo  报警信息
     */
    public void sendAlarmNotification(String userId, String alarmType, String alarmInfo) {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("alarmType", alarmType);
        messageData.put("alarmInfo", alarmInfo);
        messageData.put("timestamp", System.currentTimeMillis());
        
        sendMessageToUser(userId, messageData, "alarm_notification");
    }

    /**
     * 发送系统通知
     *
     * @param message 通知内容
     */
    public void sendSystemNotification(String message) {
        broadcastToAll(message, "system_notification");
    }
    
    /**
     * 发送大屏通知消息
     *
     * @param tenantId 租户ID
     * @param screenMessage 大屏消息模型
     */
    public void sendNotifyToScreen(String tenantId, ScreenMessageModel screenMessage) {
        try {
            // 设置消息时间和ID
            if (screenMessage.getDate() == null) {
                screenMessage.setDate(new Date());
            }
            if (screenMessage.getId() == null) {
                screenMessage.setId(System.currentTimeMillis());
            }
            
            // 使用NOTIFY类型广播消息
            broadcastToTenant(tenantId, screenMessage, WebSocketMessage.MessageType.NOTIFY);
            logger.info("向租户{}发送大屏通知消息: {}", tenantId, screenMessage.getTitle());
        } catch (Exception e) {
            logger.error("向租户{}发送大屏通知消息失败", tenantId, e);
        }
    }
}