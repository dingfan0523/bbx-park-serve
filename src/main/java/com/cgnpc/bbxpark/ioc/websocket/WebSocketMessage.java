package com.cgnpc.bbxpark.ioc.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebSocket统一消息格式
 * 标准化消息结构：{type:消息类型, msg:消息内容}
 */
public class WebSocketMessage {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketMessage.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        // 设置序列化时不包含null值
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    
    private String type;  // 消息类型
    private Object msg;   // 消息内容
    private Long timestamp; // 时间戳
    
    // 私有构造函数
    private WebSocketMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    // 构造函数
    public WebSocketMessage(String type, Object msg) {
        this();
        this.type = type;
        this.msg = msg;
    }
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Object getMsg() {
        return msg;
    }
    
    public void setMsg(Object msg) {
        this.msg = msg;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * 转换为JSON字符串
     * @return JSON字符串
     */
    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.error("WebSocket消息序列化失败", e);
            return "{\"type\":\"error\",\"msg\":\"消息序列化失败\"}";
        }
    }
    
    /**
     * 从JSON字符串解析
     * @param json JSON字符串
     * @return WebSocketMessage对象
     */
    public static WebSocketMessage fromJson(String json) {
        try {
            return objectMapper.readValue(json, WebSocketMessage.class);
        } catch (JsonProcessingException e) {
            logger.error("WebSocket消息反序列化失败: {}", json, e);
            return null;
        }
    }
    
    // 预定义的消息类型常量
    public static class MessageType {
        public static final String CONNECT = "connect";              // 连接建立
        public static final String DISCONNECT = "disconnect";        // 连接断开
        public static final String HEARTBEAT = "heartbeat";          // 心跳
        public static final String HEARTBEAT_RESPONSE = "heartbeat_response"; // 心跳响应
        public static final String BROADCAST = "broadcast";          // 广播消息
        public static final String DEVICE_STATUS = "device_status";  // 设备状态
        public static final String ALARM = "alarm";                  // 告警消息
        public static final String SYSTEM_NOTICE = "system_notice";  // 系统通知
        public static final String NOTIFY = "notify";                // 普通通知消息
        public static final String DATA_UPDATE = "data_update";      // 数据更新
        public static final String ERROR = "error";                  // 错误消息
        public static final String SUCCESS = "success";              // 成功消息
    }
    
    // 预定义的系统消息构建方法
    public static WebSocketMessage connectSuccess(String userId) {
        return new WebSocketMessage(MessageType.CONNECT, 
            String.format("用户%s连接成功", userId));
    }
    
    public static WebSocketMessage heartbeatResponse() {
        return new WebSocketMessage(MessageType.HEARTBEAT_RESPONSE, "pong");
    }
    
    public static WebSocketMessage deviceStatus(Object statusData) {
        return new WebSocketMessage(MessageType.DEVICE_STATUS, statusData);
    }
    
    public static WebSocketMessage alarm(Object alarmData) {
        return new WebSocketMessage(MessageType.ALARM, alarmData);
    }
    
    public static WebSocketMessage systemNotice(String notice) {
        return new WebSocketMessage(MessageType.SYSTEM_NOTICE, notice);
    }
    
    public static WebSocketMessage dataUpdate(String dataType, Object data) {
        return new WebSocketMessage(MessageType.DATA_UPDATE, 
            new DataUpdatePayload(dataType, data));
    }
    
    public static WebSocketMessage error(String errorMsg) {
        return new WebSocketMessage(MessageType.ERROR, errorMsg);
    }
    
    public static WebSocketMessage success(String successMsg) {
        return new WebSocketMessage(MessageType.SUCCESS, successMsg);
    }
    
    /**
     * 数据更新负载类
     */
    public static class DataUpdatePayload {
        private String dataType;
        private Object data;
        
        public DataUpdatePayload() {}
        
        public DataUpdatePayload(String dataType, Object data) {
            this.dataType = dataType;
            this.data = data;
        }
        
        public String getDataType() {
            return dataType;
        }
        
        public void setDataType(String dataType) {
            this.dataType = dataType;
        }
        
        public Object getData() {
            return data;
        }
        
        public void setData(Object data) {
            this.data = data;
        }
    }
    
    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "type='" + type + '\'' +
                ", msg=" + msg +
                ", timestamp=" + timestamp +
                '}';
    }
}