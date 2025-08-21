package org.example.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 */
@Api(tags = "健康检查")
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation("系统健康检查")
    @GetMapping
    public Result<Map<String, Object>> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        
        try {
            // 检查应用状态
            healthInfo.put("status", "UP");
            healthInfo.put("timestamp", System.currentTimeMillis());
            
            // 检查内存使用情况
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            
            Map<String, Object> memoryInfo = new HashMap<>();
            memoryInfo.put("total", totalMemory);
            memoryInfo.put("free", freeMemory);
            memoryInfo.put("used", usedMemory);
            memoryInfo.put("usage", String.format("%.2f%%", (double) usedMemory / totalMemory * 100));
            healthInfo.put("memory", memoryInfo);
            
            // 检查数据库连接
            Map<String, Object> dbInfo = new HashMap<>();
            try (Connection connection = dataSource.getConnection()) {
                dbInfo.put("status", "UP");
                dbInfo.put("database", connection.getMetaData().getDatabaseProductName());
                dbInfo.put("version", connection.getMetaData().getDatabaseProductVersion());
            } catch (Exception e) {
                dbInfo.put("status", "DOWN");
                dbInfo.put("error", e.getMessage());
                log.error("Database health check failed", e);
            }
            healthInfo.put("database", dbInfo);
            
            // 检查Redis连接
            Map<String, Object> redisInfo = new HashMap<>();
            try {
                redisTemplate.opsForValue().set("health:check", "ok");
                String result = (String) redisTemplate.opsForValue().get("health:check");
                redisTemplate.delete("health:check");
                
                if ("ok".equals(result)) {
                    redisInfo.put("status", "UP");
                } else {
                    redisInfo.put("status", "DOWN");
                    redisInfo.put("error", "Connection test failed");
                }
            } catch (Exception e) {
                redisInfo.put("status", "DOWN");
                redisInfo.put("error", e.getMessage());
                log.error("Redis health check failed", e);
            }
            healthInfo.put("redis", redisInfo);
            
            return Result.success(healthInfo);
            
        } catch (Exception e) {
            log.error("Health check failed", e);
            healthInfo.put("status", "DOWN");
            healthInfo.put("error", e.getMessage());
            return Result.failed("Health check failed: " + e.getMessage());
        }
    }

    @ApiOperation("简单健康检查")
    @GetMapping("/simple")
    public Result<String> simpleHealth() {
        return Result.success("OK");
    }
}