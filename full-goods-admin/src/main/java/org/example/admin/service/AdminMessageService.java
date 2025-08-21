package org.example.admin.service;

import org.example.common.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * 管理端消息服务接口
 */
public interface AdminMessageService {

    /**
     * 获取消息列表
     */
    Map<String, Object> getMessageList(int page, int size, String search, String status, String type);

    /**
     * 根据ID获取消息
     */
    Message getMessageById(Long id);

    /**
     * 回复消息
     */
    Boolean replyMessage(Long messageId, String replyContent);

    /**
     * 标记消息已读/未读
     */
    Boolean markMessageRead(Long messageId, Boolean isRead);

    /**
     * 批量标记消息已读/未读
     */
    Boolean batchMarkMessageRead(List<Long> messageIds, Boolean isRead);

    /**
     * 批量删除消息
     */
    Boolean batchDeleteMessages(List<Long> messageIds);

    /**
     * 生成AI回复
     */
    String generateAiReply(Long messageId);

    /**
     * 获取消息统计数据
     */
    Map<String, Object> getMessageStatistics();

    /**
     * 导出消息数据
     */
    String exportMessages(String search, String status, String type);

    /**
     * 发送系统消息
     */
    Boolean sendSystemMessage(Map<String, Object> messageData);

    /**
     * 发送批量消息
     */
    Boolean sendBatchMessage(Map<String, Object> messageData);
}