package org.example.api.service;

import org.example.common.entity.Fruit;

import java.util.List;
import java.util.Map;

/**
 * 模型预测服务接口
 */
public interface ModelPredictionService {

    /**
     * 水果推荐预测
     *
     * @param userId 用户ID
     * @param condition 推荐条件
     * @param limit 返回结果数量限制
     * @return 推荐的水果列表
     */
    List<Fruit> predictFruitRecommendation(String userId, String condition, int limit);

    /**
     * 文本分类预测
     *
     * @param text 待分类文本
     * @return 分类结果
     */
    Map<String, Object> predictTextClassification(String text);
}