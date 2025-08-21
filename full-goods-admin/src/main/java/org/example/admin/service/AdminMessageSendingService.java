package org.example.admin.service;

import org.example.common.response.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 管理端消息发送服务接口
 */
public interface AdminMessageSendingService {

    /**
     * 发送系统消息
     */
    Result<Boolean> sendSystemMessage(Map<String, Object> messageData);

    /**
     * 发送模板消息
     */
    Result<Boolean> sendTemplateMessage(Map<String, Object> messageData);

    /**
     * 发送批量消息
     */
    Result<Boolean> sendBatchMessage(Map<String, Object> messageData);

    /**
     * 获取发送历史
     */
    Result<Map<String, Object>> getSendingHistory(Integer page, Integer size, String sendType, String status, String startTime, String endTime);

    /**
     * 获取发送详情
     */
    Result<Map<String, Object>> getSendingDetail(Long id);

    /**
     * 取消发送
     */
    Result<Boolean> cancelSending(Long id);

    /**
     * 重新发送
     */
    Result<Boolean> resendMessage(Long id);

    /**
     * 获取发送统计
     */
    Result<Map<String, Object>> getSendingStatistics(String type);

    /**
     * 预览模板消息
     */
    Result<Map<String, String>> previewTemplateMessage(Map<String, Object> previewData);

    /**
     * 验证用户列表文件
     */
    Result<Map<String, Object>> validateUserListFile(MultipartFile file);

    /**
     * 获取用户组列表
     */
    Result<List<Map<String, Object>>> getUserGroups();

    /**
     * 获取用户列表
     */
    Result<List<Map<String, Object>>> getUsers(String search, Long groupId);

    /**
     * 保存草稿
     */
    Result<Long> saveDraft(Map<String, Object> draftData);

    /**
     * 获取草稿列表
     */
    Result<List<Map<String, Object>>> getDrafts();

    /**
     * 删除草稿
     */
    Result<Boolean> deleteDraft(Long id);

    /**
     * 从草稿创建消息
     */
    Result<Boolean> createFromDraft(Long id);

    /**
     * 获取发送配置
     */
    Result<Map<String, Object>> getSendingConfig();

    /**
     * 更新发送配置
     */
    Result<Boolean> updateSendingConfig(Map<String, Object> configData);

    /**
     * 测试消息发送
     */
    Result<Boolean> testMessageSending(Map<String, Object> testData);

    /**
     * 获取消息队列状态
     */
    Result<Map<String, Object>> getQueueStatus();

    /**
     * 清空消息队列
     */
    Result<Boolean> clearQueue();

    /**
     * 暂停消息发送
     */
    Result<Boolean> pauseSending();

    /**
     * 恢复消息发送
     */
    Result<Boolean> resumeSending();

    /**
     * 导出发送历史
     */
    Result<String> exportSendingHistory(String sendType, String status, String startTime, String endTime);

    /**
     * 获取消息发送报告
     */
    Result<Map<String, Object>> getSendingReport(String reportType, String startTime, String endTime);
}