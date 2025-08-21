package org.example.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 监控配置类
 */
@Slf4j
@Configuration
@EnableScheduling
public class MonitoringConfig implements WebMvcConfigurer {

    // 请求计数器
    private final AtomicLong requestCount = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    private final AtomicLong slowRequestCount = new AtomicLong(0);

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MonitoringInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/health/**", "/api/actuator/**");
    }

    /**
     * 监控拦截器
     */
    public class MonitoringInterceptor implements HandlerInterceptor {
        
        private static final String START_TIME = "monitoring_start_time";
        
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            request.setAttribute(START_TIME, System.currentTimeMillis());
            requestCount.incrementAndGet();
            return true;
        }
        
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            Long startTime = (Long) request.getAttribute(START_TIME);
            if (startTime != null) {
                long duration = System.currentTimeMillis() - startTime;
                
                // 记录异常
                if (ex != null || response.getStatus() >= 400) {
                    errorCount.incrementAndGet();
                }
                
                // 记录慢请求
                if (duration > 3000) {
                    slowRequestCount.incrementAndGet();
                    log.warn("慢请求监控 - Method: {}, URI: {}, Duration: {}ms, Status: {}", 
                            request.getMethod(), request.getRequestURI(), duration, response.getStatus());
                }
                
                // 记录详细请求信息（仅在DEBUG级别）
                if (log.isDebugEnabled()) {
                    log.debug("请求监控 - Method: {}, URI: {}, Duration: {}ms, Status: {}", 
                            request.getMethod(), request.getRequestURI(), duration, response.getStatus());
                }
            }
        }
    }

    /**
     * 定时输出监控统计信息
     */
    @Scheduled(fixedRate = 300000) // 每5分钟输出一次
    public void logStatistics() {
        long totalRequests = requestCount.get();
        long totalErrors = errorCount.get();
        long totalSlowRequests = slowRequestCount.get();
        
        if (totalRequests > 0) {
            double errorRate = (double) totalErrors / totalRequests * 100;
            double slowRate = (double) totalSlowRequests / totalRequests * 100;
            
            log.info("系统监控统计 - 总请求数: {}, 错误数: {}, 慢请求数: {}, 错误率: {:.2f}%, 慢请求率: {:.2f}%", 
                    totalRequests, totalErrors, totalSlowRequests, errorRate, slowRate);
            
            // 检查系统健康状况
            if (errorRate > 10) {
                log.warn("系统错误率过高: {:.2f}%", errorRate);
            }
            
            if (slowRate > 5) {
                log.warn("系统慢请求率过高: {:.2f}%", slowRate);
            }
        }
    }

    /**
     * 重置统计计数器
     */
    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨重置
    public void resetCounters() {
        requestCount.set(0);
        errorCount.set(0);
        slowRequestCount.set(0);
        log.info("监控统计计数器已重置");
    }
}