package org.example.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.example.api.dto.FruitRecommendDTO;
import org.example.common.entity.FruitCategory;
import org.example.common.entity.RecommendHistory;
import org.example.api.mapper.FruitMapper;
import org.example.api.mapper.FruitCategoryMapper;
import org.example.api.mapper.RecommendHistoryMapper;
import org.example.api.service.BannerService;
import org.example.api.service.FruitService;
import org.example.api.service.NLPService;
import org.example.api.service.UserService;
import org.example.api.service.ModelPredictionService;
import org.example.api.vo.BannerVO;
import org.example.api.vo.CategoryVO;
import org.example.api.vo.FruitVO;
import org.example.api.vo.HomeVO;
import org.example.api.vo.RecommendHistoryVO;
import org.example.common.entity.Fruit;
import org.example.common.entity.User;
import org.example.common.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 水果服务实现类
 */
@Slf4j
@Service
public class FruitServiceImpl implements FruitService {

    @Autowired
    private FruitMapper fruitMapper;
    
    @Autowired
    private FruitCategoryMapper fruitCategoryMapper;
    
    @Autowired
    private RecommendHistoryMapper recommendHistoryMapper;
    
    @Autowired
    private NLPService nlpService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BannerService bannerService;
    
    @Autowired
    private ModelPredictionService modelPredictionService;

    @Value("${server.servlet.context-path:}")
    private String contextPath;
    
    @Value("${server.port:8081}")
    private String serverPort;
    
    @Override
    @Cacheable(value = "fruitCache", key = "'all'")
    public List<Fruit> getAllFruits() {
        List<Fruit> fruits = fruitMapper.selectAll();
        // 处理图片URL
        for (Fruit fruit : fruits) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        return fruits;
    }

