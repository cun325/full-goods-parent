package org.example.api.service;

import org.example.common.entity.Message;
import org.example.common.response.Result;

import java.util.List;
import java.util.Map;

/**
 * 通知服务接口
 * 处理各种类型的通知：物流通知、客服消息、系统通知等
 */
public interface NotificationService {

    /**
     * 发送物流通知
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param status 物流状态
     * @param description 状态描述
     * @return 操作结果
     */
    Result<Boolean> sendLogisticsNotification(Long userId, Long orderId, String status, String description);

    /**
     * 发送订单状态通知
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @param statusDescription 状态描述
     * @return 操作结果
     */
    Result<Boolean> sendOrderStatusNotification(Long userId, Long orderId, String orderStatus, String statusDescription);

    /**
     * 发送系统通知
     * @param userId 用户ID（为null时发送给所有用户）
     * @param title 通知标题
     * @param content 通知内容
     * @param notificationType 通知类型
     * @return 操作结果
     */
    Result<Boolean> sendSystemNotification(Long userId, String title, String content, String notificationType);

    /**
     * 发送客服消息通知
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @param messageContent 消息内容
     * @param senderType 发送者类型
     * @return 操作结果
     */
    Result<Boolean> sendCustomerServiceNotification(Long userId, Long sessionId, String messageContent, String senderType);

    /**
     * 发送优惠活动通知
     * @param userId 用户ID（为null时发送给所有用户）
     * @param activityTitle 活动标题
     * @param activityContent 活动内容
     * @param activityType 活动类型
     * @return 操作结果
     */
    Result<Boolean> sendPromotionNotification(Long userId, String activityTitle, String activityContent, String activityType);

    /**
     * 发送支付相关通知
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param paymentStatus 支付状态
     * @param amount 金额
     * @return 操作结果
     */
    Result<Boolean> sendPaymentNotification(Long userId, Long orderId, String paymentStatus, Double amount);

    /**
     * 发送库存预警通知（给管理员）
     * @param productId 商品ID
     * @param productName 商品名称
     * @param currentStock 当前库存
     * @param warningThreshold 预警阈值
     * @return 操作结果
     */
    Result<Boolean> sendStockWarningNotification(Long productId, String productName, Integer currentStock, Integer warningThreshold);

    /**
     * 批量发送通知
     * @param userIds 用户ID列表
     * @param title 通知标题
     * @param content 通知内容
     * @param messageType 消息类型
     * @return 操作结果
     */
    Result<Boolean> sendBatchNotification(List<Long> userIds, String title, String content, String messageType);

    /**
     * 获取用户的通知列表
     * @param userId 用户ID
     * @param messageType 消息类型（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 通知列表
     */
    Result<List<Message>> getUserNotifications(Long userId, String messageType, Integer page, Integer size);

    /**
     * 获取用户未读通知数量
     * @param userId 用户ID
     * @param messageType 消息类型（可选）
     * @return 未读数量
     */
    Result<Integer> getUnreadNotificationCount(Long userId, String messageType);

    /**
     * 标记通知为已读
     * @param userId 用户ID
     * @param messageId 消息ID
     * @return 操作结果
     */
    Result<Boolean> markNotificationAsRead(Long userId, Long messageId);

    /**
     * 批量标记通知为已读
     * @param userId 用户ID
     * @param messageIds 消息ID列表
     * @return 操作结果
     */
    Result<Boolean> markNotificationsAsRead(Long userId, List<Long> messageIds);

    /**
     * 删除通知
     * @param userId 用户ID
     * @param messageId 消息ID
     * @return 操作结果
     */
    Result<Boolean> deleteNotification(Long userId, Long messageId);

    /**
     * 获取通知统计信息
     * @param userId 用户ID
     * @return 统计信息
     */
    Result<Map<String, Object>> getNotificationStatistics(Long userId);

    /**
     * 设置用户通知偏好
     * @param userId 用户ID
     * @param preferences 通知偏好设置
     * @return 操作结果
     */
    Result<Boolean> setNotificationPreferences(Long userId, Map<String, Boolean> preferences);

    /**
     * 获取用户通知偏好
     * @param userId 用户ID
     * @return 通知偏好设置
     */
    Result<Map<String, Boolean>> getNotificationPreferences(Long userId);
}