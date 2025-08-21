package org.example.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket消息推送服务
 */
@Slf4j
@Service
public class WebSocketMessageService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 向指定用户发送消息
     *
     * @param userId  用户ID
     * @param message 消息内容
     */
    public void sendMessageToUser(Long userId, Object message) {
        try {
            String destination = "/user/" + userId + "/queue/messages";
            messagingTemplate.convertAndSend(destination, message);
            log.info("向用户{}发送WebSocket消息成功", userId);
        } catch (Exception e) {
            log.error("向用户{}发送WebSocket消息失败", userId, e);
        }
    }

    /**
     * 向指定用户发送通知
     *
     * @param userId       用户ID
     * @param notification 通知内容
     */
    public void sendNotificationToUser(Long userId, Object notification) {
        try {
            String destination = "/user/" + userId + "/queue/notifications";
            messagingTemplate.convertAndSend(destination, notification);
            log.info("向用户{}发送WebSocket通知成功", userId);
        } catch (Exception e) {
            log.error("向用户{}发送WebSocket通知失败", userId, e);
        }
    }

    /**
     * 向指定用户发送客服消息
     *
     * @param userId        用户ID
     * @param serviceMessage 客服消息
     */
    public void sendServiceMessageToUser(Long userId, Object serviceMessage) {
        try {
            // 发送到用户特定队列
            String destination = "/user/" + userId + "/queue/service";
            messagingTemplate.convertAndSend(destination, serviceMessage);
            log.info("向用户{}发送客服WebSocket消息成功", userId);
            
            // 同时发送到通用队列，以防客户端订阅了通用队列
            String generalDestination = "/user/queue/service";
            messagingTemplate.convertAndSend(generalDestination, serviceMessage);
            log.info("向通用队列发送客服WebSocket消息成功");
        } catch (Exception e) {
            log.error("向用户{}发送客服WebSocket消息失败", userId, e);
        }
    }

    /**
     * 向客服系统发送消息（供管理员使用）
     *
     * @param serviceMessage 客服消息
     */
    public void sendServiceMessageToCustomerService(Object serviceMessage) {
        try {
            // 发送给所有在线的客服人员
            String destination = "/topic/service";
            messagingTemplate.convertAndSend(destination, serviceMessage);
            log.info("向客服系统发送WebSocket消息成功");
        } catch (Exception e) {
            log.error("向客服系统发送WebSocket消息失败", e);
        }
    }

    /**
     * 广播系统消息给所有用户
     *
     * @param message 系统消息
     */
    public void broadcastSystemMessage(Object message) {
        try {
            messagingTemplate.convertAndSend("/topic/system", message);
            log.info("广播系统消息成功");
        } catch (Exception e) {
            log.error("广播系统消息失败", e);
        }
    }

    /**
     * 广播优惠活动消息
     *
     * @param promotion 优惠活动消息
     */
    public void broadcastPromotionMessage(Object promotion) {
        try {
            messagingTemplate.convertAndSend("/topic/promotions", promotion);
            log.info("广播优惠活动消息成功");
        } catch (Exception e) {
            log.error("广播优惠活动消息失败", e);
        }
    }
}