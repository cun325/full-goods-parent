package org.example.admin.service;

import org.example.common.entity.Fruit;

import java.util.List;
import java.util.Map;

/**
 * 商品管理服务接口
 */
public interface AdminProductService {

    /**
     * 获取商品列表
     */
    Map<String, Object> getProductList(int page, int size, String search, String category, String status);

    /**
     * 根据ID获取商品
     */
    Fruit getProductById(Long id);

    /**
     * 添加商品
     */
    void addProduct(Fruit fruit);

    /**
     * 更新商品
     */
    void updateProduct(Fruit fruit);

    /**
     * 删除商品
     */
    void deleteProduct(Long id);

    /**
     * 批量删除商品
     */
    void batchDeleteProducts(List<Long> ids);

    /**
     * 切换商品状态
     */
    void toggleProductStatus(Long id);

    /**
     * 批量切换商品状态
     */
    void batchToggleStatus(List<Long> ids);

    /**
     * 获取商品分类列表
     */
    List<String> getCategories();

    /**
     * 获取商品统计数据
     */
    Map<String, Object> getProductStatistics();

    /**
     * 设置商品推荐状态
     */
    void setRecommended(Long id, Boolean recommended);

    /**
     * 批量设置商品推荐状态
     */
    void batchSetRecommended(List<Long> ids, Boolean recommended);

    /**
     * 设置商品限时特惠
     */
    void setFlashSale(Long id, Map<String, Object> flashSaleData);

    /**
     * 取消商品限时特惠
     */
    void cancelFlashSale(Long id);
}