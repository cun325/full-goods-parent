package org.example.api.service;

import com.github.pagehelper.PageInfo;
import org.example.common.entity.MessageTemplate;
import org.example.common.response.Result;

import java.util.List;
import java.util.Map;

/**
 * 消息模板服务接口
 */
public interface MessageTemplateService {

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
     * 根据ID获取模板
     * @param templateId 模板ID
     * @return 模板信息
     */
    Result<MessageTemplate> getTemplateById(Long templateId);

    /**
     * 根据编码获取模板
     * @param templateCode 模板编码
     * @return 模板信息
     */
    Result<MessageTemplate> getTemplateByCode(String templateCode);

    /**
     * 根据消息类型获取模板列表
     * @param messageType 消息类型
     * @return 模板列表
     */
    Result<List<MessageTemplate>> getTemplatesByType(String messageType);

    /**
     * 获取启用状态的模板列表
     * @return 模板列表
     */
    Result<List<MessageTemplate>> getEnabledTemplates();

    /**
     * 根据消息类型获取启用状态的模板列表
     * @param messageType 消息类型
     * @return 模板列表
     */
    Result<List<MessageTemplate>> getEnabledTemplatesByType(String messageType);

    /**
     * 分页查询模板列表
     * @param page 页码
     * @param size 每页大小
     * @param templateName 模板名称
     * @param messageType 消息类型
     * @param status 状态
     * @return 模板列表
     */
    Result<PageInfo<MessageTemplate>> getTemplateList(Integer page, Integer size, String templateName, Integer messageType, Integer status);

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
     * 根据模板渲染消息内容
     * @param templateCode 模板编码
     * @param params 参数映射
     * @return 渲染后的消息内容
     */
    Result<Map<String, String>> renderTemplate(String templateCode, Map<String, Object> params);

    /**
     * 验证模板参数
     * @param templateCode 模板编码
     * @param params 参数映射
     * @return 验证结果
     */
    Result<Boolean> validateTemplateParams(String templateCode, Map<String, Object> params);

    /**
     * 获取模板统计信息
     * @return 统计信息
     */
    Result<Map<String, Object>> getTemplateStatistics();

    /**
     * 复制模板
     * @param templateId 源模板ID
     * @param newCode 新模板编码
     * @param newName 新模板名称
     * @return 复制结果
     */
    Result<Boolean> copyTemplate(Long templateId, String newCode, String newName);
}