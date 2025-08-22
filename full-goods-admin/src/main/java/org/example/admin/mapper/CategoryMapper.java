package org.example.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.admin.vo.CategoryVo;
import org.example.common.entity.FruitCategory;

import java.util.List;

/**
 * 分类数据访问层
 */
@Mapper
public interface CategoryMapper {

    /**
     * 查询所有分类
     *
     * @return 分类列表
     */
    List<CategoryVo> selectAll();

    /**
     * 根据条件查询分类列表
     *
     * @param search 搜索关键词
     * @return 分类列表
     */
    List<FruitCategory> selectByConditions(@Param("search") String search);

    /**
     * 根据ID查询分类
     *
     * @param id 分类ID
     * @return 分类信息
     */
    FruitCategory selectById(@Param("id") Long id);

    /**
     * 插入分类
     *
     * @param category 分类信息
     * @return 影响行数
     */
    int insert(FruitCategory category);

    /**
     * 更新分类
     *
     * @param category 分类信息
     * @return 影响行数
     */
    int update(FruitCategory category);

    /**
     * 根据ID删除分类
     *
     * @param id 分类ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除分类
     *
     * @param ids 分类ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 更新分类状态
     *
     * @param id 分类ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 批量更新分类状态
     *
     * @param ids 分类ID列表
     * @param status 状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
}