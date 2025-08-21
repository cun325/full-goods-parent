package org.example.admin.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.admin.service.AdminMessageTemplateService;
import org.example.common.entity.MessageTemplate;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 管理端消息模板控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/message-templates")

public class AdminMessageTemplateController {

    @Autowired
    private AdminMessageTemplateService adminMessageTemplateService;

    /**
     * 获取消息模板列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getTemplateList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String templateName,
            @RequestParam(required = false) Integer messageType,
            @RequestParam(required = false) Integer status) {
        log.info("获取消息模板列表请求，页码：{}，大小：{}，模板名称：{}，消息类型：{}，状态：{}", 
                page, size, templateName, messageType, status);
        return adminMessageTemplateService.getTemplateList(page, size, templateName, messageType, status);
    }

    /**
     * 根据ID获取消息模板详情
     */
    @GetMapping("/{id}")
    public Result<MessageTemplate> getTemplateById(@PathVariable Long id) {
        log.info("获取消息模板详情请求，ID：{}", id);
        return adminMessageTemplateService.getTemplateById(id);
    }

    /**
     * 创建消息模板
     */
    @PostMapping
    public Result<Boolean> createTemplate(@Valid @RequestBody MessageTemplate template) {
        log.info("创建消息模板请求：{}", template);
        return adminMessageTemplateService.createTemplate(template);
    }

    /**
     * 更新消息模板
     */
    @PutMapping("/{id}")
    public Result<Boolean> updateTemplate(
            @PathVariable Long id,
            @Valid @RequestBody MessageTemplate template) {
        log.info("更新消息模板请求，ID：{}，模板：{}", id, template);
        template.setId(id);
        return adminMessageTemplateService.updateTemplate(template);
    }

    /**
     * 删除消息模板
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTemplate(@PathVariable Long id) {
        log.info("删除消息模板请求，ID：{}", id);
        return adminMessageTemplateService.deleteTemplate(id);
    }

    /**
     * 批量删除消息模板
     */
    @DeleteMapping("/batch")

    public Result<Boolean> deleteTemplates(@RequestBody List<Long> templateIds) {
        log.info("批量删除消息模板请求，IDs：{}", templateIds);
        return adminMessageTemplateService.deleteTemplates(templateIds);
    }

    /**
     * 更新模板状态
     */
    @PutMapping("/{id}/status")
    public Result<Boolean> updateTemplateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        log.info("更新模板状态请求，ID：{}，状态：{}", id, status);
        return adminMessageTemplateService.updateTemplateStatus(id, status);
    }

    /**
     * 批量更新模板状态
     */
    @PutMapping("/batch/status")
    public Result<Boolean> updateTemplatesStatus(
            @RequestBody List<Long> templateIds,
            @RequestParam Integer status) {
        log.info("批量更新模板状态请求，IDs：{}，状态：{}", templateIds, status);
        return adminMessageTemplateService.updateTemplatesStatus(templateIds, status);
    }

    /**
     * 复制模板
     */
    @PostMapping("/{id}/copy")
    public Result<Boolean> copyTemplate(
            @PathVariable Long id,
            @RequestParam String newCode,
            @RequestParam String newName) {
        log.info("复制模板请求，原ID：{}，新编码：{}，新名称：{}", id, newCode, newName);
        return adminMessageTemplateService.copyTemplate(id, newCode, newName);
    }

    /**
     * 预览模板效果
     */
    @PostMapping("/preview/{code}")
    public Result<Map<String, String>> previewTemplate(
            @PathVariable String code,
            @RequestBody(required = false) Map<String, Object> params) {
        log.info("预览模板效果请求，编码：{}，参数：{}", code, params);
        return adminMessageTemplateService.previewTemplate(code, params);
    }

    /**
     * 验证模板参数
     */
    @PostMapping("/validate/{code}")
    public Result<Boolean> validateTemplateParams(
            @PathVariable String code,
            @RequestBody Map<String, Object> params) {
        log.info("验证模板参数请求，编码：{}，参数：{}", code, params);
        return adminMessageTemplateService.validateTemplateParams(code, params);
    }

    /**
     * 获取模板统计数据
     */
    @GetMapping("/statistics")

    public Result<Map<String, Object>> getTemplateStatistics() {
        log.info("获取模板统计数据请求");
        return adminMessageTemplateService.getTemplateStatistics();
    }

    /**
     * 导出模板数据
     */
    @GetMapping("/export")
    public Result<String> exportTemplates(
            @RequestParam(required = false) String templateName,
            @RequestParam(required = false) Integer messageType,
            @RequestParam(required = false) Integer status) {
        log.info("导出模板数据请求，模板名称：{}，消息类型：{}，状态：{}", templateName, messageType, status);
        return adminMessageTemplateService.exportTemplates(templateName, messageType, status);
    }

    /**
     * 获取启用状态的模板列表
     */
    @GetMapping("/enabled")

    public Result<List<MessageTemplate>> getEnabledTemplates() {
        log.info("获取启用状态的模板列表请求");
        return adminMessageTemplateService.getEnabledTemplates();
    }
}