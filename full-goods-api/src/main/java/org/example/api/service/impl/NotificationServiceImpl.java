package org.example.api.service.impl;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.example.api.mapper.MessageMapper;
import org.example.api.mapper.MessageTemplateMapper;
import org.example.api.service.NotificationService;
import org.example.common.entity.Message;
import org.example.common.entity.MessageTemplate;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 通知服务实现类
 */
@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public Result<Boolean> sendLogisticsNotification(Long userId, Long orderId, String status, String description) {
        try {
            String title = "物流更新通知";
            String content = generateLogisticsContent(orderId, status, description);
            
            Message message = createMessage(userId, title, content, "LOGISTICS", orderId.toString());
            int result = messageMapper.insert(message);
            
            return result > 0 ? Result.success(true) : Result.failed("发送物流通知失败");
        } catch (Exception e) {
            log.error("发送物流通知失败", e);
            return Result.failed("发送物流通知失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> sendOrderStatusNotification(Long userId, Long orderId, String orderStatus, String statusDescription) {
        try {
            String title = "订单状态更新";
            String content = generateOrderStatusContent(orderId, orderStatus, statusDescription);
            
            Message message = createMessage(userId, title, content, "ORDER", orderId.toString());
            int result = messageMapper.insert(message);
            
            return result > 0 ? Result.success(true) : Result.failed("发送订单通知失败");
        } catch (Exception e) {
            log.error("发送订单状态通知失败", e);
            return Result.failed("发送订单通知失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> sendSystemNotification(Long userId, String title, String content, String notificationType) {
        try {
            if (userId == null) {
                // 发送给所有用户的系统通知
                return sendBroadcastNotification(title, content, "SYSTEM");
            } else {
                // 发送给特定用户
                Message message = createMessage(userId, title, content, "SYSTEM", notificationType);
                int result = messageMapper.insert(message);
                
                return result > 0 ? Result.success(true) : Result.failed("发送系统通知失败");
            }
        } catch (Exception e) {
            log.error("发送系统通知失败", e);
            return Result.failed("发送系统通知失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> sendCustomerServiceNotification(Long userId, Long sessionId, String messageContent, String senderType) {
        try {
            String title = "客服消息";
            String content = "您有新的客服消息：" + messageContent;
            
            Message message = createMessage(userId, title, content, "CUSTOMER_SERVICE", sessionId.toString());
            int result = messageMapper.insert(message);
            
            return result > 0 ? Result.success(true) : Result.failed("发送客服通知失败");
        } catch (Exception e) {
            log.error("发送客服消息通知失败", e);
            return Result.failed("发送客服通知失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> sendPromotionNotification(Long userId, String activityTitle, String activityContent, String activityType) {
        try {
            String title = "优惠活动通知";
            String content = generatePromotionContent(activityTitle, activityContent, activityType);
            
            if (userId == null) {
                // 发送给所有用户
                return sendBroadcastNotification(title, content, "PROMOTION");
            } else {
                Message message = createMessage(userId, title, content, "PROMOTION", activityType);
                int result = messageMapper.insert(message);
                
                return result > 0 ? Result.success(true) : Result.failed("发送优惠通知失败");
            }
        } catch (Exception e) {
            log.error("发送优惠活动通知失败", e);
            return Result.failed("发送优惠通知失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> sendPaymentNotification(Long userId, Long orderId, String paymentStatus, Double amount) {
        try {
            String title = "支付通知";
            String content = generatePaymentContent(orderId, paymentStatus, amount);
            
            Message message = createMessage(userId, title, content, "PAYMENT", orderId.toString());
            int result = messageMapper.insert(message);
            
            return result > 0 ? Result.success(true) : Result.failed("发送支付通知失败");
        } catch (Exception e) {
            log.error("发送支付通知失败", e);
            return Result.failed("发送支付通知失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> sendStockWarningNotification(Long productId, String productName, Integer currentStock, Integer warningThreshold) {
        try {
            String title = "库存预警";
            String content = String.format("商品【%s】库存不足，当前库存：%d，预警阈值：%d，请及时补货。", 
                    productName, currentStock, warningThreshold);
            
            // 发送给管理员（这里假设管理员用户ID为1）
            Message message = createMessage(1L, title, content, "STOCK_WARNING", productId.toString());
            int result = messageMapper.insert(message);
            
            return result > 0 ? Result.success(true) : Result.failed("发送库存预警失败");
        } catch (Exception e) {
            log.error("发送库存预警通知失败", e);
            return Result.failed("发送库存预警失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> sendBatchNotification(List<Long> userIds, String title, String content, String messageType) {
        try {
            List<Message> messages = new ArrayList<>();
            Date now = new Date();
            
            for (Long userId : userIds) {
                Message message = createMessage(userId, title, content, messageType, null);
                messages.add(message);
            }
            
            int result = messageMapper.batchInsert(messages);
            return result > 0 ? Result.success(true) : Result.failed("批量发送通知失败");
        } catch (Exception e) {
            log.error("批量发送通知失败", e);
            return Result.failed("批量发送通知失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<Message>> getUserNotifications(Long userId, String messageType, Integer page, Integer size) {
        try {
            PageHelper.startPage(page, size);
            Integer msgType = convertMessageType(messageType);
            List<Message> messages = messageMapper.selectByUserIdAndType(userId, msgType);
            return Result.success(messages);
        } catch (Exception e) {
            log.error("获取用户通知列表失败", e);
            return Result.failed("获取通知列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Integer> getUnreadNotificationCount(Long userId, String messageType) {
        try {
            Integer msgType = convertMessageType(messageType);
            int count = messageMapper.countUnreadByUserIdAndType(userId, msgType);
            return Result.success(count);
        } catch (Exception e) {
            log.error("获取未读通知数量失败", e);
            return Result.failed("获取未读数量失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> markNotificationAsRead(Long userId, Long messageId) {
        try {
            int result = messageMapper.markAsRead(messageId);
            return result > 0 ? Result.success(true) : Result.failed("标记已读失败");
        } catch (Exception e) {
            log.error("标记通知已读失败", e);
            return Result.failed("标记已读失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> markNotificationsAsRead(Long userId, List<Long> messageIds) {
        try {
            int result = messageMapper.batchMarkAsRead(messageIds);
            return result > 0 ? Result.success(true) : Result.failed("批量标记已读失败");
        } catch (Exception e) {
            log.error("批量标记通知已读失败", e);
            return Result.failed("批量标记已读失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> deleteNotification(Long userId, Long messageId) {
        try {
            int result = messageMapper.deleteById(messageId);
            return result > 0 ? Result.success(true) : Result.failed("删除通知失败");
        } catch (Exception e) {
            log.error("删除通知失败", e);
            return Result.failed("删除通知失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getNotificationStatistics(Long userId) {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 总通知数
            List<Message> allMessages = messageMapper.selectByUserId(userId);
            statistics.put("totalCount", allMessages.size());
            
            // 未读通知数
            int unreadCount = messageMapper.countUnreadByUserId(userId);
            statistics.put("unreadCount", unreadCount);
            
            // 各类型通知数量
            Map<String, Integer> typeCount = new HashMap<>();
            typeCount.put("SYSTEM", countMessagesByType(allMessages, 3));
            typeCount.put("ORDER", countMessagesByType(allMessages, 4));
            typeCount.put("LOGISTICS", countMessagesByType(allMessages, 1));
            typeCount.put("PAYMENT", countMessagesByType(allMessages, 5));
            typeCount.put("PROMOTION", countMessagesByType(allMessages, 6));
            typeCount.put("CUSTOMER_SERVICE", countMessagesByType(allMessages, 2));
            statistics.put("typeCount", typeCount);
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取通知统计失败", e);
            return Result.failed("获取统计信息失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> setNotificationPreferences(Long userId, Map<String, Boolean> preferences) {
        try {
            // 这里可以将用户偏好存储到数据库或缓存中
            // 暂时返回成功，实际项目中需要实现具体的存储逻辑
            log.info("设置用户{}的通知偏好：{}", userId, preferences);
            return Result.success(true);
        } catch (Exception e) {
            log.error("设置通知偏好失败", e);
            return Result.failed("设置通知偏好失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Boolean>> getNotificationPreferences(Long userId) {
        try {
            // 返回默认的通知偏好设置
            Map<String, Boolean> preferences = new HashMap<>();
            preferences.put("SYSTEM", true);
            preferences.put("ORDER", true);
            preferences.put("LOGISTICS", true);
            preferences.put("PAYMENT", true);
            preferences.put("PROMOTION", false);
            preferences.put("CUSTOMER_SERVICE", true);
            
            return Result.success(preferences);
        } catch (Exception e) {
            log.error("获取通知偏好失败", e);
            return Result.failed("获取通知偏好失败：" + e.getMessage());
        }
    }

    /**
     * 创建消息对象
     */
    private Message createMessage(Long userId, String title, String content, String messageType, String relatedId) {
        Message message = new Message();
        message.setUserId(userId);
        message.setTitle(title);
        message.setContent(content);
        message.setMessageType(convertMessageType(messageType));
        message.setOrderNo(relatedId);
        message.setStatus(0); // 0-未读
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());
        return message;
    }

    /**
     * 生成物流通知内容
     */
    private String generateLogisticsContent(Long orderId, String status, String description) {
        return String.format("您的订单 %d 物流状态已更新：%s。%s", orderId, status, description);
    }

    /**
     * 生成订单状态通知内容
     */
    private String generateOrderStatusContent(Long orderId, String orderStatus, String statusDescription) {
        return String.format("您的订单 %d 状态已更新为：%s。%s", orderId, orderStatus, statusDescription);
    }

    /**
     * 生成优惠活动通知内容
     */
    private String generatePromotionContent(String activityTitle, String activityContent, String activityType) {
        return String.format("【%s】%s\n%s", activityType, activityTitle, activityContent);
    }

    /**
     * 生成支付通知内容
     */
    private String generatePaymentContent(Long orderId, String paymentStatus, Double amount) {
        if ("SUCCESS".equals(paymentStatus)) {
            return String.format("您的订单 %d 支付成功，支付金额：￥%.2f", orderId, amount);
        } else if ("FAILED".equals(paymentStatus)) {
            return String.format("您的订单 %d 支付失败，请重新支付。金额：￥%.2f", orderId, amount);
        } else {
            return String.format("您的订单 %d 支付状态：%s，金额：￥%.2f", orderId, paymentStatus, amount);
        }
    }

    /**
     * 发送广播通知（给所有用户）
     */
    private Result<Boolean> sendBroadcastNotification(String title, String content, String messageType) {
        try {
            // 这里应该获取所有用户ID，然后批量发送
            // 为了简化，这里只是记录日志
            log.info("发送广播通知：标题={}, 内容={}, 类型={}", title, content, messageType);
            
            // 实际项目中，这里应该：
            // 1. 查询所有活跃用户ID
            // 2. 调用 sendBatchNotification 方法
            
            return Result.success(true);
        } catch (Exception e) {
            log.error("发送广播通知失败", e);
            return Result.failed("发送广播通知失败：" + e.getMessage());
        }
    }

    /**
     * 转换消息类型字符串为整数
     */
    private Integer convertMessageType(String messageType) {
        if (messageType == null) {
            return null;
        }
        switch (messageType) {
            case "LOGISTICS":
                return 1;
            case "CUSTOMER_SERVICE":
                return 2;
            case "SYSTEM":
                return 3;
            case "ORDER":
                return 4;
            case "PAYMENT":
                return 5;
            case "PROMOTION":
                return 6;
            case "STOCK_WARNING":
                return 7;
            default:
                return 3; // 默认为系统通知
        }
    }

    /**
     * 统计指定类型的消息数量
     */
    private int countMessagesByType(List<Message> messages, Integer messageType) {
        return (int) messages.stream()
                .filter(msg -> messageType.equals(msg.getMessageType()))
                .count();
    }
}