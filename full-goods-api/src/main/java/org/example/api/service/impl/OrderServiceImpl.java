package org.example.api.service.impl;

import org.example.api.dto.CreateOrderDTO;
import org.example.api.mapper.*;
import org.example.api.service.OrderService;
import org.example.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private FruitMapper fruitMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long userId, Long addressId, List<Long> cartIds) {
        return createOrder(userId, addressId, cartIds, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long userId, Long addressId, List<Long> cartIds, List<CreateOrderDTO.BuyNowItem> buyNowItems) {
        // 获取收货地址
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("收货地址不存在");
        }

        // 获取商品信息
        List<Cart> cartList = new ArrayList<>();
        List<CreateOrderDTO.BuyNowItem> buyNowOrderItems = new ArrayList<>();
        
        // 处理立即购买商品
        if (buyNowItems != null && !buyNowItems.isEmpty()) {
            buyNowOrderItems.addAll(buyNowItems);
        } else {
            // 处理购物车商品
            if (cartIds != null && !cartIds.isEmpty()) {
                for (Long cartId : cartIds) {
                    Cart cart = cartMapper.selectById(cartId);
                    if (cart != null && cart.getUserId().equals(userId)) {
                        cartList.add(cart);
                    }
                }
            } else {
                cartList = cartMapper.selectByUserId(userId);
            }
            
            if (cartList.isEmpty()) {
                throw new RuntimeException("购物车为空");
            }
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setStatus(0); // 待付款
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverDistrict(address.getDistrict());
        order.setReceiverAddress(address.getDetailAddress());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());

        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // 处理立即购买商品
        if (!buyNowOrderItems.isEmpty()) {
            for (CreateOrderDTO.BuyNowItem buyItem : buyNowOrderItems) {
                // 验证商品是否存在
                Fruit fruit = fruitMapper.selectById(buyItem.getFruitId());
                if (fruit == null) {
                    throw new RuntimeException("商品不存在：" + buyItem.getFruitId());
                }

                // 检查库存是否充足
                if (fruit.getStock() < buyItem.getQuantity()) {
                    throw new RuntimeException("商品库存不足：" + fruit.getName() + "，当前库存：" + fruit.getStock());
                }

                // 扣减库存
                int updateResult = fruitMapper.decreaseStock(buyItem.getFruitId(), buyItem.getQuantity());
                if (updateResult == 0) {
                    throw new RuntimeException("库存扣减失败，可能库存不足：" + fruit.getName());
                }

                // 创建订单项
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderNo(order.getOrderNo());
                orderItem.setFruitId(buyItem.getFruitId());
                // 如果BuyNowItem没有提供名称，则使用数据库中的商品名称
                orderItem.setFruitName(buyItem.getName() != null ? buyItem.getName() : fruit.getName());
                // 如果BuyNowItem没有提供图片，则使用数据库中的商品图片
                orderItem.setFruitImage(buyItem.getImageUrl() != null ? buyItem.getImageUrl() : fruit.getImageUrl());
                // 如果BuyNowItem没有提供价格，则使用数据库中的商品价格
                BigDecimal itemPrice = buyItem.getPrice() != null ? buyItem.getPrice() : fruit.getPrice();
                orderItem.setPrice(itemPrice);
                orderItem.setQuantity(buyItem.getQuantity());
                orderItem.setTotalPrice(itemPrice.multiply(new BigDecimal(buyItem.getQuantity())));
                orderItem.setCreateTime(new Date());
                orderItem.setUpdateTime(new Date());

                orderItems.add(orderItem);
                totalAmount = totalAmount.add(orderItem.getTotalPrice());
            }
        } else {
            // 处理购物车商品
            for (Cart cart : cartList) {
                Fruit fruit = fruitMapper.selectById(cart.getFruitId());
                if (fruit == null) {
                    continue; // 跳过不存在的商品
                }

                // 检查库存是否充足
                if (fruit.getStock() < cart.getQuantity()) {
                    throw new RuntimeException("商品库存不足：" + fruit.getName() + "，当前库存：" + fruit.getStock());
                }

                // 扣减库存
                int updateResult = fruitMapper.decreaseStock(cart.getFruitId(), cart.getQuantity());
                if (updateResult == 0) {
                    throw new RuntimeException("库存扣减失败，可能库存不足：" + fruit.getName());
                }

                // 创建订单项
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderNo(order.getOrderNo());
                orderItem.setFruitId(fruit.getId());
                orderItem.setFruitName(fruit.getName());
                orderItem.setFruitImage(fruit.getImageUrl());
                orderItem.setPrice(fruit.getPrice());
                orderItem.setQuantity(cart.getQuantity());
                orderItem.setTotalPrice(fruit.getPrice().multiply(new BigDecimal(cart.getQuantity())));
                orderItem.setCreateTime(new Date());
                orderItem.setUpdateTime(new Date());

                orderItems.add(orderItem);
                totalAmount = totalAmount.add(orderItem.getTotalPrice());
            }
        }

        // 设置订单金额
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount); // 实付金额，可以减去优惠金额
        order.setFreightAmount(BigDecimal.ZERO); // 运费，可以根据实际情况计算
        order.setDiscountAmount(BigDecimal.ZERO); // 优惠金额，可以根据实际情况计算

        // 保存订单
        orderMapper.insert(order);

        // 保存订单项
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(order.getId());
            orderItemMapper.insert(orderItem);
        }

        // 清空购物车
        for (Cart cart : cartList) {
            cartMapper.deleteById(cart.getId());
        }

        return order;
    }

    @Override
    public List<Order> getOrderList(Long userId) {
        return orderMapper.selectByUserId(userId);
    }

    @Override
    public List<Order> getOrderListByStatus(Long userId, Integer status) {
        if (status == null) {
            return orderMapper.selectByUserId(userId);
        } else {
            return orderMapper.selectByUserIdAndStatus(userId, status);
        }
    }

    @Override
    public Order getOrderDetail(Long orderId) {
        return orderMapper.selectById(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }

        if (order.getStatus() != 0 && order.getStatus() != 1) { // 待付款和待发货的订单可以取消
            return false;
        }

        // 获取订单项，回退库存
        List<OrderItem> orderItems = orderItemMapper.selectByOrderId(orderId);
        for (OrderItem orderItem : orderItems) {
            // 回退库存
            fruitMapper.increaseStock(orderItem.getFruitId(), orderItem.getQuantity());
        }

        order.setStatus(4); // 已取消
        order.setUpdateTime(new Date());
        return orderMapper.update(order) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(Long orderId, Long userId, Integer payType) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }

        if (order.getStatus() != 0) { // 只有待付款的订单可以支付
            return false;
        }

        order.setStatus(1); // 待发货
        order.setPayType(payType);
        order.setPayTime(new Date());
        order.setUpdateTime(new Date());
        return orderMapper.update(order) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReceive(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }

        if (order.getStatus() != 2) { // 只有待收货的订单可以确认收货
            return false;
        }

        order.setStatus(3); // 已完成
        order.setReceiveTime(new Date());
        order.setUpdateTime(new Date());
        return orderMapper.update(order) > 0;
    }

    /**
     * 生成订单编号
     *
     * @return 订单编号
     */
    private String generateOrderNo() {
        return System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6);
    }
}