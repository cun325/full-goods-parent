package org.example.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 水果推荐服务
 * 基于训练好的模型提供水果推荐功能
 */
@Slf4j
@Service
public class FruitRecommendationService {
    
    // 模拟水果数据库
    private static final List<Map<String, Object>> FRUIT_DATABASE = Arrays.asList(
        createFruit("苹果", "apple", 4.5, 3.0, "脆嫩", "红色", "中等", 8.5, 85, "autumn", 4.8),
        createFruit("香蕉", "banana", 8.5, 1.0, "软糯", "黄色", "中等", 9.0, 89, "all", 4.7),
        createFruit("橙子", "orange", 6.0, 4.5, "多汁", "橙色", "中等", 8.8, 87, "winter", 4.6),
        createFruit("草莓", "strawberry", 7.5, 3.5, "柔软", "红色", "小", 7.5, 91, "spring", 4.9),
        createFruit("葡萄", "grape", 8.0, 2.5, "多汁", "紫色", "小", 8.2, 82, "autumn", 4.5),
        createFruit("西瓜", "watermelon", 6.5, 1.0, "多汁", "绿色", "大", 9.5, 92, "summer", 4.4),
        createFruit("桃子", "peach", 7.0, 3.0, "柔软", "粉色", "中等", 8.0, 89, "summer", 4.6),
        createFruit("梨", "pear", 6.0, 2.0, "脆嫩", "黄色", "中等", 8.3, 84, "autumn", 4.3),
        createFruit("樱桃", "cherry", 8.5, 3.5, "脆嫩", "红色", "小", 7.8, 82, "spring", 4.8),
        createFruit("芒果", "mango", 9.0, 2.0, "柔软", "黄色", "中等", 8.7, 84, "summer", 4.7),
        createFruit("菠萝", "pineapple", 7.5, 4.0, "脆嫩", "黄色", "大", 8.5, 86, "summer", 4.5),
        createFruit("猕猴桃", "kiwi", 5.5, 4.5, "柔软", "绿色", "小", 9.2, 83, "autumn", 4.4)
    );
    
    // 用户行为记录
    private final Map<String, List<Map<String, Object>>> userBehaviors = new HashMap<>();
    
    /**
     * 获取用户推荐
     */
    public List<Map<String, Object>> getRecommendations(Map<String, Object> userContext, int limit) {
        String userId = (String) userContext.get("userId");
        String season = (String) userContext.get("season");
        String weather = (String) userContext.get("weather");
        
        log.info("为用户 {} 生成推荐，季节: {}, 天气: {}", userId, season, weather);
        
        // 获取用户历史行为
        List<Map<String, Object>> userHistory = userBehaviors.getOrDefault(userId, new ArrayList<>());
        
        // 计算推荐分数
        List<Map<String, Object>> scoredFruits = FRUIT_DATABASE.stream()
            .map(fruit -> {
                Map<String, Object> scoredFruit = new HashMap<>(fruit);
                double score = calculateRecommendationScore(fruit, userContext, userHistory);
                scoredFruit.put("recommendationScore", score);
                return scoredFruit;
            })
            .sorted((f1, f2) -> Double.compare(
                (Double) f2.get("recommendationScore"),
                (Double) f1.get("recommendationScore")
            ))
            .limit(limit)
            .collect(Collectors.toList());
        
        return scoredFruits;
    }
    
    /**
     * 计算推荐分数
     */
    private double calculateRecommendationScore(Map<String, Object> fruit, 
                                              Map<String, Object> userContext, 
                                              List<Map<String, Object>> userHistory) {
        double score = (Double) fruit.get("popularity");
        
        // 季节性加权
        String fruitSeason = (String) fruit.get("season");
        String currentSeason = (String) userContext.get("season");
        if ("all".equals(fruitSeason) || fruitSeason.equals(currentSeason)) {
            score += 1.0;
        }
        
        // 天气加权
        String weather = (String) userContext.get("weather");
        if ("hot".equals(weather) && (Double) fruit.get("waterContent") > 85) {
            score += 0.8; // 热天推荐高水分水果
        }
        
        // 用户历史偏好加权
        String fruitName = (String) fruit.get("name");
        long userPreferenceCount = userHistory.stream()
            .filter(behavior -> fruitName.equals(behavior.get("fruitName")))
            .filter(behavior -> "purchase".equals(behavior.get("behaviorType")) || 
                              "like".equals(behavior.get("behaviorType")))
            .count();
        
        score += userPreferenceCount * 0.5;
        
        // 营养评分加权
        score += (Double) fruit.get("nutritionScore") * 0.1;
        
        return score;
    }
    
