package org.example.api.service;

import com.github.pagehelper.PageInfo;
import org.example.common.entity.Message;
import org.example.common.response.Result;

import java.util.List;
import java.util.Map;

/**
 * 消息服务接口
 */
public interface MessageService {

    /**
     * 发送消息
     * @param message 消息对象
     * @return 发送结果
     */
    Result<Boolean> sendMessage(Message message);

    /**
     * 批量发送消息
     * @param messages 消息列表
     * @return 发送结果
     */
    Result<Boolean> sendBatchMessages(List<Message> messages);

    /**
     * 根据模板发送消息
     * @param templateCode 模板编码
     * @param userId 用户ID
     * @param params 模板参数
     * @return 发送结果
     */
    Result<Boolean> sendMessageByTemplate(String templateCode, Long userId, Map<String, Object> params);

    /**
     * 发送物流通知
     * @param userId 用户ID
     * @param orderNo 订单号
     * @param status 物流状态
     * @param content 通知内容
     * @return 发送结果
     */
    Result<Boolean> sendLogisticsNotification(Long userId, String orderNo, String status, String content);

    /**
     * 发送系统通知
     * @param userId 用户ID（为null时发送给所有用户）
     * @param title 通知标题
     * @param content 通知内容
     * @param iconUrl 图标URL
     * @param linkUrl 跳转链接
     * @return 发送结果
     */
    Result<Boolean> sendSystemNotification(Long userId, String title, String content, String iconUrl, String linkUrl);

    /**
     * 发送优惠活动通知
     * @param userId 用户ID（为null时发送给所有用户）
     * @param activityTitle 活动标题
     * @param activityContent 活动内容
     * @param iconUrl 图标URL
     * @param linkUrl 跳转链接
     * @return 发送结果
     */
    Result<Boolean> sendPromotionNotification(Long userId, String activityTitle, String activityContent, String iconUrl, String linkUrl);

    /**
     * 获取用户消息列表（分页）
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 消息列表
     */
    Result<PageInfo<Message>> getUserMessages(Long userId, Integer page, Integer size);

    /**
     * 根据消息类型获取用户消息列表（分页）
     * @param userId 用户ID
     * @param messageType 消息类型
     * @param page 页码
     * @param size 每页大小
     * @return 消息列表
     */
    Result<PageInfo<Message>> getUserMessagesByType(Long userId, Integer messageType, Integer page, Integer size);

    /**
     * 获取用户未读消息列表
     * @param userId 用户ID
     * @return 未读消息列表
     */
    Result<List<Message>> getUnreadMessages(Long userId);

    /**
     * 根据消息类型获取用户未读消息列表
     * @param userId 用户ID
     * @param messageType 消息类型
     * @return 未读消息列表
     */
    Result<List<Message>> getUnreadMessagesByType(Long userId, Integer messageType);

    /**
     * 获取用户未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Result<Integer> getUnreadMessageCount(Long userId);

    /**
     * 根据消息类型获取用户未读消息数量
     * @param userId 用户ID
     * @param messageType 消息类型
     * @return 未读消息数量
     */
    Result<Integer> getUnreadMessageCountByType(Long userId, Integer messageType);

    /**
     * 标记消息为已读
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Boolean> markMessageAsRead(Long messageId, Long userId);

    /**
     * 批量标记消息为已读
     * @param messageIds 消息ID列表
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Boolean> markMessagesAsRead(List<Long> messageIds, Long userId);

    /**
     * 标记用户所有消息为已读
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Boolean> markAllMessagesAsRead(Long userId);

    /**
     * 根据消息类型标记用户所有消息为已读
     * @param userId 用户ID
     * @param messageType 消息类型
     * @return 操作结果
     */
    Result<Boolean> markAllMessagesByTypeAsRead(Long userId, Integer messageType);

    /**
     * 删除消息
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Boolean> deleteMessage(Long messageId, Long userId);

    /**
     * 批量删除消息
     * @param messageIds 消息ID列表
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Boolean> deleteMessages(List<Long> messageIds, Long userId);

    /**
     * 获取消息详情
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 消息详情
     */
    Result<Message> getMessageDetail(Long messageId, Long userId);

    /**
     * 根据订单号获取物流消息
     * @param userId 用户ID
     * @param orderNo 订单号
     * @return 物流消息列表
     */
    Result<List<Message>> getLogisticsMessagesByOrderNo(Long userId, String orderNo);

    /**
     * 获取消息统计信息
     * @param userId 用户ID
     * @return 消息统计信息（包含各类型消息数量）
     */
    Result<Map<String, Object>> getMessageStatistics(Long userId);

    // ==================== 管理员相关接口 ====================

    /**
     * 获取所有用户消息列表（管理员）
     * @param page 页码
     * @param size 每页大小
     * @param messageType 消息类型（可选）
     * @param status 消息状态（可选）
     * @param userId 用户ID（可选）
     * @return 消息列表
     */
    Result<PageInfo<Message>> getAllUserMessages(Integer page, Integer size, Integer messageType, Integer status, Long userId);

    /**
     * 获取消息详情（管理员）
     * @param messageId 消息ID
     * @return 消息详情
     */
    Result<Message> getAdminMessageDetail(Long messageId);

    /**
     * 回复消息（管理员）
     * @param messageId 原消息ID
     * @param replyContent 回复内容
     * @param adminId 管理员ID
     * @return 操作结果
     */
    Result<Boolean> replyMessage(Long messageId, String replyContent, Long adminId);

    /**
     * 标记消息已读（管理员）
     * @param messageId 消息ID
     * @return 操作结果
     */
    Result<Boolean> markMessageRead(Long messageId);

    /**
     * 批量标记消息已读（管理员）
     * @param messageIds 消息ID列表
     * @return 操作结果
     */
    Result<Boolean> batchMarkMessageRead(List<Long> messageIds);

    /**
     * 批量删除消息（管理员）
     * @param messageIds 消息ID列表
     * @return 操作结果
     */
    Result<Boolean> batchDeleteMessages(List<Long> messageIds);

    /**
     * 获取消息统计信息（管理员）
     * @return 消息统计信息
     */
    Result<Map<String, Object>> getAdminMessageStatistics();

    /**
     * 获取用户聊天列表（管理员）- 按用户分组显示聊天记录
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键词（用户名、昵称、手机号）
     * @return 用户聊天列表
     */
    Result<PageInfo<Map<String, Object>>> getUserChatList(Integer page, Integer size, String keyword);

    /**
     * 获取与特定用户的聊天记录
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 聊天记录列表
     */
    Result<PageInfo<Message>> getUserChatMessages(Long userId, Integer page, Integer size);
}