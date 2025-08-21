package org.example.admin.service;

import org.example.common.entity.FruitCategory;

import java.util.List;
import java.util.Map;

/**
 * 分类管理服务接口
 */
public interface CategoryService {

    /**
     * 获取分类列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param search 搜索关键词
     * @return 分页结果
     */
    Map<String, Object> getCategoryList(int page, int size, String search);

    /**
     * 根据ID获取分类详情
     *
     * @param id 分类ID
     * @return 分类信息
     */
    FruitCategory getCategoryById(Long id);

    /**
     * 添加分类
     *
     * @param category 分类信息
     * @return 新增的分类
     */
    FruitCategory addCategory(FruitCategory category);

    /**
     * 更新分类
     *
     * @param category 分类信息
     * @return 更新后的分类
     */
    FruitCategory updateCategory(FruitCategory category);

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 是否删除成功
     */
    boolean deleteCategory(Long id);

    /**
     * 批量删除分类
     *
     * @param ids 分类ID列表
     * @return 删除成功的数量
     */
    int batchDeleteCategories(List<Long> ids);

    /**
     * 切换分类状态
     *
     * @param id 分类ID
     * @return 更新后的分类
     */
    FruitCategory toggleCategoryStatus(Long id);

    /**
     * 批量切换分类状态
     *
     * @param ids 分类ID列表
     * @return 更新成功的数量
     */
    int batchToggleCategoryStatus(List<Long> ids);
}