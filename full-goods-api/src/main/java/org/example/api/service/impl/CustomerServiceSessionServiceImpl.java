package org.example.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.api.mapper.CustomerServiceSessionMapper;
import org.example.api.service.CustomerServiceSessionService;
import org.example.common.entity.CustomerServiceSession;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 客服会话服务实现类
 */
@Slf4j
@Service
public class CustomerServiceSessionServiceImpl implements CustomerServiceSessionService {

    @Autowired
    private CustomerServiceSessionMapper customerServiceSessionMapper;

    @Override
    public Result<CustomerServiceSession> createSession(Long userId, String sessionType, String title) {
        try {
            CustomerServiceSession session = new CustomerServiceSession();
            session.setUserId(userId);
            session.setSessionType("智能客服".equals(sessionType) ? 1 : 2);
            session.setTitle(title);
            session.setStatus(1); // 进行中
            session.setUnreadCount(0);
            session.setStartTime(new Date());
            session.setCreateTime(new Date());
            session.setUpdateTime(new Date());
            
            int result = customerServiceSessionMapper.insert(session);
            if (result > 0) {
                return Result.success(session);
            } else {
                return Result.failed("创建会话失败");
            }
        } catch (Exception e) {
            log.error("创建客服会话失败", e);
            return Result.failed("创建会话失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PageInfo<CustomerServiceSession>> getUserSessions(Long userId, Integer page, Integer size) {
        try {
            PageHelper.startPage(page, size);
            List<CustomerServiceSession> sessions = customerServiceSessionMapper.selectByUserId(userId);
            PageInfo<CustomerServiceSession> pageInfo = new PageInfo<>(sessions);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("获取用户会话列表失败", e);
            return Result.failed("获取会话列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PageInfo<CustomerServiceSession>> getUserSessionsByType(Long userId, String sessionType, Integer page, Integer size) {
        try {
            PageHelper.startPage(page, size);
            List<CustomerServiceSession> sessions = customerServiceSessionMapper.selectByUserId(userId);
            // 过滤会话类型
            Integer type = "智能客服".equals(sessionType) ? 1 : 2;
            sessions = sessions.stream()
                    .filter(session -> session.getSessionType().equals(type))
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
            PageInfo<CustomerServiceSession> pageInfo = new PageInfo<>(sessions);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("获取用户会话列表失败", e);
            return Result.failed("获取会话列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<CustomerServiceSession> getSessionDetail(Long sessionId, Long userId) {
        try {
            CustomerServiceSession session = customerServiceSessionMapper.selectById(sessionId);
            if (session == null) {
                return Result.failed("会话不存在");
            }
            if (!session.getUserId().equals(userId)) {
                return Result.failed("无权访问该会话");
            }
            return Result.success(session);
        } catch (Exception e) {
            log.error("获取会话详情失败", e);
            return Result.failed("获取会话详情失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> updateSessionStatus(Long sessionId, String status, Long userId) {
        try {
            CustomerServiceSession session = customerServiceSessionMapper.selectById(sessionId);
            if (session == null) {
                return Result.failed("会话不存在");
            }
            if (!session.getUserId().equals(userId)) {
                return Result.failed("无权操作该会话");
            }
            
            Integer statusCode = getStatusCode(status);
            int result = customerServiceSessionMapper.updateStatus(sessionId, statusCode);
            return result > 0 ? Result.success(true) : Result.failed("更新状态失败");
        } catch (Exception e) {
            log.error("更新会话状态失败", e);
            return Result.failed("更新状态失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> endSession(Long sessionId, Long userId) {
        try {
            CustomerServiceSession session = customerServiceSessionMapper.selectById(sessionId);
            if (session == null) {
                return Result.failed("会话不存在");
            }
            if (!session.getUserId().equals(userId)) {
                return Result.failed("无权操作该会话");
            }
            
            session.setStatus(3); // 已结束
            session.setEndTime(new Date());
            session.setUpdateTime(new Date());
            
            int result = customerServiceSessionMapper.update(session);
            return result > 0 ? Result.success(true) : Result.failed("结束会话失败");
        } catch (Exception e) {
            log.error("结束会话失败", e);
            return Result.failed("结束会话失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> updateLastMessage(Long sessionId, String lastMessageContent) {
        try {
            int result = customerServiceSessionMapper.updateLastMessage(sessionId, lastMessageContent);
            return result > 0 ? Result.success(true) : Result.failed("更新最后消息失败");
        } catch (Exception e) {
            log.error("更新最后消息失败", e);
            return Result.failed("更新最后消息失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> increaseUnreadCount(Long sessionId, Integer count) {
        try {
            int result = customerServiceSessionMapper.increaseUnreadCount(sessionId, count);
            return result > 0 ? Result.success(true) : Result.failed("增加未读数失败");
        } catch (Exception e) {
            log.error("增加未读数失败", e);
            return Result.failed("增加未读数失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> clearUnreadCount(Long sessionId, Long userId) {
        try {
            CustomerServiceSession session = customerServiceSessionMapper.selectById(sessionId);
            if (session == null) {
                return Result.failed("会话不存在");
            }
            if (!session.getUserId().equals(userId)) {
                return Result.failed("无权操作该会话");
            }
            
            int result = customerServiceSessionMapper.clearUnreadCount(sessionId);
            return result > 0 ? Result.success(true) : Result.failed("清零未读数失败");
        } catch (Exception e) {
            log.error("清零未读数失败", e);
            return Result.failed("清零未读数失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> assignService(Long sessionId, Long serviceId) {
        try {
            int result = customerServiceSessionMapper.assignService(sessionId, serviceId);
            return result > 0 ? Result.success(true) : Result.failed("分配客服失败");
        } catch (Exception e) {
            log.error("分配客服失败", e);
            return Result.failed("分配客服失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PageInfo<CustomerServiceSession>> getServiceSessions(Long serviceId, String status, Integer page, Integer size) {
        try {
            PageHelper.startPage(page, size);
            List<CustomerServiceSession> sessions = customerServiceSessionMapper.selectByServiceId(serviceId);
            
            // 过滤状态
            if (status != null && !status.isEmpty()) {
                Integer statusCode = getStatusCode(status);
                sessions = sessions.stream()
                        .filter(session -> session.getStatus().equals(statusCode))
                        .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
            }
            
            PageInfo<CustomerServiceSession> pageInfo = new PageInfo<>(sessions);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("获取客服会话列表失败", e);
            return Result.failed("获取会话列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PageInfo<CustomerServiceSession>> getPendingSessions(Integer page, Integer size) {
        try {
            PageHelper.startPage(page, size);
            List<CustomerServiceSession> sessions = customerServiceSessionMapper.selectWaitingByType(2); // 人工客服等待中
            PageInfo<CustomerServiceSession> pageInfo = new PageInfo<>(sessions);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("获取待分配会话列表失败", e);
            return Result.failed("获取待分配会话列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getSessionStatistics(Long userId) {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            if (userId != null) {
                // 用户统计
                int totalSessions = customerServiceSessionMapper.countByUserId(userId);
                statistics.put("totalSessions", totalSessions);
            } else {
                // 全局统计
                statistics.put("totalSessions", 0);
                statistics.put("activeSessions", 0);
                statistics.put("pendingSessions", 0);
            }
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取会话统计失败", e);
            return Result.failed("获取统计信息失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> getAutoReply(Long sessionId, String userMessage) {
        try {
            // 简单的自动回复逻辑
            String reply = generateAutoReply(userMessage);
            
            // 更新会话的最后消息
            updateLastMessage(sessionId, reply);
            
            return Result.success(reply);
        } catch (Exception e) {
            log.error("获取自动回复失败", e);
            return Result.failed("获取自动回复失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> transferToHuman(Long sessionId) {
        try {
            CustomerServiceSession session = customerServiceSessionMapper.selectById(sessionId);
            if (session == null) {
                return Result.failed("会话不存在");
            }
            
            session.setSessionType(2); // 转为人工客服
            session.setStatus(1); // 等待中
            session.setUpdateTime(new Date());
            
            int result = customerServiceSessionMapper.update(session);
            return result > 0 ? Result.success(true) : Result.failed("转接失败");
        } catch (Exception e) {
            log.error("转接人工客服失败", e);
            return Result.failed("转接失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<Map<String, Object>>> getOnlineServices() {
        try {
            // 模拟在线客服数据
            List<Map<String, Object>> services = new ArrayList<>();
            Map<String, Object> service1 = new HashMap<>();
            service1.put("id", 1L);
            service1.put("name", "客服小王");
            service1.put("status", "在线");
            service1.put("sessionCount", 3);
            services.add(service1);
            
            Map<String, Object> service2 = new HashMap<>();
            service2.put("id", 2L);
            service2.put("name", "客服小李");
            service2.put("status", "在线");
            service2.put("sessionCount", 2);
            services.add(service2);
            
            return Result.success(services);
        } catch (Exception e) {
            log.error("获取在线客服列表失败", e);
            return Result.failed("获取在线客服列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取状态码
     */
    private Integer getStatusCode(String status) {
        switch (status) {
            case "进行中":
                return 1;
            case "等待中":
                return 2;
            case "已结束":
                return 3;
            default:
                return 1;
        }
    }

    /**
     * 生成自动回复
     */
    private String generateAutoReply(String userMessage) {
        if (userMessage.contains("订单")) {
            return "您好！关于订单问题，您可以在\"我的订单\"中查看订单状态，或者提供订单号我来帮您查询。";
        } else if (userMessage.contains("退款") || userMessage.contains("退货")) {
            return "关于退款退货，请在订单详情页面申请，我们会在1-3个工作日内处理。如需加急处理，请转人工客服。";
        } else if (userMessage.contains("物流") || userMessage.contains("快递")) {
            return "物流信息您可以在订单详情中查看，或者提供订单号我来帮您查询物流状态。";
        } else if (userMessage.contains("优惠") || userMessage.contains("活动")) {
            return "最新优惠活动请关注首页推荐，新用户还有专享优惠券哦！";
        } else {
            return "您好！我是智能客服助手，很高兴为您服务。如需更详细的帮助，请转人工客服。";
        }
    }
}