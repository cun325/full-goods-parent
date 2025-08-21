package org.example.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.example.common.utils.LogUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 请求日志拦截器
 * 记录API请求的详细信息
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";
    private static final String TRACE_ID = "traceId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 生成追踪ID
        String traceId = LogUtils.generateTraceId();
        LogUtils.setTraceId(traceId);
        
        // 设置请求信息到MDC
        LogUtils.setRequestInfo(request);
        
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);
        request.setAttribute(TRACE_ID, traceId);
        
        // 记录请求开始日志
        logRequestStart(request);
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 可以在这里处理响应前的逻辑
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            // 计算请求耗时
            Long startTime = (Long) request.getAttribute(START_TIME);
            long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;
            
            // 记录请求完成日志
            logRequestEnd(request, response, duration, ex);
            
        } finally {
            // 清除MDC
            LogUtils.clearMDC();
        }
    }

    /**
     * 记录请求开始日志
     */
    private void logRequestStart(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("请求开始 - ")
                  .append("方法: ").append(method)
                  .append(", URI: ").append(uri);
        
        if (queryString != null && !queryString.isEmpty()) {
            logMessage.append(", 参数: ").append(queryString);
        }
        
        logMessage.append(", IP: ").append(clientIp);
        
        log.info(logMessage.toString());
        
        // 记录用户代理（如果需要）
        if (userAgent != null && log.isDebugEnabled()) {
            log.debug("User-Agent: {}", userAgent);
        }
    }

    /**
     * 记录请求结束日志
     */
    private void logRequestEnd(HttpServletRequest request, HttpServletResponse response, long duration, Exception ex) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();
        
        if (ex != null) {
            // 有异常的情况
            log.error("请求异常 - 方法: {}, URI: {}, 耗时: {}ms, 状态: {}, 异常: {}", 
                     method, uri, duration, status, ex.getMessage(), ex);
        } else {
            // 正常完成的情况
            if (duration > 3000) {
                // 超过3秒的请求记录为警告
                log.warn("请求完成(慢) - 方法: {}, URI: {}, 耗时: {}ms, 状态: {}", 
                        method, uri, duration, status);
            } else {
                log.info("请求完成 - 方法: {}, URI: {}, 耗时: {}ms, 状态: {}", 
                        method, uri, duration, status);
            }
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}