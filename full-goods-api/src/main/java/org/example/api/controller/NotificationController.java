package org.example.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.example.api.service.NotificationService;
import org.example.common.entity.Message;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 通知控制器
 */
@Slf4j
@RestController
@RequestMapping("/notifications")
@Api(tags = "通知管理")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 发送物流通知
     */
    @PostMapping("/logistics")
    @ApiOperation("发送物流通知")
    public Result<Boolean> sendLogisticsNotification(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("订单ID") @RequestParam Long orderId,
            @ApiParam("物流状态") @RequestParam String status,
            @ApiParam("状态描述") @RequestParam String description) {
        return notificationService.sendLogisticsNotification(userId, orderId, status, description);
    }

    /**
     * 发送订单状态通知
     */
    @PostMapping("/order-status")
    @ApiOperation("发送订单状态通知")
    public Result<Boolean> sendOrderStatusNotification(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("订单ID") @RequestParam Long orderId,
            @ApiParam("订单状态") @RequestParam String orderStatus,
            @ApiParam("状态描述") @RequestParam String statusDescription) {
        return notificationService.sendOrderStatusNotification(userId, orderId, orderStatus, statusDescription);
    }

    /**
     * 发送系统通知
     */
    @PostMapping("/system")
    @ApiOperation("发送系统通知")
    public Result<Boolean> sendSystemNotification(
            @ApiParam("用户ID（为空则发送给所有用户）") @RequestParam(required = false) Long userId,
            @ApiParam("通知标题") @RequestParam String title,
            @ApiParam("通知内容") @RequestParam String content,
            @ApiParam("通知类型") @RequestParam String notificationType) {
        return notificationService.sendSystemNotification(userId, title, content, notificationType);
    }

    /**
     * 发送客服消息通知
     */
    @PostMapping("/customer-service")
    @ApiOperation("发送客服消息通知")
    public Result<Boolean> sendCustomerServiceNotification(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("会话ID") @RequestParam Long sessionId,
            @ApiParam("消息内容") @RequestParam String messageContent,
            @ApiParam("发送者类型") @RequestParam String senderType) {
        return notificationService.sendCustomerServiceNotification(userId, sessionId, messageContent, senderType);
    }

    /**
     * 发送优惠活动通知
     */
    @PostMapping("/promotion")
    @ApiOperation("发送优惠活动通知")
    public Result<Boolean> sendPromotionNotification(
            @ApiParam("用户ID（为空则发送给所有用户）") @RequestParam(required = false) Long userId,
            @ApiParam("活动标题") @RequestParam String activityTitle,
            @ApiParam("活动内容") @RequestParam String activityContent,
            @ApiParam("活动类型") @RequestParam String activityType) {
        return notificationService.sendPromotionNotification(userId, activityTitle, activityContent, activityType);
    }

    /**
     * 发送支付通知
     */
    @PostMapping("/payment")
    @ApiOperation("发送支付通知")
    public Result<Boolean> sendPaymentNotification(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("订单ID") @RequestParam Long orderId,
            @ApiParam("支付状态") @RequestParam String paymentStatus,
            @ApiParam("支付金额") @RequestParam Double amount) {
        return notificationService.sendPaymentNotification(userId, orderId, paymentStatus, amount);
    }

    /**
     * 发送库存预警通知
     */
    @PostMapping("/stock-warning")
    @ApiOperation("发送库存预警通知")
    public Result<Boolean> sendStockWarningNotification(
            @ApiParam("商品ID") @RequestParam Long productId,
            @ApiParam("商品名称") @RequestParam String productName,
            @ApiParam("当前库存") @RequestParam Integer currentStock,
            @ApiParam("预警阈值") @RequestParam Integer warningThreshold) {
        return notificationService.sendStockWarningNotification(productId, productName, currentStock, warningThreshold);
    }

    /**
     * 批量发送通知
     */
    @PostMapping("/batch")
    @ApiOperation("批量发送通知")
    public Result<Boolean> sendBatchNotification(
            @ApiParam("用户ID列表") @RequestBody List<Long> userIds,
            @ApiParam("通知标题") @RequestParam String title,
            @ApiParam("通知内容") @RequestParam String content,
            @ApiParam("消息类型") @RequestParam String messageType) {
        return notificationService.sendBatchNotification(userIds, title, content, messageType);
    }

    /**
     * 获取用户通知列表
     */
    @GetMapping("/user/{userId}")
    @ApiOperation("获取用户通知列表")
    public Result<List<Message>> getUserNotifications(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("消息类型") @RequestParam(required = false) String messageType,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size) {
        return notificationService.getUserNotifications(userId, messageType, page, size);
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/user/{userId}/unread-count")
    @ApiOperation("获取未读通知数量")
    public Result<Integer> getUnreadNotificationCount(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("消息类型") @RequestParam(required = false) String messageType) {
        return notificationService.getUnreadNotificationCount(userId, messageType);
    }

    /**
     * 标记通知为已读
     */
    @PutMapping("/user/{userId}/read/{messageId}")
    @ApiOperation("标记通知为已读")
    public Result<Boolean> markNotificationAsRead(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("消息ID") @PathVariable Long messageId) {
        return notificationService.markNotificationAsRead(userId, messageId);
    }

    /**
     * 批量标记通知为已读
     */
    @PutMapping("/user/{userId}/read-batch")
    @ApiOperation("批量标记通知为已读")
    public Result<Boolean> markNotificationsAsRead(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("消息ID列表") @RequestBody List<Long> messageIds) {
        return notificationService.markNotificationsAsRead(userId, messageIds);
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/user/{userId}/{messageId}")
    @ApiOperation("删除通知")
    public Result<Boolean> deleteNotification(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("消息ID") @PathVariable Long messageId) {
        return notificationService.deleteNotification(userId, messageId);
    }

    /**
     * 获取通知统计信息
     */
    @GetMapping("/user/{userId}/statistics")
    @ApiOperation("获取通知统计信息")
    public Result<Map<String, Object>> getNotificationStatistics(
            @ApiParam("用户ID") @PathVariable Long userId) {
        return notificationService.getNotificationStatistics(userId);
    }

    /**
     * 设置通知偏好
     */
    @PutMapping("/user/{userId}/preferences")
    @ApiOperation("设置通知偏好")
    public Result<Boolean> setNotificationPreferences(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("通知偏好设置") @RequestBody Map<String, Boolean> preferences) {
        return notificationService.setNotificationPreferences(userId, preferences);
    }

    /**
     * 获取通知偏好
     */
    @GetMapping("/user/{userId}/preferences")
    @ApiOperation("获取通知偏好")
    public Result<Map<String, Boolean>> getNotificationPreferences(
            @ApiParam("用户ID") @PathVariable Long userId) {
        return notificationService.getNotificationPreferences(userId);
    }
}