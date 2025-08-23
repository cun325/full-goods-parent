package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.api.vo.FruitVO;
import org.example.common.entity.Fruit;
import org.example.common.entity.FlashSale;

import java.util.List;
import java.util.Set;

/**
 * 水果Mapper接口
 */
@Mapper
public interface FruitMapper {

    /**
     * 查询所有水果
     *
     * @return 水果列表
     */
    List<Fruit> selectAll();

    /**
     * 分页查询水果列表
     *
     * @return 水果列表
     */
    List<Fruit> selectAllWithPage();

    /**
     * 根据条件查询水果列表
     *
     * @param search 搜索关键词
     * @param category 分类
     * @param status 状态
     * @return 水果列表
     */
    List<Fruit> selectByConditions(@Param("search") String search, @Param("category") String category, @Param("status") Integer status);

    /**
     * 根据条件分页查询水果列表
     *
     * @param search 搜索关键词
     * @param category 分类
     * @param status 状态
     * @return 水果列表
     */
    List<Fruit> selectByConditionsWithPage(@Param("search") String search, @Param("category") String category, @Param("status") Integer status);

    /**
     * 根据ID查询水果
     *
     * @param id 水果ID
     * @return 水果信息
     */
    Fruit selectById(@Param("id") Long id);

    /**
     * 根据分类查询水果
     *
     * @param category 分类
     * @return 水果列表
     */
    List<Fruit> selectByCategory(@Param("category") String category);
    
    /**
     * 根据分类ID查询水果
     *
     * @param categoryId 分类ID
     * @return 水果列表
     */
    List<Fruit> selectByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据指定字段和关键词查询水果
     *
     * @param field 字段名
     * @param keyword 关键词
     * @return 水果列表
     */
    List<Fruit> selectByKeyword(@Param("field") String field, @Param("keyword") String keyword);
    
    /**
     * 根据关键词搜索水果（全字段）
     * 搜索范围包括：名称、描述、产地、分类、口感、营养成分、适合人群
     *
     * @param keyword 关键词
     * @return 水果列表
     */
    List<Fruit> selectByGeneralKeyword(@Param("keyword") String keyword);
    
    /**
     * 根据关键词列表搜索水果
     *
     * @param keywords 关键词列表
     * @return 水果列表
     */
    List<Fruit> selectByKeywords(@Param("keywords") List<String> keywords);

    /**
     * 根据水果名称查询
     *
     * @param name 水果名称
     * @return 水果信息
     */
    @Select("SELECT * FROM fruit WHERE name = #{name} AND status = 1")
    Fruit selectByName(@Param("name") String name);

    /**
     * 新增水果
     *
     * @param fruit 水果信息
     * @return 影响行数
     */
    int insert(Fruit fruit);

    /**
     * 更新水果
     *
     * @param fruit 水果信息
     * @return 影响行数
     */
    int update(Fruit fruit);

    /**
     * 更新水果图片URL
     *
     * @param id 水果ID
     * @param imageUrl 图片URL
     * @return 影响行数
     */
    int updateImageUrl(@Param("id") Long id, @Param("imageUrl") String imageUrl);

    /**
     * 删除水果
     *
     * @param id 水果ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 扣减库存
     *
     * @param id 水果ID
     * @param quantity 扣减数量
     * @return 影响行数
     */
    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 增加库存
     *
     * @param id 水果ID
     * @param quantity 增加数量
     * @return 影响行数
     */
    int increaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);
    
    // ==================== Admin统计相关方法 ====================
    
    /**
     * 统计商品总数
     *
     * @return 商品总数
     */
    Long countTotal();
    
    /**
     * 根据状态统计商品数
     *
     * @param status 商品状态
     * @return 商品数量
     */
    Long countByStatus(@Param("status") Integer status);
    
    /**
     * 统计库存不足的商品数
     *
     * @param threshold 库存阈值
     * @return 库存不足商品数
     */
    Long countLowStock(@Param("threshold") Integer threshold);
    
    /**
      * 统计今日新增商品数
      *
      * @return 今日新增商品数
      */
     Long countTodayAdded();
     
     /**
      * 获取最新商品列表
      *
      * @param limit 限制数量
      * @return 最新商品列表
      */
     List<Fruit> selectNewFruits(@Param("limit") Integer limit);
     
     /**
      * 更新商品推荐状态
      *
      * @param id 商品ID
      * @param recommended 推荐状态
      * @return 影响行数
      */
     int updateRecommended(@Param("id") Long id, @Param("recommended") Integer recommended);
     
     /**
      * 批量更新商品推荐状态
      *
      * @param ids 商品ID列表
      * @param recommended 推荐状态
      * @return 影响行数
      */
     int batchUpdateRecommended(@Param("ids") List<Long> ids, @Param("recommended") Integer recommended);
     
     /**
      * 为商品设置限时特惠
      *
      * @param fruitId 商品ID
      * @param flashSale 限时特惠信息
      * @return 影响行数
      */
     int insertFlashSale(@Param("fruitId") Long fruitId, @Param("flashSale") FlashSale flashSale);
     
     /**
      * 更新商品的限时特惠信息
      *
      * @param fruitId 商品ID
      * @param flashSale 限时特惠信息
      * @return 影响行数
      */
     int updateFlashSale(@Param("fruitId") Long fruitId, @Param("flashSale") FlashSale flashSale);
     
     /**
      * 删除商品的限时特惠
      *
      * @param fruitId 商品ID
      * @return 影响行数
      */
     int deleteFlashSaleByFruitId(@Param("fruitId") Long fruitId);
     
     /**
      * 查询商品列表（包含限时特惠信息）
      *
      * @param search 搜索关键词
      * @param category 分类
      * @param status 状态
      * @return 商品列表（包含限时特惠信息）
      */
     List<FruitVO> selectWithFlashSaleInfo(@Param("search") String search, @Param("category") String category, @Param("status") Integer status);
     
     /**
      * 统计各分类的水果数量
      *
      * @return 分类统计列表
      */
     List<CategoryCount> selectCategoryCounts();
     
     class CategoryCount {
        private String category;
        private int count;
        
        // getters and setters
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public int getCount() {
            return count;
        }
        
        public void setCount(int count) {
            this.count = count;
        }
    }
}