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
            String destination = "/user/" + userId + "/queue/service";
            messagingTemplate.convertAndSend(destination, serviceMessage);
            log.info("向用户{}发送客服WebSocket消息成功", userId);
        } catch (Exception e) {
            log.error("向用户{}发送客服WebSocket消息失败", userId, e);
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
            messagingTemplate.convertAndSend("/topic/promotion", promotion);
            log.info("广播优惠活动消息成功");
        } catch (Exception e) {
            log.error("广播优惠活动消息失败", e);
        }
    }

    /**
     * 发送订单状态更新消息
     *
     * @param userId      用户ID
     * @param orderUpdate 订单更新信息
     */
    public void sendOrderUpdateToUser(Long userId, Object orderUpdate) {
        try {
            String destination = "/user/" + userId + "/queue/orders";
            messagingTemplate.convertAndSend(destination, orderUpdate);
            log.info("向用户{}发送订单更新消息成功", userId);
        } catch (Exception e) {
            log.error("向用户{}发送订单更新消息失败", userId, e);
        }
    }

    /**
     * 发送物流更新消息
     *
     * @param userId          用户ID
     * @param logisticsUpdate 物流更新信息
     */
    public void sendLogisticsUpdateToUser(Long userId, Object logisticsUpdate) {
        try {
            String destination = "/user/" + userId + "/queue/logistics";
            messagingTemplate.convertAndSend(destination, logisticsUpdate);
            log.info("向用户{}发送物流更新消息成功", userId);
        } catch (Exception e) {
            log.error("向用户{}发送物流更新消息失败", userId, e);
        }
    }

    /**
     * 发送支付结果消息
     *
     * @param userId        用户ID
     * @param paymentResult 支付结果信息
     */
    public void sendPaymentResultToUser(Long userId, Object paymentResult) {
        try {
            String destination = "/user/" + userId + "/queue/payment";
            messagingTemplate.convertAndSend(destination, paymentResult);
            log.info("向用户{}发送支付结果消息成功", userId);
        } catch (Exception e) {
            log.error("向用户{}发送支付结果消息失败", userId, e);
        }
    }

    /**
     * 发送自定义消息到指定目标
     *
     * @param destination 目标地址
     * @param message     消息内容
     * @param headers     消息头（可选）
     */
    public void sendCustomMessage(String destination, Object message, Map<String, Object> headers) {
        try {
            if (headers != null && !headers.isEmpty()) {
                messagingTemplate.convertAndSend(destination, message, headers);
            } else {
                messagingTemplate.convertAndSend(destination, message);
            }
            log.info("发送自定义消息到{}成功", destination);
        } catch (Exception e) {
            log.error("发送自定义消息到{}失败", destination, e);
        }
    }

    /**
     * 发送在线状态更新
     *
     * @param userId       用户ID
     * @param onlineStatus 在线状态
     */
    public void sendOnlineStatusUpdate(Long userId, Object onlineStatus) {
        try {
            String destination = "/user/" + userId + "/queue/status";
            messagingTemplate.convertAndSend(destination, onlineStatus);
            log.info("向用户{}发送在线状态更新成功", userId);
        } catch (Exception e) {
            log.error("向用户{}发送在线状态更新失败", userId, e);
        }
    }

    /**
     * 发送未读消息数量更新
     *
     * @param userId      用户ID
     * @param unreadCount 未读消息数量
     */
    public void sendUnreadCountUpdate(Long userId, Integer unreadCount) {
        try {
            String destination = "/user/" + userId + "/queue/unread";
            Map<String, Object> payload = new HashMap<>();
            payload.put("unreadCount", unreadCount);
            messagingTemplate.convertAndSend(destination, payload);
            log.info("向用户{}发送未读消息数量更新成功，数量：{}", userId, unreadCount);
        } catch (Exception e) {
            log.error("向用户{}发送未读消息数量更新失败", userId, e);
        }
    }
}