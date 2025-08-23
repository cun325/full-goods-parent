package org.example.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.api.service.ModelPredictionService;
import org.example.common.entity.Fruit;
import org.example.api.mapper.FruitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 模型预测服务实现类
 */
@Slf4j
@Service
public class ModelPredictionServiceImpl implements ModelPredictionService {

    @Autowired
    private FruitMapper fruitMapper;

    // 模拟已部署的模型（实际项目中应该从文件系统或数据库加载）
    private static final Map<String, Object> DEPLOYED_MODELS = new HashMap<>();

    // 模拟水果数据库
    private static final List<Map<String, Object>> FRUIT_DATABASE = Arrays.asList(
        createFruit("苹果", 4.5, 3.0, "脆嫩", "红色", "中等", 8.5, 85, "秋季"),
        createFruit("香蕉", 8.5, 1.0, "软糯", "黄色", "中等", 9.0, 89, "全年"),
        createFruit("橙子", 6.0, 4.5, "多汁", "橙色", "中等", 8.8, 87, "冬季"),
        createFruit("草莓", 7.5, 3.5, "柔软", "红色", "小", 7.5, 91, "春季"),
        createFruit("葡萄", 8.0, 2.5, "多汁", "紫色", "小", 8.2, 82, "秋季"),
        createFruit("西瓜", 6.5, 1.0, "多汁", "绿色", "大", 9.5, 92, "夏季"),
        createFruit("桃子", 7.0, 3.0, "柔软", "粉色", "中等", 8.0, 89, "夏季"),
        createFruit("梨", 6.0, 2.0, "脆嫩", "黄色", "中等", 8.3, 84, "秋季"),
        createFruit("樱桃", 8.5, 3.5, "脆嫩", "红色", "小", 7.8, 82, "春季"),
        createFruit("芒果", 9.0, 2.0, "柔软", "黄色", "中等", 8.7, 84, "夏季"),
        createFruit("菠萝", 7.5, 4.0, "脆嫩", "黄色", "大", 8.5, 86, "夏季"),
        createFruit("猕猴桃", 5.5, 4.5, "柔软", "绿色", "小", 9.2, 83, "秋季")
    );

    static {
        // 初始化模拟模型数据
        initializeModelData();
    }

    /**
     * 初始化模型数据
     */
    private static void initializeModelData() {
        // 模拟水果推荐模型数据
        Map<String, Double> fruitScores = new HashMap<>();
        fruitScores.put("苹果", 4.8);
        fruitScores.put("香蕉", 4.7);
        fruitScores.put("橙子", 4.6);
        fruitScores.put("草莓", 4.9);
        fruitScores.put("葡萄", 4.5);
        fruitScores.put("西瓜", 4.4);
        fruitScores.put("桃子", 4.6);
        fruitScores.put("梨", 4.3);
        fruitScores.put("樱桃", 4.8);
        fruitScores.put("芒果", 4.7);
        fruitScores.put("菠萝", 4.5);
        fruitScores.put("猕猴桃", 4.4);

        Map<String, Map<String, Double>> userPreferences = new HashMap<>();
        Map<String, Double> user1001Prefs = new HashMap<>();
        user1001Prefs.put("苹果", 4.5);
        user1001Prefs.put("香蕉", 4.2);
        user1001Prefs.put("橙子", 4.7);
        userPreferences.put("1001", user1001Prefs);

        Map<String, Double> user1002Prefs = new HashMap<>();
        user1002Prefs.put("草莓", 4.9);
        user1002Prefs.put("葡萄", 4.6);
        user1002Prefs.put("西瓜", 4.3);
        userPreferences.put("1002", user1002Prefs);

        Map<String, Map<String, Double>> fruitFeatures = new HashMap<>();
        Map<String, Double> appleFeatures = new HashMap<>();
        appleFeatures.put("sweetness", 4.5);
        appleFeatures.put("acidity", 3.0);
        appleFeatures.put("price", 8.5);
        appleFeatures.put("nutrition", 85.0);
        fruitFeatures.put("苹果", appleFeatures);

        Map<String, Double> bananaFeatures = new HashMap<>();
        bananaFeatures.put("sweetness", 8.5);
        bananaFeatures.put("acidity", 1.0);
        bananaFeatures.put("price", 9.0);
        bananaFeatures.put("nutrition", 89.0);
        fruitFeatures.put("香蕉", bananaFeatures);

        // 存储模型数据
        Map<String, Object> fruitRecommendationModel = new HashMap<>();
        fruitRecommendationModel.put("fruitScores", fruitScores);
        fruitRecommendationModel.put("userPreferences", userPreferences);
        fruitRecommendationModel.put("fruitFeatures", fruitFeatures);
        fruitRecommendationModel.put("type", "fruit_recommendation");

        DEPLOYED_MODELS.put("fruit_recommendation_latest", fruitRecommendationModel);

        // 模拟文本分类模型数据
        Map<String, Object> textClassificationModel = new HashMap<>();
        Map<String, Double> classProbabilities = new HashMap<>();
        classProbabilities.put("positive", 0.7);
        classProbabilities.put("negative", 0.3);
        textClassificationModel.put("classProbabilities", classProbabilities);
        textClassificationModel.put("type", "text_classification");

        DEPLOYED_MODELS.put("text_classification_latest", textClassificationModel);
    }

