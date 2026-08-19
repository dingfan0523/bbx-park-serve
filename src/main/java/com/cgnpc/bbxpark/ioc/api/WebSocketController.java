package com.cgnpc.bbxpark.ioc.api;

import com.cgnpc.bbxpark.ioc.service.WebSocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket演示控制器
 * 展示如何使用WebSocket服务
 */
@Api(tags = "WebSocket演示接口")
@RestController
@RequestMapping("/api/ws")
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;

    /**
     * 向指定用户发送消息
     *
     * @param userId  目标用户ID
     * @param message 要发送的消息
     * @return 操作结果
     */
    @ApiOperation(value = "向指定用户发送消息")
    @PostMapping("/sendToUser")
    public Map<String, Object> sendToUser(@RequestParam String userId, @RequestParam String message) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            webSocketService.sendMessageToUser(userId, message, "demo_message");
            result.put("success", true);
            result.put("message", "消息已发送给用户: " + userId);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送消息失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 广播消息给所有连接的用户
     *
     * @param message 要广播的消息
     * @return 操作结果
     */
    @ApiOperation(value = "广播消息给所有用户")
    @PostMapping("/broadcast")
    public Map<String, Object> broadcast(@RequestParam String message) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            webSocketService.broadcastToAll(message, "demo_broadcast");
            result.put("success", true);
            result.put("message", "消息已广播");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "广播消息失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 发送设备状态更新
     *
     * @param userId     目标用户ID
     * @param deviceType 设备类型
     * @param deviceId   设备ID
     * @param status     设备状态
     * @return 操作结果
     */
    @ApiOperation(value = "发送设备状态更新")
    @PostMapping("/sendDeviceStatus")
    public Map<String, Object> sendDeviceStatus(
            @RequestParam String userId,
            @RequestParam String deviceType,
            @RequestParam String deviceId,
            @RequestParam String status) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            webSocketService.sendDeviceStatusUpdate(userId, deviceType, deviceId, status);
            result.put("success", true);
            result.put("message", "设备状态已发送");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送设备状态失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 发送报警通知
     *
     * @param userId    目标用户ID
     * @param alarmType 报警类型
     * @param alarmInfo 报警信息
     * @return 操作结果
     */
    @ApiOperation(value = "发送报警通知")
    @PostMapping("/sendAlarm")
    public Map<String, Object> sendAlarm(
            @RequestParam String userId,
            @RequestParam String alarmType,
            @RequestParam String alarmInfo) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            webSocketService.sendAlarmNotification(userId, alarmType, alarmInfo);
            result.put("success", true);
            result.put("message", "报警通知已发送");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送报警通知失败: " + e.getMessage());
        }
        
        return result;
    }
}