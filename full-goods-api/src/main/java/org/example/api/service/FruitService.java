package org.example.api.service;

import org.example.api.dto.FruitRecommendDTO;
import org.example.api.vo.CategoryVO;
import org.example.api.vo.HomeVO;
import org.example.common.entity.Fruit;
import org.example.api.vo.FruitVO;
import org.example.api.vo.RecommendHistoryVO;

import java.util.List;

/**
 * 水果服务接口
 */
public interface FruitService {

    /**
     * 获取所有水果
     *
     * @return 水果列表
     */
    List<Fruit> getAllFruits();

    /**
     * 根据ID获取水果
     *
     * @param id 水果ID
     * @return 水果信息
     */
    Fruit getById(Long id);

    /**
     * 根据条件推荐水果
     *
     * @param recommendDTO 推荐条件
     * @return 推荐的水果列表
     */
    List<Fruit> recommendFruits(FruitRecommendDTO recommendDTO);

    /**
     * 获取用户的推荐历史记录
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 历史记录列表
     */
    List<RecommendHistoryVO> getRecommendHistory(Long userId, Integer limit);

    /**
     * 保存推荐历史记录
     *
     * @param userId 用户ID
     * @param condition 推荐条件
     * @return 是否保存成功
     */
    boolean saveRecommendHistory(Long userId, String condition);
    
    /**
     * 获取推荐水果列表
     *
     * @param limit 限制数量
     * @return 推荐水果列表
     */
    List<Fruit> getRecommendFruits(Integer limit);
    
    /**
     * 获取推荐水果列表
     *
     * @param limit 限制数量
     * @param isRecommended 是否只返回推荐商品
     * @return 推荐水果列表
     */
    List<Fruit> getRecommendFruits(Integer limit, Boolean isRecommended);
    
    /**
     * 获取新品水果列表
     *
     * @param limit 限制数量
     * @return 新品水果列表
     */
    List<Fruit> getNewFruits(Integer limit);
    
    /**
     * 获取所有水果分类
     *
     * @return 分类列表
     */
    List<CategoryVO> getCategories();
    
    /**
     * 根据分类获取水果列表
     *
     * @param category 分类名称
     * @return 水果列表
     */
    List<Fruit> getFruitsByCategory(String category);
    
    /**
     * 根据分类ID获取水果列表
     *
     * @param categoryId 分类ID
     * @return 水果列表
     */
    List<Fruit> getFruitsByCategoryId(Long categoryId);
    
    /**
     * 根据条件搜索水果
     *
     * @param search 搜索关键词
     * @param category 分类
     * @param status 状态
     * @return 水果列表
     */
    List<Fruit> searchByConditions(String search, String category, Integer status);

    /**
     * 根据关键词搜索水果
     *
     * @param keyword 搜索关键词
     * @return 水果列表
     */
    List<Fruit> searchFruits(String keyword);

    /**
     * 根据分类获取水果列表（带排序）
     *
     * @param category 分类名称
     * @param sortBy 排序字段
     * @param sortOrder 排序方式
     * @return 水果列表
     */
    List<Fruit> getFruitsByCategoryWithSort(String category, String sortBy, String sortOrder);

    /**
     * 根据分类ID获取水果列表（带排序）
     *
     * @param categoryId 分类ID
     * @param sortBy 排序字段
     * @param sortOrder 排序方式
     * @return 水果列表
     */
    List<Fruit> getFruitsByCategoryIdWithSort(Long categoryId, String sortBy, String sortOrder);

    /**
     * 搜索水果（带排序）
     *
     * @param keyword 搜索关键词
     * @param sortBy 排序字段
     * @param sortOrder 排序方式
     * @return 水果列表
     */
    List<Fruit> searchFruitsWithSort(String keyword, String sortBy, String sortOrder);
}