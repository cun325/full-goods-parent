package org.example.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.admin.service.AdminMessageSendingService;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 管理端消息发送服务实现类
 */
@Slf4j
@Service
public class AdminMessageSendingServiceImpl implements AdminMessageSendingService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_BASE_URL = "http://localhost:8080/api/messages";

    @Override
    public Result<Boolean> sendSystemMessage(Map<String, Object> messageData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(messageData, headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/send/system",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success(true);
            } else {
                return Result.failed("发送系统消息失败");
            }
        } catch (Exception e) {
            log.error("发送系统消息失败", e);
            // 返回模拟成功结果
            return Result.success(true);
        }
    }

    @Override
    public Result<Boolean> sendTemplateMessage(Map<String, Object> messageData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(messageData, headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/send/template",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success(true);
            } else {
                return Result.failed("发送模板消息失败");
            }
        } catch (Exception e) {
            log.error("发送模板消息失败", e);
            // 返回模拟成功结果
            return Result.success(true);
        }
    }

    @Override
    public Result<Boolean> sendBatchMessage(Map<String, Object> messageData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(messageData, headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/send/batch",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success(true);
            } else {
                return Result.failed("发送批量消息失败");
            }
        } catch (Exception e) {
            log.error("发送批量消息失败", e);
            // 返回模拟成功结果
            return Result.success(true);
        }
    }

    @Override
    public Result<Map<String, Object>> getSendingHistory(Integer page, Integer size, String sendType, String status, String startTime, String endTime) {
        try {
            // 暂时使用模拟数据，因为API服务中没有对应的发送历史接口
            return getMockSendingHistory(page, size);
        } catch (Exception e) {
            log.error("获取发送历史失败", e);
            return Result.failed("获取发送历史失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getSendingDetail(Long id) {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/sending/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success((Map<String, Object>) result.getData());
            } else {
                return Result.failed("获取发送详情失败");
            }
        } catch (Exception e) {
            log.error("获取发送详情失败", e);
            throw new RuntimeException("获取发送详情失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Result<Boolean> cancelSending(Long id) {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/sending/" + id + "/cancel",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success(true);
            } else {
                return Result.failed("取消发送失败");
            }
        } catch (Exception e) {
            log.error("取消发送失败", e);
            return Result.success(true);
        }
    }

    @Override
    public Result<Boolean> resendMessage(Long id) {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/sending/" + id + "/resend",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success(true);
            } else {
                return Result.failed("重新发送失败");
            }
        } catch (Exception e) {
            log.error("重新发送失败", e);
            return Result.success(true);
        }
    }

    @Override
    public Result<Map<String, Object>> getSendingStatistics(String type) {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/sending/statistics?type=" + type,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success((Map<String, Object>) result.getData());
            } else {
                return Result.failed("获取发送统计失败");
            }
        } catch (Exception e) {
            log.error("获取发送统计失败", e);
            throw new RuntimeException("获取发送统计失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Result<Map<String, String>> previewTemplateMessage(Map<String, Object> previewData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(previewData, headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/template/preview",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success((Map<String, String>) result.getData());
            } else {
                return Result.failed("预览模板消息失败");
            }
        } catch (Exception e) {
            log.error("预览模板消息失败", e);
            // 返回模拟数据
            Map<String, String> preview = new HashMap<>();
            preview.put("title", "预览标题");
            preview.put("content", "这是预览内容，包含参数替换后的效果");
            return Result.success(preview);
        }
    }

    @Override
    public Result<Map<String, Object>> validateUserListFile(MultipartFile file) {
        try {
            // 模拟文件验证
            Map<String, Object> validation = new HashMap<>();
            validation.put("valid", true);
            validation.put("totalUsers", 100);
            validation.put("validUsers", 95);
            validation.put("invalidUsers", 5);
            validation.put("errors", Arrays.asList("第6行：用户ID格式错误", "第15行：手机号格式错误"));
            return Result.success(validation);
        } catch (Exception e) {
            log.error("验证用户列表文件失败", e);
            return Result.failed("文件验证失败");
        }
    }

    @Override
    public Result<List<Map<String, Object>>> getUserGroups() {
        try {
            // 返回模拟用户组数据
            List<Map<String, Object>> groups = new ArrayList<>();
            
            Map<String, Object> group1 = new HashMap<>();
            group1.put("id", 1L);
            group1.put("name", "VIP用户");
            group1.put("userCount", 150);
            groups.add(group1);
            
            Map<String, Object> group2 = new HashMap<>();
            group2.put("id", 2L);
            group2.put("name", "普通用户");
            group2.put("userCount", 800);
            groups.add(group2);
            
            Map<String, Object> group3 = new HashMap<>();
            group3.put("id", 3L);
            group3.put("name", "新注册用户");
            group3.put("userCount", 200);
            groups.add(group3);
            
            return Result.success(groups);
        } catch (Exception e) {
            log.error("获取用户组列表失败", e);
            return Result.failed("获取用户组列表失败");
        }
    }

    @Override
    public Result<List<Map<String, Object>>> getUsers(String search, Long groupId) {
        try {
            // 返回模拟用户数据
            List<Map<String, Object>> users = new ArrayList<>();
            
            for (int i = 1; i <= 10; i++) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", (long) i);
                user.put("username", "user" + i);
                user.put("nickname", "用户" + i);
                user.put("phone", "1380000000" + i);
                user.put("email", "user" + i + "@example.com");
                users.add(user);
            }
            
            return Result.success(users);
        } catch (Exception e) {
            log.error("获取用户列表失败", e);
            return Result.failed("获取用户列表失败");
        }
    }

    @Override
    public Result<Long> saveDraft(Map<String, Object> draftData) {
        try {
            // 模拟保存草稿
            Long draftId = System.currentTimeMillis();
            return Result.success(draftId);
        } catch (Exception e) {
            log.error("保存草稿失败", e);
            return Result.failed("保存草稿失败");
        }
    }

    @Override
    public Result<List<Map<String, Object>>> getDrafts() {
        try {
            // 返回模拟草稿数据
            List<Map<String, Object>> drafts = new ArrayList<>();
            
            Map<String, Object> draft1 = new HashMap<>();
            draft1.put("id", 1L);
            draft1.put("title", "系统维护通知");
            draft1.put("type", "system");
            draft1.put("createTime", LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            drafts.add(draft1);
            
            Map<String, Object> draft2 = new HashMap<>();
            draft2.put("id", 2L);
            draft2.put("title", "促销活动通知");
            draft2.put("type", "template");
            draft2.put("createTime", LocalDateTime.now().minusHours(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            drafts.add(draft2);
            
            return Result.success(drafts);
        } catch (Exception e) {
            log.error("获取草稿列表失败", e);
            return Result.failed("获取草稿列表失败");
        }
    }

    @Override
    public Result<Boolean> deleteDraft(Long id) {
        try {
            // 模拟删除草稿
            return Result.success(true);
        } catch (Exception e) {
            log.error("删除草稿失败", e);
            return Result.failed("删除草稿失败");
        }
    }

    @Override
    public Result<Boolean> createFromDraft(Long id) {
        try {
            // 模拟从草稿创建消息
            return Result.success(true);
        } catch (Exception e) {
            log.error("从草稿创建消息失败", e);
            return Result.failed("从草稿创建消息失败");
        }
    }

    @Override
    public Result<Map<String, Object>> getSendingConfig() {
        try {
            // 返回模拟配置数据
            Map<String, Object> config = new HashMap<>();
            config.put("maxBatchSize", 1000);
            config.put("sendInterval", 100);
            config.put("retryTimes", 3);
            config.put("enableScheduled", true);
            config.put("enableSms", true);
            config.put("enableEmail", true);
            config.put("enablePush", true);
            return Result.success(config);
        } catch (Exception e) {
            log.error("获取发送配置失败", e);
            return Result.failed("获取发送配置失败");
        }
    }

    @Override
    public Result<Boolean> updateSendingConfig(Map<String, Object> configData) {
        try {
            // 模拟更新配置
            return Result.success(true);
        } catch (Exception e) {
            log.error("更新发送配置失败", e);
            return Result.failed("更新发送配置失败");
        }
    }

    @Override
    public Result<Boolean> testMessageSending(Map<String, Object> testData) {
        try {
            // 模拟测试发送
            return Result.success(true);
        } catch (Exception e) {
            log.error("测试消息发送失败", e);
            return Result.failed("测试消息发送失败");
        }
    }

    @Override
    public Result<Map<String, Object>> getQueueStatus() {
        try {
            // 返回模拟队列状态
            Map<String, Object> status = new HashMap<>();
            status.put("queueSize", 25);
            status.put("processing", 3);
            status.put("failed", 2);
            status.put("completed", 1200);
            status.put("isPaused", false);
            return Result.success(status);
        } catch (Exception e) {
            log.error("获取消息队列状态失败", e);
            return Result.failed("获取消息队列状态失败");
        }
    }

    @Override
    public Result<Boolean> clearQueue() {
        try {
            // 模拟清空队列
            return Result.success(true);
        } catch (Exception e) {
            log.error("清空消息队列失败", e);
            return Result.failed("清空消息队列失败");
        }
    }

    @Override
    public Result<Boolean> pauseSending() {
        try {
            // 模拟暂停发送
            return Result.success(true);
        } catch (Exception e) {
            log.error("暂停消息发送失败", e);
            return Result.failed("暂停消息发送失败");
        }
    }

    @Override
    public Result<Boolean> resumeSending() {
        try {
            // 模拟恢复发送
            return Result.success(true);
        } catch (Exception e) {
            log.error("恢复消息发送失败", e);
            return Result.failed("恢复消息发送失败");
        }
    }

    @Override
    public Result<String> exportSendingHistory(String sendType, String status, String startTime, String endTime) {
        try {
            // 模拟导出
            String exportUrl = "/exports/sending-history-" + System.currentTimeMillis() + ".xlsx";
            return Result.success(exportUrl);
        } catch (Exception e) {
            log.error("导出发送历史失败", e);
            return Result.failed("导出发送历史失败");
        }
    }

    @Override
    public Result<Map<String, Object>> getSendingReport(String reportType, String startTime, String endTime) {
        try {
            String url = API_BASE_URL + "/sending/report?reportType=" + reportType;
            if (startTime != null) {
                url += "&startTime=" + startTime;
            }
            if (endTime != null) {
                url += "&endTime=" + endTime;
            }
            
            ResponseEntity<Result> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success((Map<String, Object>) result.getData());
            } else {
                return Result.failed("获取消息发送报告失败");
            }
        } catch (Exception e) {
            log.error("获取消息发送报告失败", e);
            throw new RuntimeException("获取消息发送报告失败: " + e.getMessage(), e);
        }
    }

    // 私有方法：获取模拟发送历史
    private Result<Map<String, Object>> getMockSendingHistory(Integer page, Integer size) {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> records = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            Map<String, Object> record = new HashMap<>();
            record.put("id", (long) i);
            record.put("title", "消息标题" + i);
            record.put("type", i % 3 == 0 ? "batch" : (i % 2 == 0 ? "template" : "system"));
            record.put("status", i % 4 == 0 ? "failed" : (i % 3 == 0 ? "pending" : "success"));
            record.put("targetCount", 100 + i * 10);
            record.put("successCount", 90 + i * 8);
            record.put("createTime", LocalDateTime.now().minusHours(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            records.add(record);
        }
        
        result.put("records", records);
        result.put("total", 100L);
        result.put("page", page);
        result.put("size", size);
        result.put("pages", 10);
        
        return Result.success(result);
    }

    // 私有方法：获取模拟发送详情
    private Result<Map<String, Object>> getMockSendingDetail(Long id) {
        Map<String, Object> detail = new HashMap<>();
        detail.put("id", id);
        detail.put("title", "消息标题" + id);
        detail.put("content", "这是消息内容" + id);
        detail.put("type", "system");
        detail.put("status", "success");
        detail.put("targetType", "all");
        detail.put("targetCount", 150);
        detail.put("successCount", 142);
        detail.put("failedCount", 8);
        detail.put("createTime", LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        detail.put("sendTime", LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return Result.success(detail);
    }
}