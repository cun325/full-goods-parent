package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.api.vo.CategoryVO;
import org.example.common.entity.FruitCategory;

import java.util.List;

/**
 * 水果分类Mapper接口
 */
@Mapper
public interface FruitCategoryMapper {

    /**
     * 查询所有启用的分类
     *
     * @return 分类列表
     */
    List<CategoryVO> selectAllEnabled();

    /**
     * 根据ID查询分类
     *
     * @param id 分类ID
     * @return 分类信息
     */
    FruitCategory selectById(@Param("id") Long id);

    /**
     * 根据名称查询分类
     *
     * @param name 分类名称
     * @return 分类信息
     */
    FruitCategory selectByName(@Param("name") String name);

    /**
     * 新增分类
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
     * 删除分类
     *
     * @param id 分类ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}