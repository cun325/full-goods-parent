package org.example.api.service.impl;

import org.example.api.service.CartService;
import org.example.common.entity.Cart;
import org.example.api.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 购物车服务实现类
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<Cart> getCartList(Long userId) {
        return cartMapper.selectByUserId(userId);
    }

    @Override
    public boolean addToCart(Long userId, Long fruitId, Integer quantity) {
        // 查询购物车中是否已存在该商品
        Cart existCart = cartMapper.selectByUserIdAndFruitId(userId, fruitId);
        if (existCart != null) {
            // 已存在，更新数量
            existCart.setQuantity(existCart.getQuantity() + quantity);
            existCart.setUpdateTime(new Date());
            return cartMapper.update(existCart) > 0;
        } else {
            // 不存在，新增
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setFruitId(fruitId);
            cart.setQuantity(quantity);
            cart.setCreateTime(new Date());
            cart.setUpdateTime(new Date());
            return cartMapper.insert(cart) > 0;
        }
    }

    @Override
    public boolean updateQuantity(Long userId, Long fruitId, Integer quantity) {
        Cart cart = cartMapper.selectByUserIdAndFruitId(userId, fruitId);
        if (cart != null) {
            cart.setQuantity(quantity);
            cart.setUpdateTime(new Date());
            return cartMapper.update(cart) > 0;
        }
        return false;
    }

    @Override
    public boolean removeFromCart(Long userId, Long fruitId) {
        return cartMapper.deleteByUserIdAndFruitId(userId, fruitId) > 0;
    }

    @Override
    public boolean clearCart(Long userId) {
        return cartMapper.deleteByUserId(userId) > 0;
    }
}