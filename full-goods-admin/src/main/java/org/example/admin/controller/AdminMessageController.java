package org.example.admin.controller;

import com.github.pagehelper.PageInfo;
import org.example.admin.service.AdminMessageService;
import org.example.common.entity.Message;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理端消息控制器
 */
@RestController
@RequestMapping("/admin/message")
@CrossOrigin(origins = "*")
public class AdminMessageController {

    @Autowired
    private AdminMessageService adminMessageService;

    /**
     * 获取消息列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getMessageList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        try {
            Map<String, Object> result = adminMessageService.getMessageList(page, size, search, status, type);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed("获取消息列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取消息详情
     */
    @GetMapping("/{id}")
    public Result<Message> getMessageDetail(@PathVariable Long id) {
        try {
            Message message = adminMessageService.getMessageById(id);
            if (message != null) {
                return Result.success(message);
            } else {
                return Result.failed("消息不存在");
            }
        } catch (Exception e) {
            return Result.failed("获取消息详情失败: " + e.getMessage());
        }
    }

    /**
     * 回复消息
     */
    @PostMapping("/reply")
    public Result<Boolean> replyMessage(@RequestBody Map<String, Object> replyData) {
        try {
            Long messageId = Long.valueOf(replyData.get("messageId").toString());
            String replyContent = replyData.get("replyContent").toString();
            Boolean result = adminMessageService.replyMessage(messageId, replyContent);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed("回复消息失败: " + e.getMessage());
        }
    }

    /**
     * 标记消息已读/未读
     */
    @PutMapping("/read/{id}")
    public Result<Boolean> markMessageRead(@PathVariable Long id, @RequestParam Boolean isRead) {
        try {
            Boolean result = adminMessageService.markMessageRead(id, isRead);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed("标记消息状态失败: " + e.getMessage());
        }
    }

    /**
     * 批量标记消息已读/未读
     */
    @PutMapping("/batch-read")
    public Result<Boolean> batchMarkMessageRead(@RequestBody Map<String, Object> data) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> messageIds = (List<Long>) data.get("messageIds");
            Boolean isRead = (Boolean) data.get("isRead");
            Boolean result = adminMessageService.batchMarkMessageRead(messageIds, isRead);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed("批量标记消息状态失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除消息
     */
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteMessages(@RequestBody List<Long> messageIds) {
        try {
            Boolean result = adminMessageService.batchDeleteMessages(messageIds);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed("批量删除消息失败: " + e.getMessage());
        }
    }

    /**
     * AI自动回复
     */
    @PostMapping("/ai-reply")
    public Result<String> aiReply(@RequestBody Map<String, Object> data) {
        try {
            Long messageId = Long.valueOf(data.get("messageId").toString());
            String aiReply = adminMessageService.generateAiReply(messageId);
            return Result.success(aiReply);
        } catch (Exception e) {
            return Result.failed("AI回复生成失败: " + e.getMessage());
        }
    }

    /**
     * 获取消息统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getMessageStatistics() {
        try {
            Map<String, Object> statistics = adminMessageService.getMessageStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.failed("获取消息统计失败: " + e.getMessage());
        }
    }

    /**
     * 导出消息数据
     */
    @GetMapping("/export")
    public Result<String> exportMessages(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        try {
            String exportUrl = adminMessageService.exportMessages(search, status, type);
            return Result.success(exportUrl);
        } catch (Exception e) {
            return Result.failed("导出消息数据失败: " + e.getMessage());
        }
    }

    /**
     * 发送系统消息
     */
    @PostMapping("/send-system")
    public Result<Boolean> sendSystemMessage(@RequestBody Map<String, Object> messageData) {
        try {
            Boolean result = adminMessageService.sendSystemMessage(messageData);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed("发送系统消息失败: " + e.getMessage());
        }
    }

    /**
     * 发送批量消息
     */
    @PostMapping("/send-batch")
    public Result<Boolean> sendBatchMessage(@RequestBody Map<String, Object> messageData) {
        try {
            Boolean result = adminMessageService.sendBatchMessage(messageData);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed("发送批量消息失败: " + e.getMessage());
        }
    }
}