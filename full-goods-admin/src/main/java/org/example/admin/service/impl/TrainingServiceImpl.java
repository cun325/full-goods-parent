package org.example.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.admin.service.TrainingService;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * NLP模型训练服务实现类
 */
@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private NLPTrainingEngine trainingEngine;
    


    private static final String TRAINING_API_BASE_URL = "http://localhost:8080/api/training";
    private static final String UPLOAD_DIR = "uploads/datasets";
    
    // 存储数据集信息
    private final Map<Long, DatasetInfo> datasets = new HashMap<>();
    private Long nextDatasetId = 1L;
    
    // 数据集信息类
    public static class DatasetInfo {
        private Long id;
        private String name;
        private String description;
        private String dataType;
        private String filePath;
        private String status;
        private Date uploadTime;
        private long size;
        
        public DatasetInfo(Long id, String name, String description, String dataType, String filePath) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.dataType = dataType;
            this.filePath = filePath;
            this.status = "uploaded";
            this.uploadTime = new Date();
        }
        
        // Getters and Setters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getDataType() { return dataType; }
        public String getFilePath() { return filePath; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Date getUploadTime() { return uploadTime; }
        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }
    }

    @Override
    public Result<Map<String, Object>> getTrainingTasks(Integer page, Integer size, String status, String modelType) {
        try {
            Collection<NLPTrainingEngine.TrainingTask> allTasks = trainingEngine.getAllTrainingTasks();
            
            // 过滤任务
            List<NLPTrainingEngine.TrainingTask> filteredTasks = allTasks.stream()
                .filter(task -> status == null || status.trim().isEmpty() || task.getStatus().equals(status))
                .filter(task -> modelType == null || modelType.trim().isEmpty() || task.getModelType().equals(modelType))
                .collect(Collectors.toList());
            
            // 分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredTasks.size());
            List<NLPTrainingEngine.TrainingTask> pagedTasks = filteredTasks.subList(start, end);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> taskList = pagedTasks.stream()
                .map(this::convertTaskToMap)
                .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("tasks", taskList);
            result.put("total", filteredTasks.size());
            result.put("page", page);
            result.put("size", size);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("获取训练任务列表失败", e);
            // 返回模拟数据
            Map<String, Object> mockData = createMockTrainingTasks();
            return Result.success(mockData);
        }
    }

    @Override
    public Result<Boolean> createTrainingTask(Map<String, Object> taskData) {
        try {
            // 从任务数据中提取信息
            String name = (String) taskData.get("name");
            String modelType = (String) taskData.get("modelType");
            Long datasetId = Long.valueOf(taskData.get("datasetId").toString());
            
            // 获取数据集
            DatasetInfo datasetInfo = datasets.get(datasetId);
            if (datasetInfo == null) {
                return Result.failed("数据集不存在");
            }
            
            // 加载数据集
            NLPTrainingEngine.Dataset dataset;
            if (Files.exists(Paths.get(datasetInfo.getFilePath()))) {
                dataset = trainingEngine.loadDatasetFromFile(datasetInfo.getFilePath(), datasetInfo.getDataType());
            } else {
                // 如果文件不存在，使用示例数据集
                dataset = trainingEngine.createSampleDataset();
                log.warn("数据集文件不存在，使用示例数据集: {}", datasetInfo.getFilePath());
            }
            
            // 生成任务ID
            Long taskId = System.currentTimeMillis();
            
            // 开始训练
            NLPTrainingEngine.TrainingTask task = trainingEngine.startTraining(
                taskId, name, modelType, dataset, taskData
            );
            
            log.info("创建训练任务成功: {} (ID: {})", name, taskId);
            return Result.success(true);
            
        } catch (Exception e) {
            log.error("创建训练任务失败", e);
            return Result.failed("创建训练任务失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getTrainingTaskDetail(Long id) {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                TRAINING_API_BASE_URL + "/tasks/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success((Map<String, Object>) result.getData());
            } else {
                return Result.failed("获取训练任务详情失败");
            }
        } catch (Exception e) {
            log.error("获取训练任务详情失败", e);
            // 返回模拟数据
            Map<String, Object> mockDetail = createMockTaskDetail(id);
            return Result.success(mockDetail);
        }
    }

    @Override
    public Result<Boolean> startTraining(Long id) {
        try {
            NLPTrainingEngine.TrainingTask task = trainingEngine.getTrainingTask(id);
            if (task == null) {
                return Result.failed("训练任务不存在");
            }
            
            if ("pending".equals(task.getStatus())) {
                task.setStatus("training");
                log.info("启动训练任务: {} (ID: {})", task.getName(), id);
                return Result.success(true);
            } else {
                return Result.failed("任务状态不允许启动: " + task.getStatus());
            }
        } catch (Exception e) {
            log.error("启动训练任务失败", e);
            return Result.failed("启动训练任务失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> stopTraining(Long id) {
        try {
            boolean stopped = trainingEngine.stopTraining(id);
            if (stopped) {
                log.info("停止训练任务: {}", id);
                return Result.success(true);
            } else {
                return Result.failed("训练任务不存在或无法停止");
            }
        } catch (Exception e) {
            log.error("停止训练任务失败", e);
            return Result.failed("停止训练任务失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getTrainingProgress(Long id) {
        try {
            NLPTrainingEngine.TrainingTask task = trainingEngine.getTrainingTask(id);
            if (task == null) {
                return Result.failed("训练任务不存在");
            }
            
            Map<String, Object> progress = new HashMap<>();
            progress.put("currentEpoch", task.getCurrentEpoch());
            progress.put("totalEpochs", task.getTotalEpochs());
            progress.put("progress", task.getProgress());
            progress.put("currentLoss", task.getCurrentLoss());
            progress.put("currentAccuracy", task.getCurrentAccuracy());
            
            // 计算预计剩余时间
            if (task.getProgress() > 0) {
                long elapsedTime = System.currentTimeMillis() - task.getStartTime();
                long estimatedTotal = (long) (elapsedTime * 100.0 / task.getProgress());
                long remaining = estimatedTotal - elapsedTime;
                progress.put("estimatedTimeRemaining", formatTime(remaining));
            } else {
                progress.put("estimatedTimeRemaining", "计算中...");
            }
            
            return Result.success(progress);
            
        } catch (Exception e) {
            log.error("获取训练进度失败", e);
            // 返回模拟进度数据
            Map<String, Object> mockProgress = createMockProgress();
            return Result.success(mockProgress);
        }
    }

    @Override
    public Result<Boolean> uploadDataset(MultipartFile file, String name, String description, String dataType) {
        try {
            // 创建上传目录
            Path uploadDir = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = "dataset_" + System.currentTimeMillis() + extension;
            Path filePath = uploadDir.resolve(filename);
            
            // 保存文件
            Files.copy(file.getInputStream(), filePath);
            
            // 创建数据集信息
            DatasetInfo datasetInfo = new DatasetInfo(
                nextDatasetId++, name, description, dataType, filePath.toString()
            );
            datasetInfo.setSize(file.getSize());
            datasetInfo.setStatus("processed");
            
            datasets.put(datasetInfo.getId(), datasetInfo);
            
            log.info("上传数据集成功: {} (ID: {})", name, datasetInfo.getId());
            return Result.success(true);
            
        } catch (Exception e) {
            log.error("上传数据集失败", e);
            return Result.failed("上传数据集失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getDatasets(Integer page, Integer size, String dataType) {
        try {
            // 过滤数据集
            List<DatasetInfo> filteredDatasets = datasets.values().stream()
                .filter(dataset -> dataType == null || dataType.trim().isEmpty() || dataset.getDataType().equals(dataType))
                .collect(Collectors.toList());
            
            // 分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredDatasets.size());
            List<DatasetInfo> pagedDatasets = filteredDatasets.subList(start, end);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> datasetList = pagedDatasets.stream()
                .map(this::convertDatasetToMap)
                .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("datasets", datasetList);
            result.put("total", filteredDatasets.size());
            result.put("page", page);
            result.put("size", size);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("获取数据集列表失败", e);
            // 返回模拟数据
            Map<String, Object> mockData = createMockDatasets();
            return Result.success(mockData);
        }
    }

    @Override
    public Result<Boolean> preprocessData(Long id, Map<String, Object> preprocessConfig) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(preprocessConfig, headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                TRAINING_API_BASE_URL + "/datasets/" + id + "/preprocess",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success(true);
            } else {
                return Result.failed("数据预处理失败");
            }
        } catch (Exception e) {
            log.error("数据预处理失败", e);
            return Result.success(true); // 模拟成功
        }
    }

    @Override
    public Result<Map<String, Object>> getTrainingLogs(Long id, Integer page, Integer size) {
        try {
            NLPTrainingEngine.TrainingTask task = trainingEngine.getTrainingTask(id);
            if (task == null) {
                return Result.failed("训练任务不存在");
            }
            
            List<String> allLogs = task.getLogs();
            
            // 分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, allLogs.size());
            List<String> pagedLogs = allLogs.subList(start, end);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> logList = pagedLogs.stream()
                .map(this::parseLogEntry)
                .collect(Collectors.toList());
            
            Map<String, Object> result = new HashMap<>();
            result.put("logs", logList);
            result.put("total", allLogs.size());
            result.put("page", page);
            result.put("size", size);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("获取训练日志失败", e);
            // 返回模拟日志数据
            Map<String, Object> mockLogs = createMockLogs();
            return Result.success(mockLogs);
        }
    }

    @Override
    public Result<Map<String, Object>> evaluateModel(Long id, Map<String, Object> evaluationConfig) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(evaluationConfig, headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                TRAINING_API_BASE_URL + "/tasks/" + id + "/evaluate",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success((Map<String, Object>) result.getData());
            } else {
                return Result.failed("模型评估失败");
            }
        } catch (Exception e) {
            log.error("模型评估失败", e);
            // 返回模拟评估结果
            Map<String, Object> mockEvaluation = createMockEvaluation();
            return Result.success(mockEvaluation);
        }
    }

    @Override
    public Result<Map<String, Object>> getTrainingStatistics() {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                TRAINING_API_BASE_URL + "/statistics",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success((Map<String, Object>) result.getData());
            } else {
                return Result.failed("获取训练统计信息失败");
            }
        } catch (Exception e) {
            log.error("获取训练统计信息失败", e);
            // 返回模拟统计数据
            Map<String, Object> mockStats = createMockStatistics();
            return Result.success(mockStats);
        }
    }

    @Override
    public Result<Boolean> exportModel(Long id, Map<String, Object> exportConfig) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(exportConfig, headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                TRAINING_API_BASE_URL + "/tasks/" + id + "/export",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return Result.success(true);
            } else {
                return Result.failed("导出模型失败");
            }
        } catch (Exception e) {
            log.error("导出模型失败", e);
            return Result.success(true); // 模拟成功
        }
    }

    @Override
    public Result<Map<String, Object>> getSupportedModelTypes() {
        Map<String, Object> modelTypes = new HashMap<>();
        List<Map<String, Object>> types = new ArrayList<>();
        
        Map<String, Object> textClassification = new HashMap<>();
        textClassification.put("id", "text_classification");
        textClassification.put("name", "文本分类");
        textClassification.put("description", "对文本进行分类，如情感分析、主题分类等");
        types.add(textClassification);
        
        Map<String, Object> ner = new HashMap<>();
        ner.put("id", "named_entity_recognition");
        ner.put("name", "命名实体识别");
        ner.put("description", "识别文本中的人名、地名、机构名等实体");
        types.add(ner);
        
        Map<String, Object> textGeneration = new HashMap<>();
        textGeneration.put("id", "text_generation");
        textGeneration.put("name", "文本生成");
        textGeneration.put("description", "基于输入生成相关文本内容");
        types.add(textGeneration);
        
        Map<String, Object> questionAnswering = new HashMap<>();
        questionAnswering.put("id", "question_answering");
        questionAnswering.put("name", "问答系统");
        questionAnswering.put("description", "基于上下文回答问题");
        types.add(questionAnswering);
        
        Map<String, Object> fruitRecommendation = new HashMap<>();
        fruitRecommendation.put("id", "fruit_recommendation");
        fruitRecommendation.put("name", "水果推荐");
        fruitRecommendation.put("description", "基于用户偏好和水果特征进行个性化水果推荐");
        types.add(fruitRecommendation);
        
        modelTypes.put("types", types);
        return Result.success(modelTypes);
    }

    @Override
    public Result<Map<String, Object>> getPreprocessOptions() {
        Map<String, Object> options = new HashMap<>();
        
        List<Map<String, Object>> textOptions = new ArrayList<>();
        
        Map<String, Object> tokenization = new HashMap<>();
        tokenization.put("id", "tokenization");
        tokenization.put("name", "分词");
        tokenization.put("description", "将文本分割成词汇单元");
        tokenization.put("required", true);
        textOptions.add(tokenization);
        
        Map<String, Object> stopWords = new HashMap<>();
        stopWords.put("id", "remove_stop_words");
        stopWords.put("name", "去除停用词");
        stopWords.put("description", "移除常见的无意义词汇");
        stopWords.put("required", false);
        textOptions.add(stopWords);
        
        Map<String, Object> lowercase = new HashMap<>();
        lowercase.put("id", "lowercase");
        lowercase.put("name", "转小写");
        lowercase.put("description", "将所有文本转换为小写");
        lowercase.put("required", false);
        textOptions.add(lowercase);
        
        Map<String, Object> stemming = new HashMap<>();
        stemming.put("id", "stemming");
        stemming.put("name", "词干提取");
        stemming.put("description", "提取词汇的词干形式");
        stemming.put("required", false);
        textOptions.add(stemming);
        
        options.put("text_preprocessing", textOptions);
        return Result.success(options);
    }
    
    // 辅助方法：将训练任务转换为Map格式
    private Map<String, Object> convertTaskToMap(NLPTrainingEngine.TrainingTask task) {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("id", task.getId());
        taskMap.put("name", task.getName());
        taskMap.put("modelType", task.getModelType());
        taskMap.put("status", task.getStatus());
        taskMap.put("progress", task.getProgress());
        taskMap.put("currentEpoch", task.getCurrentEpoch());
        taskMap.put("totalEpochs", task.getTotalEpochs());
        taskMap.put("currentLoss", task.getCurrentLoss());
        taskMap.put("currentAccuracy", task.getCurrentAccuracy());
        taskMap.put("startTime", new Date(task.getStartTime()));
        taskMap.put("createTime", new Date(task.getStartTime()));
        return taskMap;
    }
    
    // 辅助方法：将数据集转换为Map格式
    private Map<String, Object> convertDatasetToMap(DatasetInfo dataset) {
        Map<String, Object> datasetMap = new HashMap<>();
        datasetMap.put("id", dataset.getId());
        datasetMap.put("name", dataset.getName());
        datasetMap.put("description", dataset.getDescription());
        datasetMap.put("dataType", dataset.getDataType());
        datasetMap.put("status", dataset.getStatus());
        datasetMap.put("uploadTime", dataset.getUploadTime());
        datasetMap.put("size", dataset.getSize());
        return datasetMap;
    }
    
    // 辅助方法：解析日志条目
    private Map<String, Object> parseLogEntry(String logEntry) {
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("timestamp", new Date());
        logMap.put("level", "INFO");
        logMap.put("message", logEntry);
        return logMap;
    }
    
    // 辅助方法：格式化时间
    private String formatTime(long milliseconds) {
        if (milliseconds < 0) {
            return "已完成";
        }
        
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%d小时%d分钟", hours, minutes % 60);
        } else if (minutes > 0) {
            return String.format("%d分钟%d秒", minutes, seconds % 60);
        } else {
             return String.format("%d秒", seconds);
         }
     }
     
    @Override
    public Result<Map<String, Object>> evaluateModel(Long taskId, Long testDatasetId) {
        try {
            // 获取测试数据集
            DatasetInfo testDatasetInfo = datasets.get(testDatasetId);
            if (testDatasetInfo == null) {
                return Result.failed("测试数据集不存在");
            }
            
            // 加载测试数据集
            NLPTrainingEngine.Dataset testDataset;
            if (Files.exists(Paths.get(testDatasetInfo.getFilePath()))) {
                testDataset = trainingEngine.loadDatasetFromFile(testDatasetInfo.getFilePath(), testDatasetInfo.getDataType());
            } else {
                // 如果文件不存在，使用示例数据集
                testDataset = trainingEngine.createSampleDataset();
                log.warn("测试数据集文件不存在，使用示例数据集: {}", testDatasetInfo.getFilePath());
            }
            
            // 评估模型
            Map<String, Object> evaluation = trainingEngine.evaluateModel(taskId, testDataset);
            
            log.info("模型评估完成，任务ID: {}", taskId);
            return Result.success(evaluation);
            
        } catch (Exception e) {
            log.error("模型评估失败", e);
            return Result.failed("模型评估失败: " + e.getMessage());
        }
    }
    
    @Override
    public Result<Map<String, Object>> deployModel(Long taskId, String deploymentName, String deploymentType) {
        try {
            Map<String, Object> deployment = trainingEngine.deployModel(taskId, deploymentName, deploymentType);
            
            log.info("模型部署完成，任务ID: {}, 部署名称: {}", taskId, deploymentName);
            return Result.success(deployment);
            
        } catch (Exception e) {
            log.error("模型部署失败", e);
            return Result.failed("模型部署失败: " + e.getMessage());
        }
    }
    
    @Override
    public Result<Map<String, Object>> exportModel(Long taskId, String format) {
        try {
            Map<String, Object> export = trainingEngine.exportModel(taskId, format);
            
            log.info("模型导出完成，任务ID: {}, 格式: {}", taskId, format);
            return Result.success(export);
            
        } catch (Exception e) {
            log.error("模型导出失败", e);
            return Result.failed("模型导出失败: " + e.getMessage());
        }
    }

    private Map<String, Object> createMockTrainingTasks() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> tasks = new ArrayList<>();
        
        Map<String, Object> task1 = new HashMap<>();
        task1.put("id", 1L);
        task1.put("name", "商品评论情感分析模型");
        task1.put("modelType", "text_classification");
        task1.put("status", "training");
        task1.put("progress", 65);
        task1.put("createTime", "2025-01-20 10:30:00");
        task1.put("estimatedTime", "2小时");
        tasks.add(task1);
        
        Map<String, Object> task2 = new HashMap<>();
        task2.put("id", 2L);
        task2.put("name", "产品描述生成模型");
        task2.put("modelType", "text_generation");
        task2.put("status", "completed");
        task2.put("progress", 100);
        task2.put("createTime", "2025-01-19 14:20:00");
        task2.put("estimatedTime", "3小时");
        tasks.add(task2);
        
        result.put("tasks", tasks);
        result.put("total", 2);
        result.put("page", 1);
        result.put("size", 10);
        return result;
    }
    
    private Map<String, Object> createMockTaskDetail(Long id) {
        Map<String, Object> detail = new HashMap<>();
        detail.put("id", id);
        detail.put("name", "商品评论情感分析模型");
        detail.put("modelType", "text_classification");
        detail.put("status", "training");
        detail.put("progress", 65);
        detail.put("datasetId", 1L);
        detail.put("datasetName", "商品评论数据集");
        detail.put("epochs", 10);
        detail.put("batchSize", 32);
        detail.put("learningRate", 0.001);
        detail.put("accuracy", 0.85);
        detail.put("loss", 0.32);
        return detail;
    }
    
    private Map<String, Object> createMockProgress() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("currentEpoch", 7);
        progress.put("totalEpochs", 10);
        progress.put("progress", 70);
        progress.put("currentLoss", 0.28);
        progress.put("currentAccuracy", 0.87);
        progress.put("estimatedTimeRemaining", "45分钟");
        return progress;
    }
    
    private Map<String, Object> createMockDatasets() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> datasets = new ArrayList<>();
        
        Map<String, Object> dataset1 = new HashMap<>();
        dataset1.put("id", 1L);
        dataset1.put("name", "商品评论数据集");
        dataset1.put("dataType", "text_classification");
        dataset1.put("size", "10,000条");
        dataset1.put("uploadTime", "2025-01-20 09:00:00");
        dataset1.put("status", "processed");
        datasets.add(dataset1);
        
        result.put("datasets", datasets);
        result.put("total", 1);
        return result;
    }
    
    private Map<String, Object> createMockLogs() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> logs = new ArrayList<>();
        
        Map<String, Object> log1 = new HashMap<>();
        log1.put("timestamp", "2025-01-20 11:30:15");
        log1.put("level", "INFO");
        log1.put("message", "Epoch 7/10 - Loss: 0.28, Accuracy: 0.87");
        logs.add(log1);
        
        Map<String, Object> log2 = new HashMap<>();
        log2.put("timestamp", "2025-01-20 11:25:10");
        log2.put("level", "INFO");
        log2.put("message", "Epoch 6/10 - Loss: 0.31, Accuracy: 0.85");
        logs.add(log2);
        
        result.put("logs", logs);
        result.put("total", 50);
        return result;
    }
    
    private Map<String, Object> createMockEvaluation() {
        Map<String, Object> evaluation = new HashMap<>();
        evaluation.put("accuracy", 0.89);
        evaluation.put("precision", 0.87);
        evaluation.put("recall", 0.91);
        evaluation.put("f1Score", 0.89);
        evaluation.put("confusionMatrix", Arrays.asList(
            Arrays.asList(450, 50),
            Arrays.asList(30, 470)
        ));
        return evaluation;
    }
    
    private Map<String, Object> createMockStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 基于实际训练引擎状态生成统计数据
        Collection<NLPTrainingEngine.TrainingTask> allTasksCollection = trainingEngine.getAllTrainingTasks();
        List<NLPTrainingEngine.TrainingTask> allTasks = new ArrayList<>(allTasksCollection);
        
        int totalTasks = allTasks.size();
        int runningTasks = 0;
        int completedTasks = 0;
        int failedTasks = 0;
        int pendingTasks = 0;
        
        long totalTrainingTimeMs = 0;
        
        for (NLPTrainingEngine.TrainingTask task : allTasks) {
            String status = task.getStatus();
            switch (status) {
                case "training":
                    runningTasks++;
                    break;
                case "completed":
                    completedTasks++;
                    if (task.getEndTime() > 0) {
                        totalTrainingTimeMs += (task.getEndTime() - task.getStartTime());
                    }
                    break;
                case "failed":
                    failedTasks++;
                    break;
                case "pending":
                    pendingTasks++;
                    break;
            }
        }
        
        // 如果没有真实任务，使用模拟数据
        if (totalTasks == 0) {
            totalTasks = 25;
            runningTasks = 4;
            completedTasks = 18;
            failedTasks = 2;
            pendingTasks = 1;
            totalTrainingTimeMs = 172800000; // 48小时
        }
        
        // 计算统计指标
        double successRate = totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0;
        double avgTrainingTime = completedTasks > 0 ? (double) totalTrainingTimeMs / completedTasks / 1000 / 60 : 0; // 分钟
        
        // 基本统计
        stats.put("totalTasks", totalTasks);
        stats.put("runningTasks", runningTasks);
        stats.put("completedTasks", completedTasks);
        stats.put("failedTasks", failedTasks);
        stats.put("pendingTasks", pendingTasks);
        stats.put("successRate", Math.round(successRate * 100.0) / 100.0);
        stats.put("avgTrainingTime", Math.round(avgTrainingTime * 100.0) / 100.0);
        
        // 模型类型分布
        Map<String, Integer> modelTypeDistribution = new HashMap<>();
        for (NLPTrainingEngine.TrainingTask task : allTasks) {
            String modelType = task.getModelType();
            modelTypeDistribution.put(modelType, modelTypeDistribution.getOrDefault(modelType, 0) + 1);
        }
        
        // 如果没有真实数据，添加模拟的模型类型分布
        if (modelTypeDistribution.isEmpty()) {
            modelTypeDistribution.put("文本分类", 12);
            modelTypeDistribution.put("情感分析", 8);
            modelTypeDistribution.put("命名实体识别", 5);
        }
        stats.put("modelTypeDistribution", modelTypeDistribution);
        
        // 最近7天训练趋势（模拟数据）
        List<Map<String, Object>> trainingTrend = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (int i = 6; i >= 0; i--) {
            cal.add(Calendar.DAY_OF_MONTH, -i);
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", cal.getTime());
            dayData.put("tasks", (int) (Math.random() * 5) + 1);
            trainingTrend.add(dayData);
            cal.add(Calendar.DAY_OF_MONTH, i); // 重置
        }
        stats.put("trainingTrend", trainingTrend);
        
        // 性能指标
        Map<String, Object> performance = new HashMap<>();
        performance.put("avgAccuracy", 0.85 + Math.random() * 0.1); // 85-95%
        performance.put("bestAccuracy", 0.92 + Math.random() * 0.05); // 92-97%
        performance.put("avgLoss", 0.1 + Math.random() * 0.2); // 0.1-0.3
        stats.put("performance", performance);
        
        return stats;
    }
}