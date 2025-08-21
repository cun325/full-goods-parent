package org.example.api.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.Message;
import org.example.api.service.MessageService;
import org.example.common.response.Result;
import org.example.api.context.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息控制器
 * 提供消息相关的REST API接口
 */
@Slf4j
@RestController
@RequestMapping("/messages")
@Tag(name = "消息管理", description = "消息发送、接收、标记已读等功能")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 发送单条消息
     */
    @PostMapping("/send")
    @Operation(summary = "发送单条消息", description = "发送一条消息给指定用户")
    public Result<Boolean> sendMessage(@RequestBody Message message) {
        return messageService.sendMessage(message);
    }

    /**
     * 批量发送消息
     */
    @PostMapping("/send/batch")
    @Operation(summary = "批量发送消息", description = "批量发送消息给多个用户")
    public Result<Boolean> sendBatchMessages(@RequestBody List<Message> messages) {
        return messageService.sendBatchMessages(messages);
    }

    /**
     * 通过模板发送消息
     */
    @PostMapping("/send/template")
    @Operation(summary = "通过模板发送消息", description = "使用消息模板发送个性化消息")
    public Result<Boolean> sendMessageByTemplate(
            @Parameter(description = "模板代码") @RequestParam String templateCode,
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "模板参数") @RequestBody Map<String, Object> params) {
        return messageService.sendMessageByTemplate(templateCode, userId, params);
    }

    /**
     * 发送物流通知
     */
    @PostMapping("/send/logistics")
    @Operation(summary = "发送物流通知", description = "发送订单物流状态通知")
    public Result<Boolean> sendLogisticsNotification(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "订单号") @RequestParam String orderNo,
            @Parameter(description = "物流状态") @RequestParam String status,
            @Parameter(description = "通知内容") @RequestParam String content) {
        return messageService.sendLogisticsNotification(userId, orderNo, status, content);
    }

    /**
     * 发送系统通知
     */
    @PostMapping("/send/system")
    @Operation(summary = "发送系统通知", description = "发送系统级别的通知消息")
    public Result<Boolean> sendSystemNotification(@RequestBody Map<String, Object> request) {
        try {
            // 参数验证
            if (request.get("title") == null || request.get("title").toString().trim().isEmpty()) {
                return Result.failed("消息标题不能为空");
            }
            if (request.get("content") == null || request.get("content").toString().trim().isEmpty()) {
                return Result.failed("消息内容不能为空");
            }
            
            String title = request.get("title").toString();
            String content = request.get("content").toString();
            String iconUrl = request.get("iconUrl") != null ? request.get("iconUrl").toString() : null;
            String linkUrl = request.get("linkUrl") != null ? request.get("linkUrl").toString() : null;
            
            // 处理目标用户
            String targetType = request.get("targetType") != null ? request.get("targetType").toString() : "all";
            
            if ("all".equals(targetType)) {
                // 发送给所有用户 - 这里简化处理，发送给用户ID为1的用户作为示例
                return messageService.sendSystemNotification(1L, title, content, iconUrl, linkUrl);
            } else if ("specific".equals(targetType)) {
                // 发送给指定用户
                String userIds = request.get("userIds") != null ? request.get("userIds").toString() : "";
                if (userIds.trim().isEmpty()) {
                    return Result.failed("指定用户时，用户ID不能为空");
                }
                
                // 处理多个用户ID（逗号分隔）
                String[] userIdArray = userIds.split(",");
                boolean allSuccess = true;
                for (String userIdStr : userIdArray) {
                    try {
                        Long userId = Long.valueOf(userIdStr.trim());
                        Result<Boolean> result = messageService.sendSystemNotification(userId, title, content, iconUrl, linkUrl);
                         if (result.getCode() != 200) {
                             allSuccess = false;
                         }
                    } catch (NumberFormatException e) {
                        log.error("用户ID格式不正确: {}", userIdStr);
                        allSuccess = false;
                    }
                }
                return allSuccess ? Result.success(true) : Result.failed("部分消息发送失败");
            } else {
                // 兼容旧版本API，直接使用userId参数
                if (request.get("userId") == null) {
                    return Result.failed("用户ID不能为空");
                }
                Long userId = Long.valueOf(request.get("userId").toString());
                return messageService.sendSystemNotification(userId, title, content, iconUrl, linkUrl);
            }
            
        } catch (NumberFormatException e) {
            return Result.failed("用户ID格式不正确");
        } catch (Exception e) {
            log.error("发送系统通知失败", e);
            return Result.failed("发送系统通知失败: " + e.getMessage());
        }
    }

    /**
     * 发送优惠活动通知
     */
    @PostMapping("/send/promotion")
    @Operation(summary = "发送优惠活动通知", description = "发送优惠活动相关的通知")
    public Result<Boolean> sendPromotionNotification(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "活动标题") @RequestParam String activityTitle,
            @Parameter(description = "活动内容") @RequestParam String activityContent,
            @Parameter(description = "图标URL") @RequestParam(required = false) String iconUrl,
            @Parameter(description = "链接URL") @RequestParam(required = false) String linkUrl) {
        return messageService.sendPromotionNotification(userId, activityTitle, activityContent, iconUrl, linkUrl);
    }

    /**
     * 获取用户消息列表（分页）
     */
    @GetMapping("/user")
    @Operation(summary = "获取用户消息列表", description = "分页获取当前用户的消息列表")
    public Result<PageInfo<Message>> getUserMessages(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.getUserMessages(userId, page, size);
        } catch (Exception e) {
            log.error("获取用户消息列表失败: {}", e.getMessage(), e);
            return Result.failed("获取消息列表失败");
        }
    }

    /**
     * 按类型获取用户消息列表（分页）
     */
    @GetMapping("/user/type/{messageType}")
    @Operation(summary = "按类型获取用户消息", description = "分页获取当前用户特定类型的消息列表")
    public Result<PageInfo<Message>> getUserMessagesByType(
            @Parameter(description = "消息类型") @PathVariable Integer messageType,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.getUserMessagesByType(userId, messageType, page, size);
        } catch (Exception e) {
            log.error("按类型获取用户消息失败: {}", e.getMessage(), e);
            return Result.failed("获取消息列表失败");
        }
    }

    /**
     * 获取用户未读消息列表
     */
    @GetMapping("/user/unread")
    @Operation(summary = "获取未读消息列表", description = "获取当前用户的所有未读消息")
    public Result<List<Message>> getUnreadMessages() {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.getUnreadMessages(userId);
        } catch (Exception e) {
            log.error("获取未读消息列表失败: {}", e.getMessage(), e);
            return Result.failed("获取未读消息列表失败");
        }
    }

    /**
     * 按类型获取用户未读消息列表
     */
    @GetMapping("/user/unread/type/{messageType}")
    @Operation(summary = "按类型获取未读消息", description = "获取当前用户特定类型的未读消息")
    public Result<List<Message>> getUnreadMessagesByType(
            @Parameter(description = "消息类型") @PathVariable Integer messageType) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.getUnreadMessagesByType(userId, messageType);
        } catch (Exception e) {
            log.error("按类型获取未读消息失败: {}", e.getMessage(), e);
            return Result.failed("获取未读消息失败");
        }
    }

    /**
     * 获取用户未读消息数量
     */
    @GetMapping("/user/unread/count")
    @Operation(summary = "获取未读消息数量", description = "获取当前用户的未读消息总数")
    public Result<Integer> getUnreadMessageCount() {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.getUnreadMessageCount(userId);
        } catch (Exception e) {
            log.error("获取未读消息数量失败: {}", e.getMessage(), e);
            return Result.failed("获取未读消息数量失败");
        }
    }

    /**
     * 按类型获取用户未读消息数量
     */
    @GetMapping("/user/unread/count/type/{messageType}")
    @Operation(summary = "按类型获取未读消息数量", description = "获取当前用户特定类型的未读消息数量")
    public Result<Integer> getUnreadMessageCountByType(
            @Parameter(description = "消息类型") @PathVariable Integer messageType) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.getUnreadMessageCountByType(userId, messageType);
        } catch (Exception e) {
            log.error("按类型获取未读消息数量失败: {}", e.getMessage(), e);
            return Result.failed("获取未读消息数量失败");
        }
    }

    /**
     * 标记消息为已读
     */
    @PutMapping("/{messageId}/read")
    @Operation(summary = "标记消息为已读", description = "将指定消息标记为已读状态")
    public Result<Boolean> markMessageAsRead(
            @Parameter(description = "消息ID") @PathVariable Long messageId) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.markMessageAsRead(messageId, userId);
        } catch (Exception e) {
            log.error("标记消息为已读失败: {}", e.getMessage(), e);
            return Result.failed("标记消息为已读失败");
        }
    }

    /**
     * 批量标记消息为已读
     */
    @PutMapping("/read/batch")
    @Operation(summary = "批量标记消息为已读", description = "批量将多条消息标记为已读状态")
    public Result<Boolean> markMessagesAsRead(
            @Parameter(description = "消息ID列表") @RequestBody List<Long> messageIds) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.markMessagesAsRead(messageIds, userId);
        } catch (Exception e) {
            log.error("批量标记消息为已读失败: {}", e.getMessage(), e);
            return Result.failed("批量标记消息为已读失败");
        }
    }

    /**
     * 标记全部消息为已读
     */
    @PutMapping("/user/read/all")
    @Operation(summary = "标记全部消息为已读", description = "将当前用户的所有消息标记为已读状态")
    public Result<Boolean> markAllMessagesAsRead() {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.markAllMessagesAsRead(userId);
        } catch (Exception e) {
            log.error("标记全部消息为已读失败: {}", e.getMessage(), e);
            return Result.failed("标记全部消息为已读失败");
        }
    }

    /**
     * 按类型标记全部消息为已读
     */
    @PutMapping("/user/read/all/type/{messageType}")
    @Operation(summary = "按类型标记全部消息为已读", description = "将当前用户指定类型的所有消息标记为已读状态")
    public Result<Boolean> markAllMessagesByTypeAsRead(
            @Parameter(description = "消息类型") @PathVariable Integer messageType) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.markAllMessagesByTypeAsRead(userId, messageType);
        } catch (Exception e) {
            log.error("按类型标记全部消息为已读失败: {}", e.getMessage(), e);
            return Result.failed("按类型标记全部消息为已读失败");
        }
    }

    /**
     * 删除消息
     */
    @DeleteMapping("/{messageId}")
    @Operation(summary = "删除消息", description = "删除指定的消息")
    public Result<Boolean> deleteMessage(
            @Parameter(description = "消息ID") @PathVariable Long messageId) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.deleteMessage(messageId, userId);
        } catch (Exception e) {
            log.error("删除消息失败: {}", e.getMessage(), e);
            return Result.failed("删除消息失败");
        }
    }

    /**
     * 批量删除消息
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除消息", description = "批量删除多条消息")
    public Result<Boolean> deleteMessages(
            @Parameter(description = "消息ID列表") @RequestBody List<Long> messageIds) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.deleteMessages(messageIds, userId);
        } catch (Exception e) {
            log.error("批量删除消息失败: {}", e.getMessage(), e);
            return Result.failed("批量删除消息失败");
        }
    }

    /**
     * 获取消息详情
     */
    @GetMapping("/{messageId}")
    @Operation(summary = "获取消息详情", description = "获取指定消息的详细信息")
    public Result<Message> getMessageDetail(
            @Parameter(description = "消息ID") @PathVariable Long messageId) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.getMessageDetail(messageId, userId);
        } catch (Exception e) {
            log.error("获取消息详情失败: {}", e.getMessage(), e);
            return Result.failed("获取消息详情失败");
        }
    }

    /**
     * 根据订单号获取物流消息
     */
    @GetMapping("/logistics/order/{orderNo}")
    @Operation(summary = "获取物流消息", description = "根据订单号获取相关的物流消息")
    public Result<List<Message>> getLogisticsMessagesByOrderNo(
            @Parameter(description = "订单号") @PathVariable String orderNo) {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.getLogisticsMessagesByOrderNo(userId, orderNo);
        } catch (Exception e) {
            log.error("获取物流消息失败: {}", e.getMessage(), e);
            return Result.failed("获取物流消息失败");
        }
    }

    /**
     * 获取消息统计信息
     */
    @GetMapping("/user/statistics")
    @Operation(summary = "获取消息统计信息", description = "获取当前用户的消息统计数据，包括总数、未读数、各类型数量等")
    public Result<Map<String, Object>> getMessageStatistics() {
        try {
            Long userId = UserContext.getCurrentUserId();
            return messageService.getMessageStatistics(userId);
        } catch (Exception e) {
            log.error("获取消息统计信息失败: {}", e.getMessage(), e);
            return Result.failed("获取消息统计信息失败");
        }
    }

    /**
     * 管理员获取所有用户消息列表（分页）
     */
    @GetMapping("/user/list")
    @Operation(summary = "管理员获取所有用户消息列表", description = "管理员分页获取所有用户的消息列表，支持搜索、状态和类型过滤")
    public Result<PageInfo<Message>> getAllUserMessages(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "消息类型") @RequestParam(required = false) Integer messageType,
            @Parameter(description = "消息状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        try {
            return messageService.getAllUserMessages(page, size, messageType, status, userId);
        } catch (Exception e) {
            log.error("管理员获取消息列表失败: {}", e.getMessage(), e);
            return Result.failed("获取消息列表失败");
        }
    }

    /**
     * 管理员获取消息详情
     */
    @GetMapping("/admin/{messageId}")
    @Operation(summary = "管理员获取消息详情", description = "管理员获取指定消息的详细信息")
    public Result<Message> getAdminMessageDetail(
            @Parameter(description = "消息ID") @PathVariable Long messageId) {
        try {
            return messageService.getAdminMessageDetail(messageId);
        } catch (Exception e) {
            log.error("管理员获取消息详情失败: {}", e.getMessage(), e);
            return Result.failed("获取消息详情失败");
        }
    }

    /**
     * 管理员回复消息
     */
    @PostMapping("/admin/{messageId}/reply")
    @Operation(summary = "管理员回复消息", description = "管理员回复用户消息")
    public Result<Boolean> adminReplyMessage(
            @Parameter(description = "消息ID") @PathVariable Long messageId,
            @Parameter(description = "回复内容") @RequestParam String replyContent,
            @Parameter(description = "管理员ID") @RequestParam Long adminId) {
        try {
            return messageService.replyMessage(messageId, replyContent, adminId);
        } catch (Exception e) {
            log.error("管理员回复消息失败: {}", e.getMessage(), e);
            return Result.failed("回复消息失败");
        }
    }

    /**
     * 管理员标记消息已读
     */
    @PutMapping("/admin/{messageId}/read")
    @Operation(summary = "管理员标记消息已读", description = "管理员将指定消息标记为已读状态")
    public Result<Boolean> adminMarkMessageAsRead(
            @Parameter(description = "消息ID") @PathVariable Long messageId) {
        try {
            return messageService.markMessageRead(messageId);
        } catch (Exception e) {
            log.error("管理员标记消息已读失败: {}", e.getMessage(), e);
            return Result.failed("标记消息已读失败");
        }
    }

    /**
     * 管理员批量标记消息已读
     */
    @PutMapping("/admin/read/batch")
    @Operation(summary = "管理员批量标记消息已读", description = "管理员批量将多条消息标记为已读状态")
    public Result<Boolean> adminBatchMarkMessagesAsRead(
            @Parameter(description = "消息ID列表") @RequestBody List<Long> messageIds) {
        try {
            return messageService.batchMarkMessageRead(messageIds);
        } catch (Exception e) {
            log.error("管理员批量标记消息已读失败: {}", e.getMessage(), e);
            return Result.failed("批量标记消息已读失败");
        }
    }

    /**
     * 管理员批量删除消息
     */
    @DeleteMapping("/admin/batch")
    @Operation(summary = "管理员批量删除消息", description = "管理员批量删除多条消息")
    public Result<Boolean> adminBatchDeleteMessages(
            @Parameter(description = "消息ID列表") @RequestBody List<Long> messageIds) {
        try {
            return messageService.batchDeleteMessages(messageIds);
        } catch (Exception e) {
            log.error("管理员批量删除消息失败: {}", e.getMessage(), e);
            return Result.failed("批量删除消息失败");
        }
    }

    /**
     * 管理员获取消息统计信息
     */
    @GetMapping("/admin/statistics")
    @Operation(summary = "管理员获取消息统计信息", description = "管理员获取所有消息的统计数据")
    public Result<Map<String, Object>> getAdminMessageStatistics() {
        try {
            return messageService.getAdminMessageStatistics();
        } catch (Exception e) {
            log.error("管理员获取消息统计信息失败: {}", e.getMessage(), e);
            return Result.failed("获取消息统计信息失败");
        }
    }

    /**
     * 管理员获取用户聊天列表
     */
    @GetMapping("/admin/chat/users")
    @Operation(summary = "管理员获取用户聊天列表", description = "管理员分页获取用户聊天列表，按用户分组显示聊天记录")
    public Result<PageInfo<Map<String, Object>>> getUserChatList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        try {
            return messageService.getUserChatList(page, size, keyword);
        } catch (Exception e) {
            log.error("管理员获取用户聊天列表失败: {}", e.getMessage(), e);
            return Result.failed("获取用户聊天列表失败");
        }
    }

    /**
     * 管理员获取与特定用户的聊天记录
     */
    @GetMapping("/admin/chat/user/{userId}")
    @Operation(summary = "管理员获取用户聊天记录", description = "管理员分页获取与特定用户的聊天记录")
    public Result<PageInfo<Message>> getUserChatMessages(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        try {
            return messageService.getUserChatMessages(userId, page, size);
        } catch (Exception e) {
            log.error("管理员获取用户聊天记录失败，用户ID: {}", userId, e);
            return Result.failed("获取用户聊天记录失败");
        }
    }
}