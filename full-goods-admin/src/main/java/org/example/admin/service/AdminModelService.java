package org.example.admin.service;

import org.example.common.response.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * AI模型管理服务接口
 */
public interface AdminModelService {
    
    /**
     * 获取模型列表
     */
    Result<Map<String, Object>> getModelList(Integer page, Integer size, String search, String status);
    
    /**
     * 获取模型详情
     */
    Result<Map<String, Object>> getModelDetail(Long id);
    
    /**
     * 创建模型
     */
    Result<Boolean> createModel(Map<String, Object> modelData);
    
    /**
     * 上传模型
     */
    Result<Boolean> uploadModel(MultipartFile file, String name, String version, String description);
    
    /**
     * 测试模型
     */
    Result<Map<String, Object>> testModel(Long id);
    
    /**
     * 更新模型
     */
    Result<Boolean> updateModel(Long id, Map<String, Object> modelData);
    
    /**
     * 激活模型
     */
    Result<Boolean> activateModel(Long id);
    
    /**
     * 停用模型
     */
    Result<Boolean> deactivateModel(Long id);
    
    /**
     * 删除模型
     */
    Result<Boolean> deleteModel(Long id);
    
    /**
     * 上传训练数据
     */
    Result<Boolean> uploadTrainingData(MultipartFile file, Long modelId, String name);
    
    /**
     * 创建推荐策略
     */
    Result<Boolean> createStrategy(Map<String, Object> strategyData);
    
    /**
     * 获取模型统计信息
     */
    Result<Map<String, Object>> getModelStatistics();
}