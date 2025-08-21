package org.example.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.api.service.FruitRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 水果推荐控制器
 * 为uniapp端提供水果推荐API
 */
@Slf4j
@RestController
@RequestMapping("/api/fruit")
@CrossOrigin(origins = "*")
public class FruitRecommendationController {

    @Autowired
    private FruitRecommendationService fruitRecommendationService;

    /**
     * 获取用户水果推荐
     */
    @GetMapping("/recommend/{userId}")
    public Map<String, Object> getRecommendations(
            @PathVariable String userId,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) String weather) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 构建用户上下文
            Map<String, Object> userContext = new HashMap<>();
            userContext.put("userId", userId);
            userContext.put("season", season != null ? season : getCurrentSeason());
            userContext.put("weather", weather != null ? weather : "sunny");
            
            // 获取推荐结果
            List<Map<String, Object>> recommendations = fruitRecommendationService
                .getRecommendations(userContext, limit);
            
            result.put("success", true);
            result.put("data", recommendations);
            result.put("total", recommendations.size());
            result.put("message", "获取推荐成功");
            
        } catch (Exception e) {
            log.error("获取水果推荐失败", e);
            result.put("success", false);
            result.put("message", "获取推荐失败: " + e.getMessage());
            result.put("data", Collections.emptyList());
        }
        
        return result;
    }
    
    /**
     * 获取热门水果
     */
    @GetMapping("/popular")
    public Map<String, Object> getPopularFruits(
            @RequestParam(defaultValue = "10") int limit) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> popularFruits = fruitRecommendationService
                .getPopularFruits(limit);
            
            result.put("success", true);
            result.put("data", popularFruits);
            result.put("total", popularFruits.size());
            
        } catch (Exception e) {
            log.error("获取热门水果失败", e);
            result.put("success", false);
            result.put("message", "获取热门水果失败: " + e.getMessage());
            result.put("data", Collections.emptyList());
        }
        
        return result;
    }
    
    /**
     * 获取季节性水果
     */
    @GetMapping("/seasonal")
    public Map<String, Object> getSeasonalFruits(
            @RequestParam(required = false) String season) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String currentSeason = season != null ? season : getCurrentSeason();
            List<Map<String, Object>> seasonalFruits = fruitRecommendationService
                .getSeasonalFruits(currentSeason);
            
            result.put("success", true);
            result.put("data", seasonalFruits);
            result.put("season", currentSeason);
            result.put("total", seasonalFruits.size());
            
        } catch (Exception e) {
            log.error("获取季节性水果失败", e);
            result.put("success", false);
            result.put("message", "获取季节性水果失败: " + e.getMessage());
            result.put("data", Collections.emptyList());
        }
        
        return result;
    }
    
    /**
     * 记录用户行为
     */
    @PostMapping("/behavior")
    public Map<String, Object> recordUserBehavior(@RequestBody Map<String, Object> behaviorData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            fruitRecommendationService.recordUserBehavior(behaviorData);
            
            result.put("success", true);
            result.put("message", "行为记录成功");
            
        } catch (Exception e) {
            log.error("记录用户行为失败", e);
            result.put("success", false);
            result.put("message", "记录行为失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 获取水果详细信息
     */
    @GetMapping("/detail/{fruitName}")
    public Map<String, Object> getFruitDetail(@PathVariable String fruitName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> fruitDetail = fruitRecommendationService
                .getFruitDetail(fruitName);
            
            if (fruitDetail != null) {
                result.put("success", true);
                result.put("data", fruitDetail);
            } else {
                result.put("success", false);
                result.put("message", "未找到水果信息");
            }
            
        } catch (Exception e) {
            log.error("获取水果详情失败", e);
            result.put("success", false);
            result.put("message", "获取水果详情失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 获取推荐统计信息
     */
    @GetMapping("/stats")
    public Map<String, Object> getRecommendationStats() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> stats = fruitRecommendationService.getRecommendationStats();
            
            result.put("success", true);
            result.put("data", stats);
            
        } catch (Exception e) {
            log.error("获取推荐统计失败", e);
            result.put("success", false);
            result.put("message", "获取统计失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 获取当前季节
     */
    private String getCurrentSeason() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH 从0开始
        
        if (month >= 3 && month <= 5) {
            return "spring";
        } else if (month >= 6 && month <= 8) {
            return "summer";
        } else if (month >= 9 && month <= 11) {
            return "autumn";
        } else {
            return "winter";
        }
    }
}