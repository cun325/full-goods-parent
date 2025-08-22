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
        // 解析推荐条件
        List<String> keywords = nlpService.extractKeywords(recommendDTO.getCondition());
        log.info("解析关键词: {}", keywords);
        
        // 根据关键词查询水果
        List<Fruit> recommendList = fruitMapper.selectByKeywords(keywords);
        log.info("推荐水果数量: {}", recommendList.size());
        
        int limit = recommendDTO.getLimit() != null ? recommendDTO.getLimit() : 5;
        
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
        List<RecommendHistory> historyList = recommendHistoryMapper.selectByUserId(userId, limit);
        return historyList.stream()
                .map(this::convertToHistoryVO)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为历史记录VO
     */
    private RecommendHistoryVO convertToHistoryVO(RecommendHistory history) {
        RecommendHistoryVO vo = new RecommendHistoryVO();
        vo.setId(history.getId());
        vo.setCondition(history.getCondition());
        vo.setCreateTime(history.getCreateTime());
        
        // 计算时间描述
        vo.setTimeDesc(getTimeDesc(history.getCreateTime()));
        
        return vo;
    }
    
    /**
     * 获取时间描述，如：10分钟前
     */
    private String getTimeDesc(Date date) {
        if (date == null) {
            return "";
        }
        
        long diff = System.currentTimeMillis() - date.getTime();
        long minutes = diff / (60 * 1000);
        
        if (minutes < 1) {
            return "刚刚";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (minutes < 24 * 60) {
            return (minutes / 60) + "小时前";
        } else if (minutes < 30 * 24 * 60) {
            return (minutes / (24 * 60)) + "天前";
        } else {
            return "很久以前";
        }
    }
    
    @Override
    @Cacheable(value = "recommendCache", key = "'recommend:' + #limit")
    public List<Fruit> getRecommendFruits(Integer limit) {
        return getRecommendFruits(limit, true);
    }
    
    @Override
    @Cacheable(value = "recommendCache", key = "'recommend:' + #limit + ':' + #isRecommended")
    public List<Fruit> getRecommendFruits(Integer limit, Boolean isRecommended) {
        if (limit == null || limit <= 0) {
            limit = 6;
        }
        
        List<Fruit> allFruits;
        if (isRecommended != null && isRecommended) {
            // 只获取推荐状态为1的商品
            allFruits = fruitMapper.selectAll().stream()
                    .filter(fruit -> fruit.getRecommended() != null && fruit.getRecommended() == 1)
                    .collect(Collectors.toList());
        } else {
            // 获取所有商品
            allFruits = fruitMapper.selectAll();
        }
        
        List<Fruit> recommendFruits;
        if (allFruits.size() > limit) {
            // 创建新的ArrayList避免Redis序列化问题
            recommendFruits = new ArrayList<>(allFruits.subList(0, limit));
        } else {
            recommendFruits = allFruits;
        }
        
        // 处理图片URL
        for (Fruit fruit : recommendFruits) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        
        return recommendFruits;
    }
    
    @Override
    @Cacheable(value = "recommendCache", key = "'new:' + #limit")
    public List<Fruit> getNewFruits(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 6;
        }
        
        // 获取新品上架
        List<Fruit> newFruits = fruitMapper.selectNewFruits(limit);
        // 处理图片URL
        for (Fruit fruit : newFruits) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        return newFruits;
    }
    
    @Override
 //   @Cacheable(value = "fruitCache", key = "'categories'")
    public List<CategoryVO> getCategories() {
        // 从数据库获取所有启用的分类
        List<CategoryVO> categories = fruitCategoryMapper.selectAllEnabled();
        log.info("获取到{}个启用的水果分类", categories);
        return categories;
    }
    
    @Override
    public List<Fruit> getFruitsByCategory(String category) {
        if (StringUtils.isEmpty(category)) {
            return Collections.emptyList();
        }

        List<Fruit> fruits = fruitMapper.selectByCategory(category);
        // 处理图片URL
        for (Fruit fruit : fruits) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        return fruits;
    }
    
    @Override
    public List<Fruit> getFruitsByCategoryId(Long categoryId) {
        if (categoryId == null) {
            return Collections.emptyList();
        }

        List<Fruit> fruits = fruitMapper.selectByCategoryId(categoryId);
        // 处理图片URL
        for (Fruit fruit : fruits) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        return fruits;
    }
    
    @Override
    public List<Fruit> searchByConditions(String search, String category, Integer status) {
        List<Fruit> fruits = fruitMapper.selectByConditions(search, category, status);
        // 处理图片URL
        for (Fruit fruit : fruits) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        return fruits;
    }
    
    @Override
    public List<Fruit> searchFruits(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return Collections.emptyList();
        }
        
        List<Fruit> fruits = fruitMapper.selectByGeneralKeyword(keyword);
        // 处理图片URL
        for (Fruit fruit : fruits) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        return fruits;
    }
    
    /**
     * 转换为水果VO
     */
    private FruitVO convertToFruitVO(Fruit fruit) {
        FruitVO vo = new FruitVO();
        BeanUtils.copyProperties(fruit, vo);
        // 为本地图片URL添加/api前缀
        vo.setImageUrl(processImageUrl(vo.getImageUrl()));
        return vo;
    }
    
    /**
     * 处理图片URL，为本地资源添加/images前缀以使用图片代理服务
     * @param imageUrl 原始图片URL
     * @return 处理后的图片URL
     */
    private String processImageUrl(String imageUrl) {
        if (!org.apache.commons.lang3.StringUtils.isNotBlank(imageUrl)) {
            return imageUrl;
        }
        
        // 如果是外部链接（http或https开头），不添加前缀
        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            return imageUrl;
        }
        
        // 如果已经是/images开头，不重复添加
        if (imageUrl.startsWith("/images/")) {
            return imageUrl;
        }
        
        // 为本地资源添加/images前缀，通过图片代理服务访问
        if (imageUrl.startsWith("/")) {
            return "/images?url=" + imageUrl;
        } else {
            return "/images?url=/" + imageUrl;
        }
    }
    
    @Override
    public List<Fruit> getFruitsByCategoryWithSort(String category, String sortBy, String sortOrder) {
        List<Fruit> fruits = fruitMapper.selectByCategory(category);
        // 处理图片URL
        for (Fruit fruit : fruits) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        return sortFruits(fruits, sortBy, sortOrder);
    }
    
    @Override
    public List<Fruit> getFruitsByCategoryIdWithSort(Long categoryId, String sortBy, String sortOrder) {
        List<Fruit> fruits = fruitMapper.selectByCategoryId(categoryId);
        // 处理图片URL
        for (Fruit fruit : fruits) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        return sortFruits(fruits, sortBy, sortOrder);
    }
    
    @Override
    public List<Fruit> searchFruitsWithSort(String keyword, String sortBy, String sortOrder) {
        if (StringUtils.isEmpty(keyword)) {
            return Collections.emptyList();
        }
        
        List<Fruit> fruits = fruitMapper.selectByGeneralKeyword(keyword);
        // 处理图片URL
        for (Fruit fruit : fruits) {
            fruit.setImageUrl(processImageUrl(fruit.getImageUrl()));
        }
        return sortFruits(fruits, sortBy, sortOrder);
    }
    
    /**
     * 对水果列表进行排序
     * @param fruits 水果列表
     * @param sortBy 排序字段：price(价格)、stock(库存)、createTime(时间)
     * @param sortOrder 排序方式：asc(升序)、desc(降序)
     * @return 排序后的水果列表
     */
    private List<Fruit> sortFruits(List<Fruit> fruits, String sortBy, String sortOrder) {
        if (fruits == null || fruits.isEmpty()) {
            return fruits;
        }
        
        Comparator<Fruit> comparator;
        
        switch (sortBy) {
            case "price":
                comparator = Comparator.comparing(Fruit::getPrice, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "stock":
                comparator = Comparator.comparing(Fruit::getStock, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "createTime":
            default:
                comparator = Comparator.comparing(Fruit::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
        }
        
        // 如果是降序，反转比较器
        if ("asc".equalsIgnoreCase(sortOrder)) {
            fruits.sort(comparator);
        } else {
            fruits.sort(comparator.reversed());
        }
        
        return fruits;
    }
    
}
