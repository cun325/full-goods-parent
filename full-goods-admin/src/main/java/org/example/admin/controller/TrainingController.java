package org.example.admin.controller;

import org.example.admin.service.TrainingService;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * NLP模型训练控制器
 * 提供模型训练、数据预处理、训练监控等功能
 */
@RestController
@RequestMapping("/admin/training")
@CrossOrigin(origins = "*")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    /**
     * 获取训练任务列表
     */
    @GetMapping("/tasks")
    public Result<Map<String, Object>> getTrainingTasks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String modelType) {
        return trainingService.getTrainingTasks(page, size, status, modelType);
    }

    /**
     * 创建训练任务
     */
    @PostMapping("/tasks")
    public Result<Boolean> createTrainingTask(@RequestBody Map<String, Object> taskData) {
        return trainingService.createTrainingTask(taskData);
    }

    /**
     * 获取训练任务详情
     */
    @GetMapping("/tasks/{id}")
    public Result<Map<String, Object>> getTrainingTaskDetail(@PathVariable Long id) {
        return trainingService.getTrainingTaskDetail(id);
    }

    /**
     * 启动训练任务
     */
    @PostMapping("/tasks/{id}/start")
    public Result<Boolean> startTraining(@PathVariable Long id) {
        return trainingService.startTraining(id);
    }

    /**
     * 停止训练任务
     */
    @PostMapping("/tasks/{id}/stop")
    public Result<Boolean> stopTraining(@PathVariable Long id) {
        return trainingService.stopTraining(id);
    }

    /**
     * 获取训练进度
     */
    @GetMapping("/tasks/{id}/progress")
    public Result<Map<String, Object>> getTrainingProgress(@PathVariable Long id) {
        return trainingService.getTrainingProgress(id);
    }

    /**
     * 上传训练数据集
     */
    @PostMapping("/datasets")
    public Result<Boolean> uploadDataset(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("dataType") String dataType) {
        return trainingService.uploadDataset(file, name, description, dataType);
    }

    /**
     * 获取数据集列表
     */
    @GetMapping("/datasets")
    public Result<Map<String, Object>> getDatasets(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String dataType) {
        return trainingService.getDatasets(page, size, dataType);
    }

    /**
     * 数据预处理
     */
    @PostMapping("/datasets/{id}/preprocess")
    public Result<Boolean> preprocessData(
            @PathVariable Long id,
            @RequestBody Map<String, Object> preprocessConfig) {
        return trainingService.preprocessData(id, preprocessConfig);
    }

    /**
     * 获取训练日志
     */
    @GetMapping("/tasks/{id}/logs")
    public Result<Map<String, Object>> getTrainingLogs(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "50") Integer size) {
        return trainingService.getTrainingLogs(id, page, size);
    }

    /**
     * 模型评估
     */
    @PostMapping("/tasks/{id}/evaluate")
    public Result<Map<String, Object>> evaluateModel(
            @PathVariable Long id,
            @RequestBody Map<String, Object> evaluationConfig) {
        return trainingService.evaluateModel(id, evaluationConfig);
    }

    /**
     * 获取训练统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getTrainingStatistics() {
        return trainingService.getTrainingStatistics();
    }

    /**
     * 导出训练模型
     */
    @PostMapping("/tasks/{id}/export")
    public Result<Boolean> exportModel(
            @PathVariable Long id,
            @RequestBody Map<String, Object> exportConfig) {
        return trainingService.exportModel(id, exportConfig);
    }

    /**
     * 获取支持的模型类型
     */
    @GetMapping("/model-types")
    public Result<Map<String, Object>> getSupportedModelTypes() {
        return trainingService.getSupportedModelTypes();
    }

    /**
     * 获取预处理选项
     */
    @GetMapping("/preprocess-options")
    public Result<Map<String, Object>> getPreprocessOptions() {
        return trainingService.getPreprocessOptions();
    }

    /**
     * 评估模型
     */
    @PostMapping("/tasks/{taskId}/evaluate")
    public Result<Map<String, Object>> evaluateModel(
            @PathVariable Long taskId,
            @RequestParam Long testDatasetId) {
        return trainingService.evaluateModel(taskId, testDatasetId);
    }

    /**
     * 部署模型
     */
    @PostMapping("/tasks/{taskId}/deploy")
    public Result<Map<String, Object>> deployModel(
            @PathVariable Long taskId,
            @RequestParam String deploymentName,
            @RequestParam String deploymentType) {
        return trainingService.deployModel(taskId, deploymentName, deploymentType);
    }

    /**
     * 导出模型
     */
    @PostMapping("/tasks/{taskId}/export")
    public Result<Map<String, Object>> exportModel(
            @PathVariable Long taskId,
            @RequestParam String format) {
        return trainingService.exportModel(taskId, format);
    }
}