package org.example.api.service;

import org.example.common.entity.Cart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {

    /**
     * 获取用户购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<Cart> getCartList(Long userId);

    /**
     * 添加商品到购物车
     *
     * @param userId  用户ID
     * @param fruitId 水果ID
     * @param quantity 数量
     * @return 是否成功
     */
    boolean addToCart(Long userId, Long fruitId, Integer quantity);

    /**
     * 更新购物车商品数量
     *
     * @param userId  用户ID
     * @param fruitId 水果ID
     * @param quantity 数量
     * @return 是否成功
     */
    boolean updateQuantity(Long userId, Long fruitId, Integer quantity);

    /**
     * 从购物车中删除商品
     *
     * @param userId  用户ID
     * @param fruitId 水果ID
     * @return 是否成功
     */
    boolean removeFromCart(Long userId, Long fruitId);

    /**
     * 清空购物车
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean clearCart(Long userId);
}