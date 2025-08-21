package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.Cart;

import java.util.List;

/**
 * 购物车Mapper接口
 */
@Mapper
public interface CartMapper {

    /**
     * 根据用户ID查询购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<Cart> selectByUserId(Long userId);

    /**
     * 根据ID查询购物车
     *
     * @param id 购物车ID
     * @return 购物车信息
     */
    Cart selectById(Long id);

    /**
     * 根据用户ID和水果ID查询购物车
     *
     * @param userId  用户ID
     * @param fruitId 水果ID
     * @return 购物车信息
     */
    Cart selectByUserIdAndFruitId(@Param("userId") Long userId, @Param("fruitId") Long fruitId);

    /**
     * 新增购物车
     *
     * @param cart 购物车信息
     * @return 影响行数
     */
    int insert(Cart cart);

    /**
     * 修改购物车
     *
     * @param cart 购物车信息
     * @return 影响行数
     */
    int update(Cart cart);

    /**
     * 删除购物车
     *
     * @param id 购物车ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据用户ID和水果ID删除购物车
     *
     * @param userId  用户ID
     * @param fruitId 水果ID
     * @return 影响行数
     */
    int deleteByUserIdAndFruitId(@Param("userId") Long userId, @Param("fruitId") Long fruitId);

    /**
     * 清空用户购物车
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(Long userId);
}