package org.example.api.service;

import com.github.pagehelper.PageInfo;
import org.example.common.entity.CustomerServiceSession;
import org.example.common.response.Result;

import java.util.List;
import java.util.Map;

/**
 * 客服会话服务接口
 */
public interface CustomerServiceSessionService {

    /**
     * 创建客服会话
     * @param userId 用户ID
     * @param sessionType 会话类型（智能客服/人工客服）
     * @param title 会话标题
     * @return 会话信息
     */
    Result<CustomerServiceSession> createSession(Long userId, String sessionType, String title);

    /**
     * 获取用户的客服会话列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 会话列表
     */
    Result<PageInfo<CustomerServiceSession>> getUserSessions(Long userId, Integer page, Integer size);

    /**
     * 根据会话类型获取用户会话列表
     * @param userId 用户ID
     * @param sessionType 会话类型
     * @param page 页码
     * @param size 每页大小
     * @return 会话列表
     */
    Result<PageInfo<CustomerServiceSession>> getUserSessionsByType(Long userId, String sessionType, Integer page, Integer size);

    /**
     * 获取会话详情
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 会话详情
     */
    Result<CustomerServiceSession> getSessionDetail(Long sessionId, Long userId);

    /**
     * 更新会话状态
     * @param sessionId 会话ID
     * @param status 新状态
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Boolean> updateSessionStatus(Long sessionId, String status, Long userId);

    /**
     * 结束会话
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Boolean> endSession(Long sessionId, Long userId);

    /**
     * 更新会话的最后一条消息
     * @param sessionId 会话ID
     * @param lastMessageContent 最后一条消息内容
     * @return 操作结果
     */
    Result<Boolean> updateLastMessage(Long sessionId, String lastMessageContent);

    /**
     * 增加未读消息数
     * @param sessionId 会话ID
     * @param count 增加数量
     * @return 操作结果
     */
    Result<Boolean> increaseUnreadCount(Long sessionId, Integer count);

    /**
     * 清零未读消息数
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Boolean> clearUnreadCount(Long sessionId, Long userId);

    /**
     * 分配客服
     * @param sessionId 会话ID
     * @param serviceId 客服ID
     * @return 操作结果
     */
    Result<Boolean> assignService(Long sessionId, Long serviceId);

    /**
     * 获取客服的会话列表（客服端使用）
     * @param serviceId 客服ID
     * @param status 会话状态
     * @param page 页码
     * @param size 每页大小
     * @return 会话列表
     */
    Result<PageInfo<CustomerServiceSession>> getServiceSessions(Long serviceId, String status, Integer page, Integer size);

    /**
     * 获取待分配的会话列表（管理端使用）
     * @param page 页码
     * @param size 每页大小
     * @return 会话列表
     */
    Result<PageInfo<CustomerServiceSession>> getPendingSessions(Integer page, Integer size);

    /**
     * 获取会话统计信息
     * @param userId 用户ID（可选，为null时获取全局统计）
     * @return 统计信息
     */
    Result<Map<String, Object>> getSessionStatistics(Long userId);

    /**
     * 智能客服自动回复
     * @param sessionId 会话ID
     * @param userMessage 用户消息
     * @return 自动回复内容
     */
    Result<String> getAutoReply(Long sessionId, String userMessage);

    /**
     * 转接到人工客服
     * @param sessionId 会话ID
     * @return 操作结果
     */
    Result<Boolean> transferToHuman(Long sessionId);

    /**
     * 获取在线客服列表
     * @return 在线客服列表
     */
    Result<List<Map<String, Object>>> getOnlineServices();
}