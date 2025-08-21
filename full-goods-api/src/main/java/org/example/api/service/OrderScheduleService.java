package org.example.api.service;

import lombok.extern.slf4j.Slf4j;
import org.example.api.mapper.FruitMapper;
import org.example.api.mapper.OrderItemMapper;
import org.example.api.mapper.OrderMapper;
import org.example.common.entity.Order;
import org.example.common.entity.OrderItem;
import org.example.common.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 订单定时任务服务
 */
@Slf4j
@Service
public class OrderScheduleService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private FruitMapper fruitMapper;

    /**
     * 自动取消超时未付款订单
     * 每分钟执行一次，检查待付款订单是否超过15分钟未付款
     */
    @Scheduled(fixedRate = 60000) // 每60秒执行一次
    @Transactional(rollbackFor = Exception.class)
    public void autoCancelUnpaidOrders() {
        long startTime = System.currentTimeMillis();
        int canceledCount = 0;
        
        try {
            // 查询15分钟前创建的待付款订单
            Date fifteenMinutesAgo = new Date(System.currentTimeMillis() - 15 * 60 * 1000);
            List<Order> unpaidOrders = orderMapper.findUnpaidOrdersBeforeTime(fifteenMinutesAgo);
            
            for (Order order : unpaidOrders) {
                try {
                    // 获取订单项，回退库存
                    List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
                    for (OrderItem orderItem : orderItems) {
                        // 回退库存
                        fruitMapper.increaseStock(orderItem.getFruitId(), orderItem.getQuantity());
                        log.debug("回退库存 - 商品ID: {}, 数量: {}", orderItem.getFruitId(), orderItem.getQuantity());
                    }
                    
                    // 更新订单状态为已取消
                    order.setStatus(4); // 已取消
                    order.setUpdateTime(new Date());
                    orderMapper.update(order);
                    
                    canceledCount++;
                    log.info("自动取消超时订单 - 订单号: {}, 用户ID: {}, 金额: {}", 
                            order.getOrderNo(), order.getUserId(), order.getTotalAmount());
                    
                } catch (Exception e) {
                    log.error("取消订单失败 - 订单ID: {}, 错误: {}", order.getId(), e.getMessage(), e);
                }
            }
            
            long duration = System.currentTimeMillis() - startTime;
            LogUtils.scheduledTask("订单自动取消任务", duration, true, 
                    String.format("检查了%d个超时订单，成功取消%d个", unpaidOrders.size(), canceledCount));
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            LogUtils.scheduledTask("订单自动取消任务", duration, false, 
                    "任务执行失败: " + e.getMessage());
            log.error("订单自动取消任务执行失败", e);
        }
    }
}