    @Override
    public List<Fruit> predictFruitRecommendation(String userId, String condition, int limit) {
        log.info("开始水果推荐预测，用户ID: {}, 条件: {}, 限制数量: {}", userId, condition, limit);

        // 获取模型
        Map<String, Object> model = (Map<String, Object>) DEPLOYED_MODELS.get("fruit_recommendation_latest");
        if (model == null) {
            throw new RuntimeException("水果推荐模型未找到");
        }

        // 获取模型数据
        Map<String, Double> fruitScores = (Map<String, Double>) model.get("fruitScores");
        Map<String, Map<String, Double>> userPreferences = (Map<String, Map<String, Double>>) model.get("userPreferences");
        Map<String, Map<String, Double>> fruitFeatures = (Map<String, Map<String, Double>>) model.get("fruitFeatures");

        // 解析条件中的关键词
        List<String> keywords = extractKeywords(condition);
        log.info("解析关键词: {}", keywords);

        // 计算推荐分数
        Map<String, Double> recommendationScores = new HashMap<>();

        for (String fruitName : fruitScores.keySet()) {
            double score = calculateRecommendationScore(userId, fruitName, keywords, fruitScores, userPreferences, fruitFeatures);
            recommendationScores.put(fruitName, score);
        }

        // 根据分数排序并返回前N个结果
        List<Fruit> recommendations = recommendationScores.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(limit)
            .map(entry -> {
                // 根据水果名称查询数据库中的完整信息
                return fruitMapper.selectByName(entry.getKey());
            })
            .filter(Objects::nonNull) // 过滤掉null值
            .collect(Collectors.toList());

        log.info("推荐完成，返回 {} 个结果", recommendations.size());
        return recommendations;
    }

    @Override
    public Map<String, Object> predictTextClassification(String text) {
        log.info("开始文本分类预测，文本: {}", text);

        // 获取模型
        Map<String, Object> model = (Map<String, Object>) DEPLOYED_MODELS.get("text_classification_latest");
        if (model == null) {
            throw new RuntimeException("文本分类模型未找到");
        }

        // 获取模型数据
        Map<String, Double> classProbabilities = (Map<String, Double>) model.get("classProbabilities");

        // 简单的情感分析（模拟）
        double positiveScore = 0.5;
        double negativeScore = 0.5;

        if (text.contains("好") || text.contains("棒") || text.contains("喜欢")) {
            positiveScore += 0.3;
        }

        if (text.contains("差") || text.contains("坏") || text.contains("讨厌")) {
            negativeScore += 0.3;
        }

        // 结合模型概率
        positiveScore = (positiveScore + classProbabilities.get("positive")) / 2;
        negativeScore = (negativeScore + classProbabilities.get("negative")) / 2;

        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("predictedClass", positiveScore > negativeScore ? "positive" : "negative");

        Map<String, Double> scores = new HashMap<>();
        scores.put("positive", positiveScore);
        scores.put("negative", negativeScore);
        result.put("scores", scores);

        log.info("分类完成，预测类别: {}", result.get("predictedClass"));
        return result;
    }

    /**
     * 计算推荐分数
     */
    private double calculateRecommendationScore(String userId, String fruitName, List<String> keywords,
                                             Map<String, Double> fruitScores,
                                             Map<String, Map<String, Double>> userPreferences,
                                             Map<String, Map<String, Double>> fruitFeatures) {
        // 基础分数
        double baseScore = fruitScores.getOrDefault(fruitName, 3.0);

        // 用户偏好分数
        double userScore = 3.0;
        if (userPreferences.containsKey(userId)) {
            userScore = userPreferences.get(userId).getOrDefault(fruitName, 3.0);
        }

        // 关键词匹配分数
        double keywordScore = 0.0;
        Map<String, Object> fruitDetail = FRUIT_DATABASE.stream()
            .filter(fruit -> fruitName.equals(fruit.get("name")))
            .findFirst()
            .orElse(new HashMap<>());

        for (String keyword : keywords) {
            if (fruitName.contains(keyword) ||
                (fruitDetail.get("taste") != null && fruitDetail.get("taste").toString().contains(keyword)) ||
                (fruitDetail.get("color") != null && fruitDetail.get("color").toString().contains(keyword)) ||
                (fruitDetail.get("season") != null && fruitDetail.get("season").toString().contains(keyword))) {
                keywordScore += 1.0;
            }
        }

        // 特征分数
        double featureScore = 0.0;
        if (fruitFeatures.containsKey(fruitName)) {
            Map<String, Double> features = fruitFeatures.get(fruitName);
            featureScore = features.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }

        // 综合计算分数
        return (baseScore * 0.3 + userScore * 0.3 + keywordScore * 0.2 + featureScore * 0.2);
    }

    /**
     * 提取关键词
     */
    private List<String> extractKeywords(String text) {
        List<String> keywords = new ArrayList<>();

        // 简单的关键词提取（实际项目中可以使用NLP技术）
        if (text.contains("维生素")) {
            keywords.add("营养");
        }

        if (text.contains("甜")) {
            keywords.add("甜");
        }

        if (text.contains("酸")) {
            keywords.add("酸");
        }

        if (text.contains("夏天") || text.contains("夏季")) {
            keywords.add("夏季");
        }

        if (text.contains("冬天") || text.contains("冬季")) {
            keywords.add("冬季");
        }

        return keywords;
    }

    /**
     * 创建水果对象
     */
    private static Map<String, Object> createFruit(String name, double sweetness, double acidity,
                                                  String texture, String color, String size,
                                                  double nutritionScore, double waterContent,
                                                  String season) {
        Map<String, Object> fruit = new HashMap<>();
        fruit.put("name", name);
        fruit.put("sweetness", sweetness);
        fruit.put("acidity", acidity);
        fruit.put("texture", texture);
        fruit.put("color", color);
        fruit.put("size", size);
        fruit.put("nutritionScore", nutritionScore);
        fruit.put("waterContent", waterContent);
        fruit.put("season", season);
        return fruit;
    }
}