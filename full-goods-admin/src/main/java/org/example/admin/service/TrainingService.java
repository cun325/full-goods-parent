package org.example.admin.service;

import org.example.common.response.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * NLP模型训练服务接口
 */
public interface TrainingService {
    
    /**
     * 获取训练任务列表
     */
    Result<Map<String, Object>> getTrainingTasks(Integer page, Integer size, String status, String modelType);
    
    /**
     * 创建训练任务
     */
    Result<Boolean> createTrainingTask(Map<String, Object> taskData);
    
    /**
     * 获取训练任务详情
     */
    Result<Map<String, Object>> getTrainingTaskDetail(Long id);
    
    /**
     * 启动训练任务
     */
    Result<Boolean> startTraining(Long id);
    
    /**
     * 停止训练任务
     */
    Result<Boolean> stopTraining(Long id);
    
    /**
     * 获取训练进度
     */
    Result<Map<String, Object>> getTrainingProgress(Long id);
    
    /**
     * 上传训练数据集
     */
    Result<Boolean> uploadDataset(MultipartFile file, String name, String description, String dataType);
    
    /**
     * 获取数据集列表
     */
    Result<Map<String, Object>> getDatasets(Integer page, Integer size, String dataType);
    
    /**
     * 数据预处理
     */
    Result<Boolean> preprocessData(Long id, Map<String, Object> preprocessConfig);
    
    /**
     * 获取训练日志
     */
    Result<Map<String, Object>> getTrainingLogs(Long id, Integer page, Integer size);
    
    /**
     * 模型评估
     */
    Result<Map<String, Object>> evaluateModel(Long id, Map<String, Object> evaluationConfig);
    
    /**
     * 获取训练统计信息
     */
    Result<Map<String, Object>> getTrainingStatistics();
    
    /**
     * 导出训练模型
     */
    Result<Boolean> exportModel(Long id, Map<String, Object> exportConfig);
    
    /**
     * 获取支持的模型类型
     */
    Result<Map<String, Object>> getSupportedModelTypes();
    
    /**
     * 获取预处理选项
     */
    Result<Map<String, Object>> getPreprocessOptions();
    
    /**
     * 评估模型
     */
    Result<Map<String, Object>> evaluateModel(Long taskId, Long testDatasetId);
    
    /**
     * 部署模型
     */
    Result<Map<String, Object>> deployModel(Long taskId, String deploymentName, String deploymentType);
    
    /**
     * 导出模型
     */
    Result<Map<String, Object>> exportModel(Long taskId, String format);
    
    /**
     * 使用模型进行预测
     */
    Result<Map<String, Object>> predictWithModel(Long taskId, Map<String, Object> input);
}