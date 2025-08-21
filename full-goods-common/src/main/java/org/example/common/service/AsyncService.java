package org.example.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 异步服务类
 * 处理耗时的异步操作
 */
@Slf4j
@Service
public class AsyncService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 异步发送验证码
     * @param mobile 手机号
     * @param code 验证码
     */
    @Async("mailExecutor")
    public void sendVerifyCodeAsync(String mobile, String code) {
        try {
            log.info("开始异步发送验证码，手机号: {}", mobile);
            
            // 模拟发送验证码的耗时操作
            Thread.sleep(1000);
            
            // 将验证码存储到Redis，有效期5分钟
            String key = "verify_code:" + mobile;
            redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
            
            log.info("验证码发送成功，手机号: {}, 验证码: {}", mobile, code);
            
        } catch (Exception e) {
            log.error("发送验证码失败，手机号: {}, 错误: {}", mobile, e.getMessage(), e);
        }
    }
    
    /**
     * 异步记录用户操作日志
     * @param userId 用户ID
     * @param action 操作类型
     * @param details 操作详情
     */
    @Async("logExecutor")
    public void logUserActionAsync(Long userId, String action, String details) {
        try {
            log.info("记录用户操作日志 - 用户ID: {}, 操作: {}, 详情: {}", userId, action, details);
            
            // 这里可以将日志存储到数据库或其他持久化存储
            // 暂时只记录到日志文件
            
        } catch (Exception e) {
            log.error("记录用户操作日志失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 异步更新用户统计信息
     * @param userId 用户ID
     * @param actionType 操作类型
     */
    @Async("taskExecutor")
    public void updateUserStatisticsAsync(Long userId, String actionType) {
        try {
            log.info("异步更新用户统计信息 - 用户ID: {}, 操作类型: {}", userId, actionType);
            
            // 更新用户活跃度、访问次数等统计信息
            String key = "user_stats:" + userId + ":" + actionType;
            redisTemplate.opsForValue().increment(key, 1);
            
            // 设置过期时间为30天
            redisTemplate.expire(key, 30, TimeUnit.DAYS);
            
        } catch (Exception e) {
            log.error("更新用户统计信息失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 异步清理过期数据
     */
    @Async("taskExecutor")
    public void cleanExpiredDataAsync() {
        try {
            log.info("开始异步清理过期数据");
            
            // 清理过期的验证码
            // 清理过期的token
            // 清理过期的临时数据
            
            log.info("异步清理过期数据完成");
            
        } catch (Exception e) {
            log.error("清理过期数据失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 异步发送系统通知
     * @param userId 用户ID
     * @param title 通知标题
     * @param content 通知内容
     */
    @Async("mailExecutor")
    public void sendNotificationAsync(Long userId, String title, String content) {
        try {
            log.info("异步发送系统通知 - 用户ID: {}, 标题: {}", userId, title);
            
            // 这里可以实现推送通知、短信通知等功能
            // 暂时只记录日志
            
            log.info("系统通知发送完成 - 用户ID: {}", userId);
            
        } catch (Exception e) {
            log.error("发送系统通知失败: {}", e.getMessage(), e);
        }
    }
}