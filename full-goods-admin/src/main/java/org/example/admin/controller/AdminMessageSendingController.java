package org.example.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.admin.service.AdminMessageSendingService;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 管理端消息发送控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/message")
@CrossOrigin(origins = "*")
public class AdminMessageSendingController {

    @Autowired
    private AdminMessageSendingService adminMessageSendingService;

    /**
     * 发送系统消息
     */
    @PostMapping("/send/system")
    public Result<Boolean> sendSystemMessage(@RequestBody Map<String, Object> messageData) {
        log.info("发送系统消息请求：{}", messageData);
        return adminMessageSendingService.sendSystemMessage(messageData);
    }

    /**
     * 发送模板消息
     */
    @PostMapping("/send/template")
    public Result<Boolean> sendTemplateMessage(@RequestBody Map<String, Object> messageData) {
        log.info("发送模板消息请求：{}", messageData);
        return adminMessageSendingService.sendTemplateMessage(messageData);
    }

    /**
     * 发送批量消息
     */
    @PostMapping("/send/batch")
    public Result<Boolean> sendBatchMessage(@RequestBody Map<String, Object> messageData) {
        log.info("发送批量消息请求：{}", messageData);
        return adminMessageSendingService.sendBatchMessage(messageData);
    }

    /**
     * 获取发送历史
     */
    @GetMapping("/sending/history")
    public Result<Map<String, Object>> getSendingHistory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sendType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        log.info("获取发送历史请求，页码：{}，大小：{}，类型：{}，状态：{}", page, size, sendType, status);
        return adminMessageSendingService.getSendingHistory(page, size, sendType, status, startTime, endTime);
    }

    /**
     * 获取发送详情
     */
    @GetMapping("/sending/{id}")
    public Result<Map<String, Object>> getSendingDetail(@PathVariable Long id) {
        log.info("获取发送详情请求，ID：{}", id);
        return adminMessageSendingService.getSendingDetail(id);
    }

    /**
     * 取消发送
     */
    @PostMapping("/sending/{id}/cancel")
    public Result<Boolean> cancelSending(@PathVariable Long id) {
        log.info("取消发送请求，ID：{}", id);
        return adminMessageSendingService.cancelSending(id);
    }

    /**
     * 重新发送
     */
    @PostMapping("/sending/{id}/resend")
    public Result<Boolean> resendMessage(@PathVariable Long id) {
        log.info("重新发送请求，ID：{}", id);
        return adminMessageSendingService.resendMessage(id);
    }

    /**
     * 获取发送统计
     */
    @GetMapping("/sending/statistics")
    public Result<Map<String, Object>> getSendingStatistics(
            @RequestParam(defaultValue = "today") String type) {
        log.info("获取发送统计请求，类型：{}", type);
        return adminMessageSendingService.getSendingStatistics(type);
    }

    /**
     * 预览模板消息
     */
    @PostMapping("/template/preview")
    public Result<Map<String, String>> previewTemplateMessage(@RequestBody Map<String, Object> previewData) {
        log.info("预览模板消息请求：{}", previewData);
        return adminMessageSendingService.previewTemplateMessage(previewData);
    }

    /**
     * 验证用户列表文件
     */
    @PostMapping("/validate/userlist")
    public Result<Map<String, Object>> validateUserListFile(@RequestParam("file") MultipartFile file) {
        log.info("验证用户列表文件请求，文件名：{}", file.getOriginalFilename());
        return adminMessageSendingService.validateUserListFile(file);
    }

    /**
     * 获取用户组列表
     */
    @GetMapping("/usergroups")
    public Result<List<Map<String, Object>>> getUserGroups() {
        log.info("获取用户组列表请求");
        return adminMessageSendingService.getUserGroups();
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/users")
    public Result<List<Map<String, Object>>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long groupId) {
        log.info("获取用户列表请求，搜索：{}，组ID：{}", search, groupId);
        return adminMessageSendingService.getUsers(search, groupId);
    }

    /**
     * 保存草稿
     */
    @PostMapping("/draft")
    public Result<Long> saveDraft(@RequestBody Map<String, Object> draftData) {
        log.info("保存草稿请求：{}", draftData);
        return adminMessageSendingService.saveDraft(draftData);
    }

    /**
     * 获取草稿列表
     */
    @GetMapping("/drafts")
    public Result<List<Map<String, Object>>> getDrafts() {
        log.info("获取草稿列表请求");
        return adminMessageSendingService.getDrafts();
    }

    /**
     * 删除草稿
     */
    @DeleteMapping("/draft/{id}")
    public Result<Boolean> deleteDraft(@PathVariable Long id) {
        log.info("删除草稿请求，ID：{}", id);
        return adminMessageSendingService.deleteDraft(id);
    }

    /**
     * 从草稿创建消息
     */
    @PostMapping("/draft/{id}/create")
    public Result<Boolean> createFromDraft(@PathVariable Long id) {
        log.info("从草稿创建消息请求，ID：{}", id);
        return adminMessageSendingService.createFromDraft(id);
    }

    /**
     * 获取发送配置
     */
    @GetMapping("/sending/config")
    public Result<Map<String, Object>> getSendingConfig() {
        log.info("获取发送配置请求");
        return adminMessageSendingService.getSendingConfig();
    }

    /**
     * 更新发送配置
     */
    @PutMapping("/sending/config")
    public Result<Boolean> updateSendingConfig(@RequestBody Map<String, Object> configData) {
        log.info("更新发送配置请求：{}", configData);
        return adminMessageSendingService.updateSendingConfig(configData);
    }

    /**
     * 测试消息发送
     */
    @PostMapping("/test")
    public Result<Boolean> testMessageSending(@RequestBody Map<String, Object> testData) {
        log.info("测试消息发送请求：{}", testData);
        return adminMessageSendingService.testMessageSending(testData);
    }

    /**
     * 获取消息队列状态
     */
    @GetMapping("/queue/status")
    public Result<Map<String, Object>> getQueueStatus() {
        log.info("获取消息队列状态请求");
        return adminMessageSendingService.getQueueStatus();
    }

    /**
     * 清空消息队列
     */
    @PostMapping("/queue/clear")
    public Result<Boolean> clearQueue() {
        log.info("清空消息队列请求");
        return adminMessageSendingService.clearQueue();
    }

    /**
     * 暂停消息发送
     */
    @PostMapping("/sending/pause")
    public Result<Boolean> pauseSending() {
        log.info("暂停消息发送请求");
        return adminMessageSendingService.pauseSending();
    }

    /**
     * 恢复消息发送
     */
    @PostMapping("/sending/resume")
    public Result<Boolean> resumeSending() {
        log.info("恢复消息发送请求");
        return adminMessageSendingService.resumeSending();
    }

    /**
     * 导出发送历史
     */
    @GetMapping("/sending/export")
    public Result<String> exportSendingHistory(
            @RequestParam(required = false) String sendType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        log.info("导出发送历史请求，类型：{}，状态：{}", sendType, status);
        return adminMessageSendingService.exportSendingHistory(sendType, status, startTime, endTime);
    }

    /**
     * 获取消息发送报告
     */
    @GetMapping("/sending/report")
    public Result<Map<String, Object>> getSendingReport(
            @RequestParam(defaultValue = "daily") String reportType,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        log.info("获取消息发送报告请求，类型：{}", reportType);
        return adminMessageSendingService.getSendingReport(reportType, startTime, endTime);
    }
}