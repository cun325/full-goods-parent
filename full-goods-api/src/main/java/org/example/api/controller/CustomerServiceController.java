package org.example.api.controller;

import org.example.api.service.CustomerServiceSessionService;
import org.example.common.entity.CustomerServiceSession;
import org.example.common.entity.Message;
import org.example.common.response.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 客服系统控制器
 * 提供客服会话管理、消息发送等功能的REST API
 */
@RestController
@RequestMapping("/customer-service")
@CrossOrigin(origins = "*")
public class CustomerServiceController {

    @Autowired
    private CustomerServiceSessionService customerServiceSessionService;

    /**
     * 创建客服会话
     * @param userId 用户ID
     * @param sessionType 会话类型
     * @param title 会话标题
     * @return 会话信息
     */
    @PostMapping("/session/create")
    public Result<CustomerServiceSession> createSession(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "智能客服") String sessionType,
            @RequestParam(defaultValue = "客服咨询") String title) {
        return customerServiceSessionService.createSession(userId, sessionType, title);
    }

    /**
     * 获取用户的会话列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 会话列表
     */
    @GetMapping("/sessions")
    public Result<PageInfo<CustomerServiceSession>> getUserSessions(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return customerServiceSessionService.getUserSessions(userId, page, size);
    }

    /**
     * 获取会话详情
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 会话信息
     */
    @GetMapping("/session/{sessionId}")
    public Result<CustomerServiceSession> getSession(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        return customerServiceSessionService.getSessionDetail(sessionId, userId);
    }

    /**
     * 更新会话的最后消息
     * @param sessionId 会话ID
     * @param content 消息内容
     * @return 操作结果
     */
    @PostMapping("/session/{sessionId}/message")
    public Result<Boolean> updateLastMessage(
            @PathVariable Long sessionId,
            @RequestParam String content) {
        return customerServiceSessionService.updateLastMessage(sessionId, content);
    }

    /**
     * 增加未读消息数
     * @param sessionId 会话ID
     * @param count 增加数量
     * @return 操作结果
     */
    @PutMapping("/session/{sessionId}/unread/increase")
    public Result<Boolean> increaseUnreadCount(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "1") Integer count) {
        return customerServiceSessionService.increaseUnreadCount(sessionId, count);
    }

    /**
     * 清零未读消息数
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/session/{sessionId}/unread/clear")
    public Result<Boolean> clearUnreadCount(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        return customerServiceSessionService.clearUnreadCount(sessionId, userId);
    }

    /**
     * 结束会话
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/session/{sessionId}/end")
    public Result<Boolean> endSession(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        return customerServiceSessionService.endSession(sessionId, userId);
    }

    /**
     * 分配客服
     * @param sessionId 会话ID
     * @param serviceId 客服ID
     * @return 操作结果
     */
    @PutMapping("/session/{sessionId}/assign")
    public Result<Boolean> assignService(
            @PathVariable Long sessionId,
            @RequestParam Long serviceId) {
        return customerServiceSessionService.assignService(sessionId, serviceId);
    }

    /**
     * 获取客服会话统计
     * @param serviceId 客服ID（可选）
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(
            @RequestParam(required = false) Long serviceId) {
        return customerServiceSessionService.getSessionStatistics(serviceId);
    }

    /**
     * 根据会话类型获取用户会话列表
     * @param userId 用户ID
     * @param sessionType 会话类型
     * @param page 页码
     * @param size 每页大小
     * @return 会话列表
     */
    @GetMapping("/sessions/by-type")
    public Result<PageInfo<CustomerServiceSession>> getUserSessionsByType(
            @RequestParam Long userId,
            @RequestParam String sessionType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return customerServiceSessionService.getUserSessionsByType(userId, sessionType, page, size);
    }

    /**
     * 获取客服的会话列表
     * @param serviceId 客服ID
     * @param status 会话状态（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 会话列表
     */
    @GetMapping("/sessions/service")
    public Result<PageInfo<CustomerServiceSession>> getServiceSessions(
            @RequestParam Long serviceId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return customerServiceSessionService.getServiceSessions(serviceId, status, page, size);
    }

    /**
     * 更新会话状态
     * @param sessionId 会话ID
     * @param status 新状态
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/session/{sessionId}/status")
    public Result<Boolean> updateSessionStatus(
            @PathVariable Long sessionId,
            @RequestParam String status,
            @RequestParam Long userId) {
        return customerServiceSessionService.updateSessionStatus(sessionId, status, userId);
    }

    /**
     * 获取智能自动回复
     * @param sessionId 会话ID
     * @param userMessage 用户消息
     * @return 自动回复内容
     */
    @PostMapping("/auto-reply")
    public Result<String> getAutoReply(
            @RequestParam Long sessionId,
            @RequestParam String userMessage) {
        return customerServiceSessionService.getAutoReply(sessionId, userMessage);
    }

    /**
     * 转接到人工客服
     * @param sessionId 会话ID
     * @return 操作结果
     */
    @PutMapping("/session/{sessionId}/transfer")
    public Result<Boolean> transferToHuman(@PathVariable Long sessionId) {
        return customerServiceSessionService.transferToHuman(sessionId);
    }

    /**
     * 获取待分配的会话列表
     * @param page 页码
     * @param size 每页大小
     * @return 会话列表
     */
    @GetMapping("/sessions/pending")
    public Result<PageInfo<CustomerServiceSession>> getPendingSessions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return customerServiceSessionService.getPendingSessions(page, size);
    }

    /**
     * 获取在线客服列表
     * @return 在线客服列表
     */
    @GetMapping("/services/online")
    public Result<List<Map<String, Object>>> getOnlineServices() {
        return customerServiceSessionService.getOnlineServices();
    }
}