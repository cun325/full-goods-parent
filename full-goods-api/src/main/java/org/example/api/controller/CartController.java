package org.example.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.api.service.CartService;
import org.example.api.service.UserService;
import org.example.api.service.FruitService;
import org.example.api.vo.CartVO;
import org.example.common.entity.Cart;
import org.example.common.entity.User;
import org.example.common.entity.Fruit;
import org.example.common.response.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车控制器
 */
@Slf4j
@RestController
@RequestMapping("/cart")
@Api(tags = "购物车接口")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private FruitService fruitService;

    @PostMapping("/list")
    @ApiOperation(value = "获取购物车列表", notes = "获取用户购物车中的所有商品")
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功"),
        @ApiResponse(code = 401, message = "用户未登录或token已过期")
    })
    public Result<List<CartVO>> getCartList(
            @ApiParam(value = "用户token", required = true) @RequestParam String token) {
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 获取购物车列表
            List<Cart> cartList = cartService.getCartList(user.getId());
            
            // 转换为VO列表
            List<CartVO> cartVOList = cartList.stream().map(cart -> {
                CartVO cartVO = new CartVO();
                BeanUtils.copyProperties(cart, cartVO);
                
                // 查询商品信息并填充到CartVO
                Fruit fruit = fruitService.getById(cart.getFruitId());
                if (fruit != null) {
                    cartVO.setFruitName(fruit.getName());
                    cartVO.setFruitImage(fruit.getImageUrl());
                    cartVO.setFruitPrice(fruit.getPrice());
                }
                
                return cartVO;
            }).collect(Collectors.toList());
            
            return Result.success(cartVOList);
        } catch (Exception e) {
            log.error("获取购物车列表失败", e);
            return Result.failed("获取购物车列表失败");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加商品到购物车", notes = "将指定商品添加到用户购物车")
    @ApiResponses({
        @ApiResponse(code = 200, message = "添加成功"),
        @ApiResponse(code = 400, message = "商品不存在或库存不足"),
        @ApiResponse(code = 401, message = "用户未登录或token已过期")
    })
    public Result<Boolean> addToCart(
            @ApiParam(value = "用户token", required = true) @RequestParam String token,
            @ApiParam(value = "商品ID", required = true) @RequestParam Long fruitId,
            @ApiParam(value = "商品数量", required = true) @RequestParam Integer quantity) {
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 添加到购物车
            boolean success = cartService.addToCart(user.getId(), fruitId, quantity);
            
            return success ? Result.success(true) : Result.failed("添加到购物车失败");
        } catch (Exception e) {
            log.error("添加到购物车失败", e);
            return Result.failed("添加到购物车失败");
        }
    }

    @PostMapping("/update")
    @ApiOperation("更新购物车商品数量")
    public Result<Boolean> updateCartItem(@RequestParam String token, @RequestParam Long fruitId, @RequestParam Integer quantity) {
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 更新购物车商品数量
            boolean success = cartService.updateQuantity(user.getId(), fruitId, quantity);
            
            return success ? Result.success(true) : Result.failed("更新购物车失败");
        } catch (Exception e) {
            log.error("更新购物车失败", e);
            return Result.failed("更新购物车失败");
        }
    }

    @PostMapping("/remove")
    @ApiOperation("删除购物车商品")
    public Result<Boolean> removeFromCart(@RequestParam String token, @RequestParam Long fruitId) {
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 删除购物车商品
            boolean success = cartService.removeFromCart(user.getId(), fruitId);
            
            return success ? Result.success(true) : Result.failed("删除购物车商品失败");
        } catch (Exception e) {
            log.error("删除购物车商品失败", e);
            return Result.failed("删除购物车商品失败");
        }
    }
}