package org.example.admin.service;

import org.example.common.entity.MessageTemplate;
import org.example.common.response.Result;

import java.util.List;
import java.util.Map;

/**
 * 管理端消息模板服务接口
 */
public interface AdminMessageTemplateService {

    /**
     * 获取消息模板列表
     * @param page 页码
     * @param size 每页大小
     * @param templateName 模板名称
     * @param messageType 消息类型
     * @param status 状态
     * @return 模板列表
     */
    Result<Map<String, Object>> getTemplateList(Integer page, Integer size, String templateName, Integer messageType, Integer status);

    /**
     * 根据ID获取消息模板详情
     * @param templateId 模板ID
     * @return 模板详情
     */
    Result<MessageTemplate> getTemplateById(Long templateId);

    /**
     * 创建消息模板
     * @param template 模板对象
     * @return 创建结果
     */
    Result<Boolean> createTemplate(MessageTemplate template);

    /**
     * 更新消息模板
     * @param template 模板对象
     * @return 更新结果
     */
    Result<Boolean> updateTemplate(MessageTemplate template);

    /**
     * 删除消息模板
     * @param templateId 模板ID
     * @return 删除结果
     */
    Result<Boolean> deleteTemplate(Long templateId);

    /**
     * 批量删除消息模板
     * @param templateIds 模板ID列表
     * @return 删除结果
     */
    Result<Boolean> deleteTemplates(List<Long> templateIds);

    /**
     * 更新模板状态
     * @param templateId 模板ID
     * @param status 新状态
     * @return 更新结果
     */
    Result<Boolean> updateTemplateStatus(Long templateId, Integer status);

    /**
     * 批量更新模板状态
     * @param templateIds 模板ID列表
     * @param status 新状态
     * @return 更新结果
     */
    Result<Boolean> updateTemplatesStatus(List<Long> templateIds, Integer status);

    /**
     * 复制模板
     * @param templateId 原模板ID
     * @param newCode 新模板编码
     * @param newName 新模板名称
     * @return 复制结果
     */
    Result<Boolean> copyTemplate(Long templateId, String newCode, String newName);

    /**
     * 预览模板效果
     * @param templateCode 模板编码
     * @param params 模板参数
     * @return 预览结果
     */
    Result<Map<String, String>> previewTemplate(String templateCode, Map<String, Object> params);

    /**
     * 验证模板参数
     * @param templateCode 模板编码
     * @param params 模板参数
     * @return 验证结果
     */
    Result<Boolean> validateTemplateParams(String templateCode, Map<String, Object> params);

    /**
     * 获取模板统计数据
     * @return 统计数据
     */
    Result<Map<String, Object>> getTemplateStatistics();

    /**
     * 导出模板数据
     * @param templateName 模板名称
     * @param messageType 消息类型
     * @param status 状态
     * @return 导出结果
     */
    Result<String> exportTemplates(String templateName, Integer messageType, Integer status);

    /**
     * 获取启用状态的模板列表
     * @return 模板列表
     */
    Result<List<MessageTemplate>> getEnabledTemplates();
}