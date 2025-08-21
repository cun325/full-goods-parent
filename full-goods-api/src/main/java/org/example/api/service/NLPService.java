package org.example.api.service;

import java.util.List;
import java.util.Map;

/**
 * NLP服务接口
 */
public interface NLPService {

    /**
     * 分析用户输入的条件，提取关键词和意图
     *
     * @param input 用户输入的条件
     * @return 分析结果，包含关键词和意图
     */
    Map<String, Object> analyzeInput(String input);

    /**
     * 根据用户输入匹配水果特性
     *
     * @param input 用户输入的条件
     * @return 匹配的特性列表
     */
    List<String> matchFruitFeatures(String input);

    /**
     * 提取用户输入中的关键词
     *
     * @param input 用户输入的条件
     * @return 关键词列表
     */
    List<String> extractKeywords(String input);

    /**
     * 分析用户输入的情感倾向
     *
     * @param input 用户输入的条件
     * @return 情感分析结果
     */
    String analyzeSentiment(String input);
}