package org.example.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.api.service.WebSocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket消息控制器
 */
@Slf4j
@Controller
public class WebSocketController {

    @Autowired
    private WebSocketMessageService webSocketMessageService;

    /**
     * 处理客户端发送的消息
     *
     * @param message   消息内容
     * @param principal 用户信息
     * @return 响应消息
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Map<String, Object> sendMessage(@Payload Map<String, Object> message, Principal principal) {
        try {
            log.info("收到用户{}的消息：{}", principal != null ? principal.getName() : "匿名", message);
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "MESSAGE");
            response.put("content", message.get("content"));
            response.put("sender", principal != null ? principal.getName() : "匿名");
            response.put("timestamp", System.currentTimeMillis());
            
            return response;
        } catch (Exception e) {
            log.error("处理消息失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "ERROR");
            errorResponse.put("message", "消息发送失败");
            return errorResponse;
        }
    }

    /**
     * 处理用户加入聊天
     *
     * @param message   消息内容
     * @param principal 用户信息
     * @return 响应消息
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Map<String, Object> addUser(@Payload Map<String, Object> message, Principal principal) {
        try {
            String username = principal != null ? principal.getName() : (String) message.get("sender");
            log.info("用户{}加入聊天", username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "JOIN");
            response.put("sender", username);
            response.put("timestamp", System.currentTimeMillis());
            
            return response;
        } catch (Exception e) {
            log.error("处理用户加入失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "ERROR");
            errorResponse.put("message", "加入聊天失败");
            return errorResponse;
        }
    }

    /**
     * 处理用户离开聊天
     *
     * @param message   消息内容
     * @param principal 用户信息
     * @return 响应消息
     */
    @MessageMapping("/chat.removeUser")
    @SendTo("/topic/public")
    public Map<String, Object> removeUser(@Payload Map<String, Object> message, Principal principal) {
        try {
            String username = principal != null ? principal.getName() : (String) message.get("sender");
            log.info("用户{}离开聊天", username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "LEAVE");
            response.put("sender", username);
            response.put("timestamp", System.currentTimeMillis());
            
            return response;
        } catch (Exception e) {
            log.error("处理用户离开失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "ERROR");
            errorResponse.put("message", "离开聊天失败");
            return errorResponse;
        }
    }

    /**
     * 处理客服消息
     *
     * @param userId    用户ID
     * @param message   消息内容
     * @param principal 用户信息
     */
    @MessageMapping("/service/{userId}")
    public void sendServiceMessage(@DestinationVariable Long userId, @Payload Map<String, Object> message, Principal principal) {
        try {
            log.info("收到发送给用户{}的客服消息：{}", userId, message);
            
            Map<String, Object> serviceMessage = new HashMap<>();
            serviceMessage.put("type", "SERVICE_MESSAGE");
            serviceMessage.put("content", message.get("content"));
            serviceMessage.put("sender", principal != null ? principal.getName() : "客服");
            serviceMessage.put("timestamp", System.currentTimeMillis());
            
            webSocketMessageService.sendServiceMessageToUser(userId, serviceMessage);
        } catch (Exception e) {
            log.error("发送客服消息失败", e);
        }
    }

    /**
     * 处理私聊消息
     *
     * @param userId    目标用户ID
     * @param message   消息内容
     * @param principal 发送者信息
     */
    @MessageMapping("/private/{userId}")
    public void sendPrivateMessage(@DestinationVariable Long userId, @Payload Map<String, Object> message, Principal principal) {
        try {
            log.info("收到发送给用户{}的私聊消息：{}", userId, message);
            
            Map<String, Object> privateMessage = new HashMap<>();
            privateMessage.put("type", "PRIVATE_MESSAGE");
            privateMessage.put("content", message.get("content"));
            privateMessage.put("sender", principal != null ? principal.getName() : "匿名");
            privateMessage.put("timestamp", System.currentTimeMillis());
            
            webSocketMessageService.sendMessageToUser(userId, privateMessage);
        } catch (Exception e) {
            log.error("发送私聊消息失败", e);
        }
    }

    /**
     * 处理用户订阅通知
     *
     * @param principal 用户信息
     * @return 欢迎消息
     */
    @SubscribeMapping("/queue/notifications")
    public Map<String, Object> subscribeNotifications(Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "匿名用户";
            log.info("用户{}订阅通知", username);
            
            Map<String, Object> welcomeMessage = new HashMap<>();
            welcomeMessage.put("type", "WELCOME");
            welcomeMessage.put("message", "欢迎订阅通知服务");
            welcomeMessage.put("timestamp", System.currentTimeMillis());
            
            return welcomeMessage;
        } catch (Exception e) {
            log.error("处理通知订阅失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "ERROR");
            errorResponse.put("message", "订阅失败");
            return errorResponse;
        }
    }

    /**
     * 处理用户订阅消息
     *
     * @param principal 用户信息
     * @return 欢迎消息
     */
    @SubscribeMapping("/queue/messages")
    public Map<String, Object> subscribeMessages(Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "匿名用户";
            log.info("用户{}订阅消息", username);
            
            Map<String, Object> welcomeMessage = new HashMap<>();
            welcomeMessage.put("type", "WELCOME");
            welcomeMessage.put("message", "欢迎订阅消息服务");
            welcomeMessage.put("timestamp", System.currentTimeMillis());
            
            return welcomeMessage;
        } catch (Exception e) {
            log.error("处理消息订阅失败", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "ERROR");
            errorResponse.put("message", "订阅失败");
            return errorResponse;
        }
    }

    /**
     * 处理心跳检测
     *
     * @param principal 用户信息
     * @return 心跳响应
     */
    @MessageMapping("/heartbeat")
    public void heartbeat(Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "匿名用户";
            log.debug("收到用户{}的心跳", username);
            
            // 可以在这里更新用户的在线状态
            // userOnlineService.updateLastActiveTime(username);
        } catch (Exception e) {
            log.error("处理心跳失败", e);
        }
    }
}