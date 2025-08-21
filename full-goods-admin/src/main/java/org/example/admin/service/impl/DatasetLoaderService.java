package org.example.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 数据集加载服务
 * 负责从CSV文件加载训练数据
 */
@Slf4j
@Service
public class DatasetLoaderService {

    /**
     * 从CSV文件加载水果推荐数据集
     */
    public NLPTrainingEngine.Dataset loadFruitRecommendationDataset() {
        NLPTrainingEngine.Dataset dataset = new NLPTrainingEngine.Dataset();
        
        try {
            // 加载主要的水果推荐数据集
            loadCsvFile("fruit_recommendation_dataset.csv", dataset);
            
            // 加载用户行为数据集
            loadCsvFile("user_behavior_dataset.csv", dataset);
            
            // 加载水果特征数据集
            loadCsvFile("fruit_features_dataset.csv", dataset);
            
            log.info("成功加载水果推荐数据集，总样本数: {}", dataset.getSamples().size());
            
        } catch (Exception e) {
            log.error("加载水果推荐数据集失败", e);
            // 如果加载失败，创建一些示例数据
            createSampleFruitData(dataset);
        }
        
        return dataset;
    }
    
    /**
     * 从CSV文件加载数据
     */
    private void loadCsvFile(String filename, NLPTrainingEngine.Dataset dataset) throws IOException {
        Path filePath = Paths.get(filename);
        
        if (!Files.exists(filePath)) {
            log.warn("文件不存在: {}", filename);
            return;
        }
        
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // 跳过标题行
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // 解析CSV行并添加到数据集
                if (filename.contains("fruit_recommendation")) {
                    parseFruitRecommendationLine(line, dataset);
                } else if (filename.contains("user_behavior")) {
                    parseUserBehaviorLine(line, dataset);
                } else if (filename.contains("fruit_features")) {
                    parseFruitFeaturesLine(line, dataset);
                }
            }
        }
    }
    
    /**
     * 解析水果推荐数据行
     */
    private void parseFruitRecommendationLine(String line, NLPTrainingEngine.Dataset dataset) {
        String[] parts = line.split(",");
        if (parts.length >= 15) {
            // 构建特征文本
            StringBuilder featureText = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                featureText.append(parts[i].trim());
                if (i < parts.length - 2) {
                    featureText.append(",");
                }
            }
            
            // 推荐评分作为标签
            String label = parts[parts.length - 1].trim();
            
            dataset.addSample(featureText.toString(), label);
        }
    }
    
    /**
     * 解析用户行为数据行
     */
    private void parseUserBehaviorLine(String line, NLPTrainingEngine.Dataset dataset) {
        String[] parts = line.split(",");
        if (parts.length >= 10) {
            // 构建用户行为特征文本
            String featureText = String.join(",", Arrays.copyOf(parts, parts.length - 1));
            
            // 使用行为类型作为标签
            String label = parts[2].trim(); // 行为类型
            
            dataset.addSample(featureText, label);
        }
    }
    
    /**
     * 解析水果特征数据行
     */
    private void parseFruitFeaturesLine(String line, NLPTrainingEngine.Dataset dataset) {
        String[] parts = line.split(",");
        if (parts.length >= 12) {
            // 构建水果特征文本
            String featureText = String.join(",", Arrays.copyOf(parts, parts.length - 1));
            
            // 使用受欢迎程度作为标签
            String label = parts[parts.length - 1].trim();
            
            dataset.addSample(featureText, label);
        }
    }
    
    /**
     * 创建示例水果数据
     */
    private void createSampleFruitData(NLPTrainingEngine.Dataset dataset) {
        log.info("创建示例水果推荐数据");
        
        // 添加一些示例数据
        String[] sampleData = {
            "user001,25,male,spring,sunny,apple;banana,apple,8.5,2.1,crisp,red,medium,3.5,9.2,high",
            "user002,30,female,summer,hot,orange;grape,orange,7.8,4.2,juicy,orange,large,4.2,8.7,high",
            "user003,22,male,autumn,cool,banana;apple,banana,9.1,1.5,soft,yellow,medium,2.8,8.9,medium",
            "user004,35,female,winter,cold,grape;orange,grape,6.5,3.8,firm,purple,small,5.1,7.8,medium",
            "user005,28,male,spring,rainy,apple;grape,apple,8.2,2.3,crisp,green,large,4.0,9.0,high"
        };
        
        for (String data : sampleData) {
            String[] parts = data.split(",");
            if (parts.length >= 15) {
                String featureText = String.join(",", Arrays.copyOf(parts, parts.length - 1));
                String label = parts[parts.length - 1];
                dataset.addSample(featureText, label);
            }
        }
    }
    
    /**
     * 获取数据集统计信息
     */
    public Map<String, Object> getDatasetStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 统计各个数据集文件的信息
            stats.put("fruit_recommendation_count", countLinesInFile("fruit_recommendation_dataset.csv"));
            stats.put("user_behavior_count", countLinesInFile("user_behavior_dataset.csv"));
            stats.put("fruit_features_count", countLinesInFile("fruit_features_dataset.csv"));
            
            // 计算总数据量
            int totalCount = (Integer) stats.get("fruit_recommendation_count") +
                           (Integer) stats.get("user_behavior_count") +
                           (Integer) stats.get("fruit_features_count");
            stats.put("total_samples", totalCount);
            
        } catch (Exception e) {
            log.error("获取数据集统计信息失败", e);
            stats.put("error", e.getMessage());
        }
        
        return stats;
    }
    
    /**
     * 统计文件行数
     */
    private int countLinesInFile(String filename) {
        Path filePath = Paths.get(filename);
        if (!Files.exists(filePath)) {
            return 0;
        }
        
        try {
            return (int) Files.lines(filePath).count() - 1; // 减去标题行
        } catch (IOException e) {
            log.error("统计文件行数失败: {}", filename, e);
            return 0;
        }
    }
}