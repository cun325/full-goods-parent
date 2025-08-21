package org.example.admin.service.impl;

import org.example.admin.service.AdminMessageService;
import org.example.common.entity.Message;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 管理员消息服务实现类
 */
@Slf4j
@Service
public class AdminMessageServiceImpl implements AdminMessageService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_BASE_URL = "http://localhost:8080/api";

    @Override
    public Map<String, Object> getMessageList(int page, int size, String search, String status, String type) {
        try {
            // 构建查询参数
            StringBuilder urlBuilder = new StringBuilder(API_BASE_URL + "/messages/user/list?page=" + page + "&size=" + size);
            
            if (status != null && !status.isEmpty()) {
                urlBuilder.append("&status=").append(status);
            }
            if (type != null && !type.isEmpty()) {
                urlBuilder.append("&messageType=").append(type);
            }
            if (search != null && !search.isEmpty()) {
                // 如果search是数字，作为userId查询
                try {
                    Long userId = Long.parseLong(search);
                    urlBuilder.append("&userId=").append(userId);
                } catch (NumberFormatException e) {
                    // 如果不是数字，忽略search参数
                }
            }
            
            ResponseEntity<Result> response = restTemplate.exchange(
                urlBuilder.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return (Map<String, Object>) result.getData();
            } else {
                throw new RuntimeException("API调用失败: " + (result != null ? result.getMessage() : "未知错误"));
            }
        } catch (Exception e) {
            throw new RuntimeException("获取消息列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Message getMessageById(Long id) {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/messages/admin/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return (Message) result.getData();
            } else {
                throw new RuntimeException("API调用失败: " + (result != null ? result.getMessage() : "未知错误"));
            }
        } catch (Exception e) {
            throw new RuntimeException("获取消息详情失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean replyMessage(Long messageId, String replyContent) {
        try {
            // 使用默认管理员ID，实际项目中应该从当前登录用户获取
            Long adminId = 1L;
            
            String url = API_BASE_URL + "/messages/admin/" + messageId + "/reply";
            
            // 构建表单数据
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            String formData = "replyContent=" + java.net.URLEncoder.encode(replyContent, "UTF-8") + 
                             "&adminId=" + adminId;
            
            log.info("发送回复消息请求 - URL: {}, FormData: {}", url, formData);
            
            HttpEntity<String> entity = new HttpEntity<>(formData, headers);
            
            ResponseEntity<Result> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            log.info("回复消息响应 - Status: {}, Body: {}", response.getStatusCode(), response.getBody());

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return true;
            }
            
            throw new RuntimeException("回复消息失败: API返回错误");
        } catch (Exception e) {
            log.error("回复消息失败: {}", e.getMessage(), e);
            throw new RuntimeException("回复消息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean markMessageRead(Long messageId, Boolean isRead) {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/messages/admin/" + messageId + "/read?isRead=" + isRead,
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return true;
            }
            
            throw new RuntimeException("标记消息状态失败: API返回错误");
        } catch (Exception e) {
            throw new RuntimeException("标记消息状态失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean batchMarkMessageRead(List<Long> messageIds, Boolean isRead) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messageIds", messageIds);
            requestBody.put("isRead", isRead);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/messages/admin/read/batch",
                HttpMethod.PUT,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return true;
            }
            
            throw new RuntimeException("批量标记消息状态失败: API返回错误");
        } catch (Exception e) {
            throw new RuntimeException("批量标记消息状态失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean batchDeleteMessages(List<Long> messageIds) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<List<Long>> entity = new HttpEntity<>(messageIds, headers);
            
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/messages/admin/batch",
                HttpMethod.DELETE,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return true;
            }
            
            throw new RuntimeException("批量删除消息失败: API返回错误");
        } catch (Exception e) {
            throw new RuntimeException("批量删除消息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateAiReply(Long messageId) {
        // 模拟AI回复
        String[] aiReplies = {
            "感谢您的反馈，我们会认真处理您的问题。",
            "您好，我们已经收到您的消息，会尽快为您解决。",
            "非常抱歉给您带来不便，我们会立即跟进处理。",
            "感谢您选择我们的服务，有任何问题随时联系我们。",
            "您的建议很宝贵，我们会持续改进服务质量。"
        };
        Random random = new Random();
        return aiReplies[random.nextInt(aiReplies.length)];
    }

    @Override
    public Map<String, Object> getMessageStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalMessages", 1250);
        statistics.put("unreadMessages", 85);
        statistics.put("todayMessages", 45);
        statistics.put("replyRate", 92.5);
        
        // 消息类型统计
        Map<String, Integer> typeStats = new HashMap<>();
        typeStats.put("system", 320);
        typeStats.put("logistics", 450);
        typeStats.put("promotion", 280);
        typeStats.put("customer_service", 200);
        statistics.put("messageTypes", typeStats);
        
        // 每日消息趋势（最近7天）
        List<Map<String, Object>> dailyTrend = new ArrayList<>();
        String[] dates = {"2024-01-15", "2024-01-16", "2024-01-17", "2024-01-18", "2024-01-19", "2024-01-20", "2024-01-21"};
        int[] counts = {35, 42, 38, 51, 47, 39, 45};
        for (int i = 0; i < dates.length; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dates[i]);
            dayData.put("count", counts[i]);
            dailyTrend.add(dayData);
        }
        statistics.put("dailyTrend", dailyTrend);
        
        return statistics;
    }

    @Override
    public String exportMessages(String search, String status, String type) {
        // 模拟导出功能，返回下载链接
        return "/admin/downloads/messages_export_" + System.currentTimeMillis() + ".xlsx";
    }

    @Override
    public Boolean sendSystemMessage(Map<String, Object> messageData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(messageData, headers);
            
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/messages/send/system",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return true;
            }
            
            throw new RuntimeException("发送系统消息失败: API返回错误");
        } catch (Exception e) {
            throw new RuntimeException("发送系统消息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean sendBatchMessage(Map<String, Object> messageData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(messageData, headers);
            
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/messages/send/batch",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return true;
            }
            
            throw new RuntimeException("发送批量消息失败: API返回错误");
        } catch (Exception e) {
            throw new RuntimeException("发送批量消息失败: " + e.getMessage(), e);
        }
    }

    // 模拟数据方法
    private Map<String, Object> getMockMessageList(int page, int size, String search, String status, String type) {
        List<Message> messages = new ArrayList<>();
        
        // 模拟消息数据
        String[] usernames = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
        Integer[] messageTypes = {1, 2, 3}; // 1-物流通知，2-客服消息，3-系统通知
        String[] messageContents = {
            "您的订单已发货，请注意查收",
            "系统维护通知：今晚22:00-24:00进行系统维护",
            "新用户专享优惠券已到账，快来使用吧！",
            "您好，请问有什么可以帮助您的吗？",
            "您的包裹已到达配送站，预计今日送达",
            "限时特惠活动开始啦，全场8折优惠！"
        };
        String[] messageTitles = {
            "订单发货通知",
            "系统维护通知",
            "优惠券到账提醒",
            "客服消息",
            "包裹配送通知",
            "限时特惠活动"
        };
        
        Random random = new Random();
        int total = 1250;
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        
        for (int i = start; i < end; i++) {
            Message message = new Message();
            message.setId((long) (i + 1));
            message.setUserId((long) (random.nextInt(100) + 1));
            message.setMessageType(messageTypes[random.nextInt(messageTypes.length)]);
            message.setTitle(messageTitles[random.nextInt(messageTitles.length)]);
            message.setContent(messageContents[random.nextInt(messageContents.length)]);
            message.setStatus(random.nextInt(2)); // 0-未读，1-已读
            message.setCreateTime(new Date(System.currentTimeMillis() - random.nextInt(7 * 24 * 60 * 60 * 1000)));
            message.setUpdateTime(new Date());
            messages.add(message);
        }
        
        // 使用PageInfo结构来模拟分页数据
        Map<String, Object> result = new HashMap<>();
        result.put("list", messages);
        result.put("total", total);
        result.put("pageNum", page);
        result.put("pageSize", size);
        result.put("pages", (total + size - 1) / size);
        result.put("size", messages.size());
        
        return result;
    }
    
    private Message getMockMessage(Long id) {
        Message message = new Message();
        message.setId(id);
        message.setUserId(1L);
        message.setMessageType(3); // 3-系统通知
        message.setTitle("系统通知标题 " + id);
        message.setContent("这是系统通知内容 " + id);
        message.setStatus(0); // 0-未读
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());
        return message;
    }
}