    @Override
    @Cacheable(value = "fruitCache", key = "'id:' + #id", unless = "#result == null")
    public Fruit getById(Long id) {
        if (id == null) {
            return null;
        }
        Fruit fruit = fruitMapper.selectById(id);
        if (fruit != null) {
            // 处理图片URL
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        return fruit;
    }

    @Override
    public List<Fruit> recommendFruits(FruitRecommendDTO recommendDTO) {
        // 使用模型预测服务进行推荐
        try {
            // 从token获取用户ID
            Long userId = null;
            if (recommendDTO.getToken() != null && !recommendDTO.getToken().isEmpty()) {
                // 通过UserService根据token获取用户信息
                User user = userService.getByToken(recommendDTO.getToken());
                if (user != null) {
                    userId = user.getId();
                }
            }
            
            // 如果无法获取用户ID，使用默认值
            if (userId == null) {
                userId = 1L;
                log.warn("无法从token获取用户ID，使用默认值: {}", userId);
            }
            
            // 调用模型预测服务
            List<Fruit> recommendList = modelPredictionService.predictFruitRecommendation(
                userId.toString(), recommendDTO.getCondition(), 
                recommendDTO.getLimit() != null ? recommendDTO.getLimit() : 5);
            
            // 保存推荐历史记录
            saveRecommendHistory(userId, recommendDTO.getCondition());
            
            // 处理图片URL
            for (Fruit fruit : recommendList) {
                fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
            }
            
            log.info("推荐水果数量: {}", recommendList.size());
            return recommendList;
        } catch (Exception e) {
            log.error("使用模型进行水果推荐时发生错误", e);
            // 出现异常时回退到基于关键词的推荐
            return fallbackRecommendFruits(recommendDTO);
        }
    }
    
    /**
     * 回退的推荐方法（基于关键词匹配）
     */
    private List<Fruit> fallbackRecommendFruits(FruitRecommendDTO recommendDTO) {
        // 解析推荐条件
        List<String> keywords = nlpService.extractKeywords(recommendDTO.getCondition());
        log.info("解析关键词: {}", keywords);
        
        // 根据关键词查询水果
        List<Fruit> recommendList = fruitMapper.selectByKeywords(keywords);
        log.info("回退推荐水果数量: {}", recommendList.size());
        
        int limit = recommendDTO.getLimit() != null ? recommendDTO.getLimit() : 5;
        if (recommendList.size() > limit) {
            recommendList = recommendList.subList(0, limit);
        }
        
        // 从token获取用户ID
        Long userId = null;
        if (recommendDTO.getToken() != null && !recommendDTO.getToken().isEmpty()) {
            // 通过UserService根据token获取用户信息
            User user = userService.getByToken(recommendDTO.getToken());
            if (user != null) {
                userId = user.getId();
            }
        }
        
        // 如果无法获取用户ID，使用默认值
        if (userId == null) {
            userId = 1L;
            log.warn("无法从token获取用户ID，使用默认值: {}", userId);
        }
        
        // 保存推荐历史记录
        saveRecommendHistory(userId, recommendDTO.getCondition());
        
        // 处理图片URL
        for (Fruit fruit : recommendList) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        
        return recommendList;
    }
    
    /**
     * 保存推荐历史记录
     */
    @Override
    public boolean saveRecommendHistory(Long userId, String condition) {
        RecommendHistory history = new RecommendHistory();
        history.setUserId(userId);
        history.setCondition(condition);
        history.setCreateTime(new Date());
        
        // 保存到数据库
        int result = recommendHistoryMapper.insert(history);
        
        // 限制历史记录数量为20条
        List<RecommendHistory> historyList = recommendHistoryMapper.selectByUserId(userId, 30);
        if (historyList.size() > 20) {
            // 删除多余的记录
            List<Long> idsToDelete = historyList.stream()
                    .skip(20)
                    .map(RecommendHistory::getId)
                    .collect(Collectors.toList());
            
            if (!idsToDelete.isEmpty()) {
                recommendHistoryMapper.deleteByIds(idsToDelete);
            }
        }
        
        return result > 0;
    }
    
    @Override
    public List<RecommendHistoryVO> getRecommendHistory(Long userId, Integer limit) {
        if (userId == null) {
            return Collections.emptyList();
        }
        
        // 默认限制20条记录
        int limitValue = (limit != null && limit > 0) ? limit : 20;
        List<RecommendHistory> historyList = recommendHistoryMapper.selectByUserId(userId, limitValue);
        
        return historyList.stream()
                .map(history -> {
                    RecommendHistoryVO vo = new RecommendHistoryVO();
                    vo.setId(history.getId());
                    vo.setCondition(history.getCondition());
                    vo.setCreateTime(history.getCreateTime());
                    // 格式化时间描述
                    vo.setTimeDesc(formatTimeDesc(history.getCreateTime()));
                    return vo;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Fruit> getRecommendFruits(Integer limit, Boolean isRecommended) {
        int limitValue = (limit != null && limit > 0) ? limit : 6;
        // 使用selectNewFruits方法替代不存在的selectRecommended方法
        return fruitMapper.selectNewFruits(limitValue);
    }
    
    @Override
    public List<Fruit> getNewFruits(Integer limit) {
        int limitValue = (limit != null && limit > 0) ? limit : 6;
        return fruitMapper.selectNewFruits(limitValue);
    }
    
    @Override
    public List<CategoryVO> getCategories() {
        // 使用selectAllEnabled方法替代不存在的selectAll方法
        List<CategoryVO> categories = fruitCategoryMapper.selectAllEnabled();
        List<FruitMapper.CategoryCount> categoryCounts = fruitMapper.selectCategoryCounts();
        
        Map<String, Integer> countMap = categoryCounts.stream()
                .collect(Collectors.toMap(
                    FruitMapper.CategoryCount::getCategory, 
                    FruitMapper.CategoryCount::getCount
                ));
        
        return categories.stream()
                .map(category -> {
                    category.setFruitCount(countMap.getOrDefault(category.getName(), 0));
                    return category;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Fruit> getFruitsByCategory(String category) {
        if (StringUtils.isBlank(category)) {
            return Collections.emptyList();
        }
        return fruitMapper.selectByCategory(category);
    }
    
    @Override
    public List<Fruit> getFruitsByCategoryId(Long categoryId) {
        // 这个方法在Mapper中没有直接实现，暂时返回空列表
        return Collections.emptyList();
    }
    
    @Override
    public List<Fruit> searchByConditions(String search, String category, Integer status) {
        // 实现搜索逻辑
        return fruitMapper.selectAll(); // 简化实现，实际应根据条件查询
    }
    
    @Override
    public List<Fruit> searchFruits(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return Collections.emptyList();
        }
        // 使用selectByGeneralKeyword替代selectByKeyword，因为它接受单个keyword参数
        return fruitMapper.selectByGeneralKeyword(keyword);
    }
    
    @Override
    public List<Fruit> getFruitsByCategoryWithSort(String category, String sortBy, String sortOrder) {
        // 简化实现，实际应根据分类和排序条件查询
        return getFruitsByCategory(category);
    }
    
    @Override
    public List<Fruit> getFruitsByCategoryIdWithSort(Long categoryId, String sortBy, String sortOrder) {
        // 简化实现，实际应根据分类ID和排序条件查询
        return getFruitsByCategoryId(categoryId);
    }
    
    @Override
    public List<Fruit> searchFruitsWithSort(String keyword, String sortBy, String sortOrder) {
        // 简化实现，实际应根据关键词和排序条件查询
        return searchFruits(keyword);
    }
    
    @Override
    public List<Fruit> getRecommendFruits(Integer limit) {
        return getRecommendFruits(limit, true);
    }
    
    /**
     * 格式化时间描述
     */
    private String formatTimeDesc(Date date) {
        if (date == null) {
            return "";
        }
        
        long diff = System.currentTimeMillis() - date.getTime();
        long minutes = diff / (60 * 1000);
        long hours = diff / (60 * 60 * 1000);
        long days = diff / (24 * 60 * 60 * 1000);
        
        if (minutes < 1) {
            return "刚刚";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (hours < 24) {
            return hours + "小时前";
        } else {
            return days + "天前";
        }
    }
    
    /**
     * 处理图片URL
     */
    protected String processImageUrl(String imageUrl) {
        if (StringUtils.isBlank(imageUrl)) {
            return imageUrl;
        }
        
        // 如果已经是完整URL，直接返回
        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            return imageUrl;
        }
        
        // 处理相对路径
        String processedUrl = imageUrl;
        if (imageUrl.startsWith("/")) {
            processedUrl = imageUrl.substring(1);
        }
        
        // 构造完整URL
        String baseUrl = String.format("http://localhost:%s%s", serverPort, contextPath);
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        
        return baseUrl + processedUrl;
    }
}