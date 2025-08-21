package org.example.api.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.example.api.service.MessageTemplateService;
import org.example.common.entity.MessageTemplate;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息模板控制器
 */
@Slf4j
@RestController
@RequestMapping("/message-templates")
@Api(tags = "消息模板管理")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    /**
     * 创建消息模板
     */
    @PostMapping
    @ApiOperation("创建消息模板")
    public Result<Boolean> createTemplate(@Valid @RequestBody MessageTemplate template) {
        log.info("创建消息模板请求：{}", template);
        return messageTemplateService.createTemplate(template);
    }

    /**
     * 更新消息模板
     */
    @PutMapping("/{id}")
    @ApiOperation("更新消息模板")
    public Result<Boolean> updateTemplate(
            @ApiParam("模板ID") @PathVariable Long id,
            @Valid @RequestBody MessageTemplate template) {
        log.info("更新消息模板请求，ID：{}，模板：{}", id, template);
        template.setId(id);
        return messageTemplateService.updateTemplate(template);
    }

    /**
     * 删除消息模板
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除消息模板")
    public Result<Boolean> deleteTemplate(@ApiParam("模板ID") @PathVariable Long id) {
        log.info("删除消息模板请求，ID：{}", id);
        return messageTemplateService.deleteTemplate(id);
    }

    /**
     * 批量删除消息模板
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除消息模板")
    public Result<Boolean> deleteTemplates(@RequestBody List<Long> templateIds) {
        log.info("批量删除消息模板请求，IDs：{}", templateIds);
        return messageTemplateService.deleteTemplates(templateIds);
    }

    /**
     * 根据ID获取模板
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取模板")
    public Result<MessageTemplate> getTemplateById(@ApiParam("模板ID") @PathVariable Long id) {
        log.info("根据ID获取模板请求，ID：{}", id);
        return messageTemplateService.getTemplateById(id);
    }

    /**
     * 根据编码获取模板
     */
    @GetMapping("/code/{code}")
    @ApiOperation("根据编码获取模板")
    public Result<MessageTemplate> getTemplateByCode(@ApiParam("模板编码") @PathVariable String code) {
        log.info("根据编码获取模板请求，编码：{}", code);
        return messageTemplateService.getTemplateByCode(code);
    }

    /**
     * 根据消息类型获取模板列表
     */
    @GetMapping("/type/{messageType}")
    @ApiOperation("根据消息类型获取模板列表")
    public Result<List<MessageTemplate>> getTemplatesByType(
            @ApiParam("消息类型") @PathVariable String messageType) {
        log.info("根据消息类型获取模板列表请求，类型：{}", messageType);
        return messageTemplateService.getTemplatesByType(messageType);
    }

    /**
     * 获取启用状态的模板列表
     */
    @GetMapping("/enabled")
    @ApiOperation("获取启用状态的模板列表")
    public Result<List<MessageTemplate>> getEnabledTemplates() {
        log.info("获取启用状态的模板列表请求");
        return messageTemplateService.getEnabledTemplates();
    }

    /**
     * 分页查询模板列表
     */
    @GetMapping
    @ApiOperation("分页查询模板列表")
    public Result<PageInfo<MessageTemplate>> getTemplateList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("模板名称") @RequestParam(required = false) String templateName,
            @ApiParam("消息类型") @RequestParam(required = false) Integer messageType,
            @ApiParam("状态") @RequestParam(required = false) Integer status) {
        log.info("分页查询模板列表请求，页码：{}，大小：{}，模板名称：{}，类型：{}，状态：{}", 
                page, size, templateName, messageType, status);
        return messageTemplateService.getTemplateList(page, size, templateName, messageType, status);
    }

    /**
     * 更新模板状态
     */
    @PutMapping("/{id}/status")
    @ApiOperation("更新模板状态")
    public Result<Boolean> updateTemplateStatus(
            @ApiParam("模板ID") @PathVariable Long id,
            @ApiParam("新状态") @RequestParam Integer status) {
        log.info("更新模板状态请求，ID：{}，状态：{}", id, status);
        return messageTemplateService.updateTemplateStatus(id, status);
    }

    /**
     * 批量更新模板状态
     */
    @PutMapping("/batch/status")
    @ApiOperation("批量更新模板状态")
    public Result<Boolean> updateTemplatesStatus(
            @RequestBody List<Long> templateIds,
            @ApiParam("新状态") @RequestParam Integer status) {
        log.info("批量更新模板状态请求，IDs：{}，状态：{}", templateIds, status);
        return messageTemplateService.updateTemplatesStatus(templateIds, status);
    }

    /**
     * 根据模板渲染消息内容
     */
    @PostMapping("/render/{code}")
    @ApiOperation("根据模板渲染消息内容")
    public Result<Map<String, String>> renderTemplate(
            @ApiParam("模板编码") @PathVariable String code,
            @RequestBody Map<String, Object> params) {
        log.info("渲染模板请求，编码：{}，参数：{}", code, params);
        return messageTemplateService.renderTemplate(code, params);
    }

    /**
     * 验证模板参数
     */
    @PostMapping("/validate/{code}")
    @ApiOperation("验证模板参数")
    public Result<Boolean> validateTemplateParams(
            @ApiParam("模板编码") @PathVariable String code,
            @RequestBody Map<String, Object> params) {
        log.info("验证模板参数请求，编码：{}，参数：{}", code, params);
        return messageTemplateService.validateTemplateParams(code, params);
    }

    /**
     * 获取模板统计信息
     */
    @GetMapping("/statistics")
    @ApiOperation("获取模板统计信息")
    public Result<Map<String, Object>> getTemplateStatistics() {
        log.info("获取模板统计信息请求");
        return messageTemplateService.getTemplateStatistics();
    }

    /**
     * 复制模板
     */
    @PostMapping("/{id}/copy")
    @ApiOperation("复制模板")
    public Result<Boolean> copyTemplate(
            @ApiParam("原模板ID") @PathVariable Long id,
            @ApiParam("新模板编码") @RequestParam String newCode,
            @ApiParam("新模板名称") @RequestParam String newName) {
        log.info("复制模板请求，原ID：{}，新编码：{}，新名称：{}", id, newCode, newName);
        return messageTemplateService.copyTemplate(id, newCode, newName);
    }

    /**
     * 预览模板效果
     */
    @PostMapping("/preview/{code}")
    @ApiOperation("预览模板效果")
    public Result<Map<String, String>> previewTemplate(
            @ApiParam("模板编码") @PathVariable String code,
            @RequestBody(required = false) Map<String, Object> params) {
        log.info("预览模板效果请求，编码：{}，参数：{}", code, params);
        Map<String, Object> renderParams = params != null ? params : new HashMap<>();
        return messageTemplateService.renderTemplate(code, renderParams);
    }

    /**
     * 获取模板参数说明
     */
    @GetMapping("/{code}/params")
    @ApiOperation("获取模板参数说明")
    public Result<String> getTemplateParamDescription(@ApiParam("模板编码") @PathVariable String code) {
        log.info("获取模板参数说明请求，编码：{}", code);
        Result<MessageTemplate> templateResult = messageTemplateService.getTemplateByCode(code);
        if (templateResult.getCode() == 200 && templateResult.getData() != null) {
            return Result.success(templateResult.getData().getParamDescription());
        }
        return Result.failed("模板不存在或获取失败");
    }
}