package org.example.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 日志工具类
 * 提供统一的日志记录功能
 */
@Slf4j
public class LogUtils {

    // 业务日志记录器
    private static final Logger BUSINESS_LOGGER = LoggerFactory.getLogger("BUSINESS");
    
    // MDC键名常量
    public static final String TRACE_ID = "traceId";
    public static final String USER_ID = "userId";
    public static final String REQUEST_URI = "requestUri";
    public static final String REQUEST_METHOD = "requestMethod";
    public static final String CLIENT_IP = "clientIp";
    public static final String USER_AGENT = "userAgent";

    /**
     * 生成追踪ID
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 设置追踪ID
     */
    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    /**
     * 获取追踪ID
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    /**
     * 设置用户ID
     */
    public static void setUserId(String userId) {
        MDC.put(USER_ID, userId);
    }

    /**
     * 获取用户ID
     */
    public static String getUserId() {
        return MDC.get(USER_ID);
    }

    /**
     * 设置请求信息
     */
    public static void setRequestInfo(HttpServletRequest request) {
        if (request != null) {
            MDC.put(REQUEST_URI, request.getRequestURI());
            MDC.put(REQUEST_METHOD, request.getMethod());
            MDC.put(CLIENT_IP, getClientIp(request));
            MDC.put(USER_AGENT, request.getHeader("User-Agent"));
        }
    }

    /**
     * 清除MDC
     */
    public static void clearMDC() {
        MDC.clear();
    }

    /**
     * 获取客户端IP
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个IP的情况，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 记录业务日志 - INFO级别
     */
    public static void business(String message, Object... args) {
        BUSINESS_LOGGER.info(message, args);
    }

    /**
     * 记录业务日志 - WARN级别
     */
    public static void businessWarn(String message, Object... args) {
        BUSINESS_LOGGER.warn(message, args);
    }

    /**
     * 记录业务日志 - ERROR级别
     */
    public static void businessError(String message, Object... args) {
        BUSINESS_LOGGER.error(message, args);
    }

    /**
     * 记录业务日志 - ERROR级别（带异常）
     */
    public static void businessError(String message, Throwable throwable, Object... args) {
        BUSINESS_LOGGER.error(message, args);
        BUSINESS_LOGGER.error("异常详情: ", throwable);
    }

    /**
     * 记录用户操作日志
     */
    public static void userOperation(String userId, String operation, String details) {
        business("用户操作 - 用户ID: {}, 操作: {}, 详情: {}", userId, operation, details);
    }

    /**
     * 记录API调用日志
     */
    public static void apiCall(String method, String uri, long duration, int status) {
        business("API调用 - 方法: {}, URI: {}, 耗时: {}ms, 状态: {}", method, uri, duration, status);
    }

    /**
     * 记录数据库操作日志
     */
    public static void dbOperation(String operation, String table, long duration) {
        business("数据库操作 - 操作: {}, 表: {}, 耗时: {}ms", operation, table, duration);
    }

    /**
     * 记录缓存操作日志
     */
    public static void cacheOperation(String operation, String key, boolean hit) {
        business("缓存操作 - 操作: {}, 键: {}, 命中: {}", operation, key, hit);
    }

    /**
     * 记录文件操作日志
     */
    public static void fileOperation(String operation, String fileName, long fileSize) {
        business("文件操作 - 操作: {}, 文件: {}, 大小: {}bytes", operation, fileName, fileSize);
    }

    /**
     * 记录登录日志
     */
    public static void loginLog(String userId, String ip, boolean success, String reason) {
        if (success) {
            business("用户登录成功 - 用户ID: {}, IP: {}", userId, ip);
        } else {
            businessWarn("用户登录失败 - 用户ID: {}, IP: {}, 原因: {}", userId, ip, reason);
        }
    }

    /**
     * 记录安全事件日志
     */
    public static void securityEvent(String event, String details, String ip) {
        businessWarn("安全事件 - 事件: {}, 详情: {}, IP: {}", event, details, ip);
    }

    /**
     * 记录性能监控日志
     */
    public static void performance(String component, String operation, long duration, String details) {
        if (duration > 1000) { // 超过1秒记录为警告
            businessWarn("性能监控 - 组件: {}, 操作: {}, 耗时: {}ms, 详情: {}", component, operation, duration, details);
        } else {
            business("性能监控 - 组件: {}, 操作: {}, 耗时: {}ms, 详情: {}", component, operation, duration, details);
        }
    }

    /**
     * 记录异常日志
     */
    public static void exception(String component, String operation, Throwable throwable) {
        businessError("异常发生 - 组件: {}, 操作: {}, 异常: {}", component, operation, throwable.getMessage());
        log.error("异常详情: ", throwable);
    }

    /**
     * 记录系统启动日志
     */
    public static void systemStart(String component, long duration) {
        business("系统启动 - 组件: {}, 耗时: {}ms", component, duration);
    }

    /**
     * 记录系统关闭日志
     */
    public static void systemShutdown(String component) {
        business("系统关闭 - 组件: {}", component);
    }

    /**
     * 记录配置变更日志
     */
    public static void configChange(String configKey, String oldValue, String newValue, String operator) {
        business("配置变更 - 配置项: {}, 旧值: {}, 新值: {}, 操作者: {}", configKey, oldValue, newValue, operator);
    }

    /**
     * 记录定时任务日志
     */
    public static void scheduledTask(String taskName, long duration, boolean success, String details) {
        if (success) {
            business("定时任务执行成功 - 任务: {}, 耗时: {}ms, 详情: {}", taskName, duration, details);
        } else {
            businessError("定时任务执行失败 - 任务: {}, 耗时: {}ms, 详情: {}", taskName, duration, details);
        }
    }

    /**
     * 记录消息队列日志
     */
    public static void messageQueue(String operation, String topic, String messageId, boolean success) {
        if (success) {
            business("消息队列操作成功 - 操作: {}, 主题: {}, 消息ID: {}", operation, topic, messageId);
        } else {
            businessError("消息队列操作失败 - 操作: {}, 主题: {}, 消息ID: {}", operation, topic, messageId);
        }
    }
}