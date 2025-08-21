package org.example.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.api.mapper.MessageTemplateMapper;
import org.example.api.service.MessageTemplateService;
import org.example.common.entity.MessageTemplate;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息模板服务实现类
 */
@Slf4j
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    // 模板变量匹配正则表达式
    private static final Pattern TEMPLATE_PATTERN = Pattern.compile("\\{\\{(\\w+)\\}\\}");

    @Override
    public Result<Boolean> createTemplate(MessageTemplate template) {
        try {
            // 验证模板编码是否已存在
            MessageTemplate existingTemplate = messageTemplateMapper.selectByCode(template.getTemplateCode());
            if (existingTemplate != null) {
                return Result.failed("模板编码已存在");
            }

            template.setCreateTime(new Date());
            template.setUpdateTime(new Date());
            template.setStatus(1); // 默认启用

            int result = messageTemplateMapper.insert(template);
            if (result > 0) {
                log.info("创建消息模板成功，模板编码：{}", template.getTemplateCode());
                return Result.success(true);
            } else {
                return Result.failed("创建模板失败");
            }
        } catch (Exception e) {
            log.error("创建消息模板失败", e);
            return Result.failed("创建模板失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> updateTemplate(MessageTemplate template) {
        try {
            // 验证模板是否存在
            MessageTemplate existingTemplate = messageTemplateMapper.selectById(template.getId());
            if (existingTemplate == null) {
                return Result.failed("模板不存在");
            }

            // 如果修改了模板编码，需要验证新编码是否已存在
            if (!existingTemplate.getTemplateCode().equals(template.getTemplateCode())) {
                MessageTemplate codeTemplate = messageTemplateMapper.selectByCode(template.getTemplateCode());
                if (codeTemplate != null && !codeTemplate.getId().equals(template.getId())) {
                    return Result.failed("模板编码已存在");
                }
            }

            template.setUpdateTime(new Date());
            int result = messageTemplateMapper.update(template);
            if (result > 0) {
                log.info("更新消息模板成功，模板ID：{}", template.getId());
                return Result.success(true);
            } else {
                return Result.failed("更新模板失败");
            }
        } catch (Exception e) {
            log.error("更新消息模板失败", e);
            return Result.failed("更新模板失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> deleteTemplate(Long templateId) {
        try {
            MessageTemplate template = messageTemplateMapper.selectById(templateId);
            if (template == null) {
                return Result.failed("模板不存在");
            }

            int result = messageTemplateMapper.deleteById(templateId);
            if (result > 0) {
                log.info("删除消息模板成功，模板ID：{}", templateId);
                return Result.success(true);
            } else {
                return Result.failed("删除模板失败");
            }
        } catch (Exception e) {
            log.error("删除消息模板失败", e);
            return Result.failed("删除模板失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> deleteTemplates(List<Long> templateIds) {
        try {
            if (templateIds == null || templateIds.isEmpty()) {
                return Result.failed("模板ID列表不能为空");
            }

            int result = messageTemplateMapper.batchDelete(templateIds);
            if (result > 0) {
                log.info("批量删除消息模板成功，删除数量：{}", result);
                return Result.success(true);
            } else {
                return Result.failed("批量删除模板失败");
            }
        } catch (Exception e) {
            log.error("批量删除消息模板失败", e);
            return Result.failed("批量删除模板失败：" + e.getMessage());
        }
    }

    @Override
    public Result<MessageTemplate> getTemplateById(Long templateId) {
        try {
            MessageTemplate template = messageTemplateMapper.selectById(templateId);
            if (template != null) {
                return Result.success(template);
            } else {
                return Result.failed("模板不存在");
            }
        } catch (Exception e) {
            log.error("获取消息模板失败", e);
            return Result.failed("获取模板失败：" + e.getMessage());
        }
    }

    @Override
    public Result<MessageTemplate> getTemplateByCode(String templateCode) {
        try {
            if (!StringUtils.hasText(templateCode)) {
                return Result.failed("模板编码不能为空");
            }

            MessageTemplate template = messageTemplateMapper.selectByCode(templateCode);
            if (template != null) {
                return Result.success(template);
            } else {
                return Result.failed("模板不存在");
            }
        } catch (Exception e) {
            log.error("根据编码获取消息模板失败", e);
            return Result.failed("获取模板失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<MessageTemplate>> getTemplatesByType(String messageType) {
        try {
            List<MessageTemplate> templates = messageTemplateMapper.selectByMessageType(Integer.parseInt(messageType));
            return Result.success(templates);
        } catch (Exception e) {
            log.error("根据类型获取消息模板失败", e);
            return Result.failed("获取模板失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<MessageTemplate>> getEnabledTemplates() {
        try {
            List<MessageTemplate> templates = messageTemplateMapper.selectEnabled();
            return Result.success(templates);
        } catch (Exception e) {
            log.error("获取启用的消息模板失败", e);
            return Result.failed("获取模板失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<MessageTemplate>> getEnabledTemplatesByType(String messageType) {
        try {
            List<MessageTemplate> allTemplates = messageTemplateMapper.selectByMessageType(Integer.parseInt(messageType));
            List<MessageTemplate> templates = allTemplates.stream()
                .filter(t -> t.getStatus() == 1)
                .collect(java.util.stream.Collectors.toList());
            return Result.success(templates);
        } catch (Exception e) {
            log.error("根据类型获取启用的消息模板失败", e);
            return Result.failed("获取模板失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PageInfo<MessageTemplate>> getTemplateList(Integer page, Integer size, String templateName, Integer messageType, Integer status) {
        try {
            PageHelper.startPage(page != null ? page : 1, size != null ? size : 10);
            
            List<MessageTemplate> templates;
            
            // 如果有查询条件，使用条件查询（不带分页的方法）
            if (messageType != null || status != null) {
                // 使用不带分页的条件查询方法，让PageHelper处理分页
                templates = messageTemplateMapper.selectByCondition(messageType, status);
            } else {
                templates = messageTemplateMapper.selectAll();
            }
            
            // 如果有模板名称搜索，进行内存过滤
            if (StringUtils.hasText(templateName)) {
                templates = templates.stream()
                    .filter(template -> 
                        (template.getTemplateName() != null && template.getTemplateName().contains(templateName)) ||
                        (template.getTemplateCode() != null && template.getTemplateCode().contains(templateName))
                    )
                    .collect(java.util.stream.Collectors.toList());
            }
            
            PageInfo<MessageTemplate> pageInfo = new PageInfo<>(templates);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("分页获取消息模板失败", e);
            return Result.failed("获取模板列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> updateTemplateStatus(Long templateId, Integer status) {
        try {
            MessageTemplate template = messageTemplateMapper.selectById(templateId);
            if (template == null) {
                return Result.failed("模板不存在");
            }

            int result = messageTemplateMapper.updateStatus(templateId, status);
            if (result > 0) {
                log.info("更新模板状态成功，模板ID：{}，状态：{}", templateId, status);
                return Result.success(true);
            } else {
                return Result.failed("更新模板状态失败");
            }
        } catch (Exception e) {
            log.error("更新模板状态失败", e);
            return Result.failed("更新状态失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> updateTemplatesStatus(List<Long> templateIds, Integer status) {
        try {
            if (templateIds == null || templateIds.isEmpty()) {
                return Result.failed("模板ID列表不能为空");
            }

            int result = 0;
            for (Long templateId : templateIds) {
                result += messageTemplateMapper.updateStatus(templateId, status);
            }
            if (result > 0) {
                log.info("批量更新模板状态成功，更新数量：{}", result);
                return Result.success(true);
            } else {
                return Result.failed("批量更新模板状态失败");
            }
        } catch (Exception e) {
            log.error("批量更新模板状态失败", e);
            return Result.failed("批量更新模板状态失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, String>> renderTemplate(String templateCode, Map<String, Object> params) {
        try {
            MessageTemplate template = messageTemplateMapper.selectByCode(templateCode);
            if (template == null) {
                return Result.failed("模板不存在");
            }

            if (template.getStatus() != 1) {
                return Result.failed("模板已禁用");
            }

            // 渲染标题和内容
            String renderedTitle = renderTemplateContent(template.getTitle(), params);
            String renderedContent = renderTemplateContent(template.getContent(), params);

            Map<String, String> result = new HashMap<>();
            result.put("title", renderedTitle);
            result.put("content", renderedContent);
            result.put("iconUrl", template.getIconUrl());
            result.put("linkTemplate", renderTemplateContent(template.getLinkTemplate(), params));

            return Result.success(result);
        } catch (Exception e) {
            log.error("渲染模板失败", e);
            return Result.failed("渲染模板失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> validateTemplateParams(String templateCode, Map<String, Object> params) {
        try {
            MessageTemplate template = messageTemplateMapper.selectByCode(templateCode);
            if (template == null) {
                return Result.failed("模板不存在");
            }

            // 提取模板中的所有变量
            Set<String> requiredParams = extractTemplateVariables(template.getTitle() + " " + template.getContent());

            // 检查是否提供了所有必需的参数
            for (String param : requiredParams) {
                if (params == null || !params.containsKey(param) || params.get(param) == null) {
                    return Result.failed("缺少必需参数：" + param);
                }
            }

            return Result.success(true);
        } catch (Exception e) {
            log.error("验证模板参数失败", e);
            return Result.failed("验证参数失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getTemplateStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            List<MessageTemplate> allTemplates = messageTemplateMapper.selectAll();
            statistics.put("totalCount", allTemplates.size());
            
            // 启用模板数
            long enabledCount = allTemplates.stream().filter(t -> t.getStatus() == 1).count();
            statistics.put("enabledCount", enabledCount);
            
            // 禁用模板数
            long disabledCount = allTemplates.stream().filter(t -> t.getStatus() == 0).count();
            statistics.put("disabledCount", disabledCount);
            
            // 按类型统计
            Map<Integer, Long> typeCount = allTemplates.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    MessageTemplate::getMessageType, 
                    java.util.stream.Collectors.counting()));
            statistics.put("typeStatistics", typeCount);

            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取模板统计信息失败", e);
            return Result.failed("获取统计信息失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> copyTemplate(Long templateId, String newCode, String newName) {
        try {
            MessageTemplate originalTemplate = messageTemplateMapper.selectById(templateId);
            if (originalTemplate == null) {
                return Result.failed("原模板不存在");
            }

            // 检查新编码是否已存在
            MessageTemplate existingTemplate = messageTemplateMapper.selectByCode(newCode);
            if (existingTemplate != null) {
                return Result.failed("新模板编码已存在");
            }

            // 创建新模板
            MessageTemplate newTemplate = new MessageTemplate();
            newTemplate.setTemplateCode(newCode);
            newTemplate.setTemplateName(newName);
            newTemplate.setMessageType(originalTemplate.getMessageType());
            newTemplate.setTitle(originalTemplate.getTitle());
            newTemplate.setContent(originalTemplate.getContent());
            newTemplate.setIconUrl(originalTemplate.getIconUrl());
            newTemplate.setLinkTemplate(originalTemplate.getLinkTemplate());
            newTemplate.setStatus(0); // 默认禁用
            newTemplate.setDescription("复制自：" + originalTemplate.getTemplateName());
            newTemplate.setParamDescription(originalTemplate.getParamDescription());
            newTemplate.setCreateTime(new Date());
            newTemplate.setUpdateTime(new Date());

            int result = messageTemplateMapper.insert(newTemplate);
            if (result > 0) {
                log.info("复制模板成功，原模板ID：{}，新模板编码：{}", templateId, newCode);
                return Result.success(true);
            } else {
                return Result.failed("复制模板失败");
            }
        } catch (Exception e) {
            log.error("复制模板失败", e);
            return Result.failed("复制模板失败：" + e.getMessage());
        }
    }

    /**
     * 渲染模板内容
     *
     * @param template 模板字符串
     * @param params   参数映射
     * @return 渲染后的内容
     */
    private String renderTemplateContent(String template, Map<String, Object> params) {
        if (!StringUtils.hasText(template) || params == null) {
            return template;
        }

        String result = template;
        Matcher matcher = TEMPLATE_PATTERN.matcher(template);
        while (matcher.find()) {
            String variable = matcher.group(1);
            Object value = params.get(variable);
            if (value != null) {
                result = result.replace("{{" + variable + "}}", value.toString());
            }
        }
        return result;
    }

    /**
     * 提取模板中的变量
     *
     * @param template 模板字符串
     * @return 变量集合
     */
    private Set<String> extractTemplateVariables(String template) {
        Set<String> variables = new HashSet<>();
        if (StringUtils.hasText(template)) {
            Matcher matcher = TEMPLATE_PATTERN.matcher(template);
            while (matcher.find()) {
                variables.add(matcher.group(1));
            }
        }
        return variables;
    }
}