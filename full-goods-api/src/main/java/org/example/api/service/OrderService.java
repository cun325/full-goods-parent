package org.example.api.service;

import org.example.api.dto.CreateOrderDTO;
import org.example.common.entity.Order;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param userId     用户ID
     * @param addressId  收货地址ID
     * @param cartIds    购物车ID列表，为空则下单所有购物车商品
     * @return 订单信息
     */
    Order createOrder(Long userId, Long addressId, List<Long> cartIds);

    /**
     * 创建订单（支持立即购买）
     *
     * @param userId      用户ID
     * @param addressId   收货地址ID
     * @param cartIds     购物车ID列表
     * @param buyNowItems 立即购买商品列表
     * @return 订单信息
     */
    Order createOrder(Long userId, Long addressId, List<Long> cartIds, List<CreateOrderDTO.BuyNowItem> buyNowItems);

    /**
     * 获取用户订单列表
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> getOrderList(Long userId);

    /**
     * 根据状态获取用户订单列表
     *
     * @param userId 用户ID
     * @param status 订单状态（null表示获取所有状态）
     * @return 订单列表
     */
    List<Order> getOrderListByStatus(Long userId, Integer status);

    /**
     * 获取订单详情
     *
     * @param orderId 订单ID
     * @return 订单信息
     */
    Order getOrderDetail(Long orderId);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param userId  用户ID
     * @return 是否成功
     */
    boolean cancelOrder(Long orderId, Long userId);

    /**
     * 支付订单
     *
     * @param orderId 订单ID
     * @param userId  用户ID
     * @param payType 支付方式：1-微信，2-支付宝
     * @return 是否成功
     */
    boolean payOrder(Long orderId, Long userId, Integer payType);

    /**
     * 确认收货
     *
     * @param orderId 订单ID
     * @param userId  用户ID
     * @return 是否成功
     */
    boolean confirmReceive(Long orderId, Long userId);
}