    /**
     * 获取热门水果
     */
    public List<Map<String, Object>> getPopularFruits(int limit) {
        return FRUIT_DATABASE.stream()
            .sorted((f1, f2) -> Double.compare(
                (Double) f2.get("popularity"),
                (Double) f1.get("popularity")
            ))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * 获取季节性水果
     */
    public List<Map<String, Object>> getSeasonalFruits(String season) {
        return FRUIT_DATABASE.stream()
            .filter(fruit -> {
                String fruitSeason = (String) fruit.get("season");
                return "all".equals(fruitSeason) || season.equals(fruitSeason);
            })
            .sorted((f1, f2) -> Double.compare(
                (Double) f2.get("popularity"),
                (Double) f1.get("popularity")
            ))
            .collect(Collectors.toList());
    }
    
    /**
     * 记录用户行为
     */
    public void recordUserBehavior(Map<String, Object> behaviorData) {
        String userId = (String) behaviorData.get("userId");
        
        if (userId != null) {
            userBehaviors.computeIfAbsent(userId, k -> new ArrayList<>()).add(behaviorData);
            log.info("记录用户 {} 的行为: {}", userId, behaviorData.get("behaviorType"));
        }
    }
    
    /**
     * 获取水果详细信息
     */
    public Map<String, Object> getFruitDetail(String fruitName) {
        return FRUIT_DATABASE.stream()
            .filter(fruit -> fruitName.equals(fruit.get("name")) || 
                           fruitName.equals(fruit.get("englishName")))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * 获取推荐统计信息
     */
    public Map<String, Object> getRecommendationStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalFruits", FRUIT_DATABASE.size());
        stats.put("totalUsers", userBehaviors.size());
        stats.put("totalBehaviors", userBehaviors.values().stream()
            .mapToInt(List::size).sum());
        
        // 最受欢迎的水果
        Optional<Map<String, Object>> mostPopular = FRUIT_DATABASE.stream()
            .max(Comparator.comparing(fruit -> (Double) fruit.get("popularity")));
        
        if (mostPopular.isPresent()) {
            stats.put("mostPopularFruit", mostPopular.get().get("name"));
        }
        
        // 季节分布
        Map<String, Long> seasonDistribution = FRUIT_DATABASE.stream()
            .collect(Collectors.groupingBy(
                fruit -> (String) fruit.get("season"),
                Collectors.counting()
            ));
        stats.put("seasonDistribution", seasonDistribution);
        
        return stats;
    }
    
    /**
     * 创建水果对象
     */
    private static Map<String, Object> createFruit(String name, String englishName, 
                                                  double sweetness, double acidity, 
                                                  String texture, String color, String size, 
                                                  double nutritionScore, double waterContent, 
                                                  String season, double popularity) {
        Map<String, Object> fruit = new HashMap<>();
        fruit.put("name", name);
        fruit.put("englishName", englishName);
        fruit.put("sweetness", sweetness);
        fruit.put("acidity", acidity);
        fruit.put("texture", texture);
        fruit.put("color", color);
        fruit.put("size", size);
        fruit.put("nutritionScore", nutritionScore);
        fruit.put("waterContent", waterContent);
        fruit.put("season", season);
        fruit.put("popularity", popularity);
        fruit.put("price", Math.round((popularity * 2 + nutritionScore) * 100) / 100.0); // 模拟价格
        return fruit;
    }
}