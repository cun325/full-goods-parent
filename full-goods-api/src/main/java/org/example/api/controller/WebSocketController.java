package org.example.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.api.service.WebSocketMessageService;
import org.example.api.service.MessageService;
import org.example.common.entity.Message;
import org.example.common.response.Result;
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
    
    @Autowired
    private MessageService messageService;

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
            
            // 保存消息到数据库
            Message serviceMessage = new Message();
            serviceMessage.setUserId(userId);
            serviceMessage.setMessageType(2); // 客服消息
            // 根据发送者类型设置不同的标题
            if (principal != null) {
                // 用户发送的消息
                serviceMessage.setTitle("客户消息");
            } else {
                // 管理员发送的消息
                serviceMessage.setTitle("客服消息");
            }
            serviceMessage.setContent((String) message.get("content"));
            serviceMessage.setStatus(0); // 未读
            serviceMessage.setSenderId(principal != null ? Long.valueOf(principal.getName()) : null);
            serviceMessage.setSenderType(1); // 用户发送
            
            Result<Boolean> result = messageService.sendMessage(serviceMessage);
            if (result.getCode() != 200) {
                log.error("保存客服消息失败: {}", result.getMessage());
            }
            
            // 通过WebSocket发送给客服管理端
            Map<String, Object> wsMessage = new HashMap<>();
            wsMessage.put("type", "customer_service");
            wsMessage.put("content", message.get("content"));
            wsMessage.put("userId", userId);
            wsMessage.put("timestamp", System.currentTimeMillis());
            wsMessage.put("messageId", serviceMessage.getId());
            wsMessage.put("id", serviceMessage.getId()); // 确保ID字段正确
            wsMessage.put("senderType", 1); // 用户发送
            
            // 发送给所有订阅了客服消息的管理员
            webSocketMessageService.sendServiceMessageToCustomerService(wsMessage); // 发送给客服系统
            
            // 同时发送给用户自己，确保用户端能实时看到自己发送的消息
            webSocketMessageService.sendServiceMessageToUser(userId, wsMessage);
        } catch (Exception e) {
            log.error("发送客服消息失败", e);
        }
    }

    /**
     * 处理客服回复消息
     *
     * @param userId    用户ID
     * @param message   消息内容
     * @param principal 用户信息（客服）
     */
    @MessageMapping("/service/reply/{userId}")
    public void sendServiceReply(@DestinationVariable Long userId, @Payload Map<String, Object> message, Principal principal) {
        try {
            log.info("客服回复用户{}的消息：{}", userId, message);
            
            // 保存回复消息到数据库
            Message replyMessage = new Message();
            replyMessage.setUserId(userId);
            replyMessage.setMessageType(2); // 客服消息
            // 根据发送者类型设置不同的标题
            if (principal != null) {
                // 管理员发送的消息
                replyMessage.setTitle("客服消息");
            } else {
                // 用户发送的消息
                replyMessage.setTitle("客户消息");
            }
            replyMessage.setContent((String) message.get("content"));
            replyMessage.setStatus(0); // 未读
            replyMessage.setSenderId(principal != null ? Long.valueOf(principal.getName()) : null);
            replyMessage.setSenderType(3); // 客服回复
            
            Result<Boolean> result = messageService.sendMessage(replyMessage);
            if (result.getCode() != 200) {
                log.error("保存客服回复消息失败: {}", result.getMessage());
            }
            
            // 通过WebSocket发送给用户
            Map<String, Object> wsMessage = new HashMap<>();
            wsMessage.put("type", "customer_service_reply");
            wsMessage.put("content", message.get("content"));
            wsMessage.put("userId", userId);
            wsMessage.put("timestamp", System.currentTimeMillis());
            wsMessage.put("messageId", replyMessage.getId());
            // 添加额外字段确保前端能正确处理
            wsMessage.put("senderType", 3); // 客服发送
            wsMessage.put("id", replyMessage.getId()); // 确保ID字段正确
            
            // 发送给指定用户
            webSocketMessageService.sendServiceMessageToUser(userId, wsMessage);
            
            // 同时发送给客服系统，确保客服管理端也能实时看到
            webSocketMessageService.sendServiceMessageToCustomerService(wsMessage);
        } catch (Exception e) {
            log.error("发送客服回复消息失败", e);
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