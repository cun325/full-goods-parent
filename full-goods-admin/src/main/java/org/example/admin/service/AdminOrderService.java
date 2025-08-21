package org.example.admin.service;

import org.example.common.entity.Order;

import java.util.List;
import java.util.Map;

/**
 * 订单管理服务接口
 */
public interface AdminOrderService {

    /**
     * 获取订单列表
     */
    Map<String, Object> getOrderList(int page, int size, String search, String status, String startDate, String endDate);

    /**
     * 根据ID获取订单
     */
    Order getOrderById(Long id);

    /**
     * 标记订单已付款
     */
    void markOrderPaid(Long id);

    /**
     * 发货
     */
    void shipOrder(Long orderId, String trackingNumber, String courier);

    /**
     * 批量发货
     */
    void batchShipOrders(List<Long> orderIds);

    /**
     * 退款
     */
    void refundOrder(Long id);

    /**
     * 批量退款
     */
    void batchRefundOrders(List<Long> orderIds);

    /**
     * 删除订单
     */
    void deleteOrder(Long id);

    /**
     * 批量删除订单
     */
    void batchDeleteOrders(List<Long> orderIds);

    /**
     * 获取订单统计数据
     */
    Map<String, Object> getOrderStatistics();

    /**
     * 获取销售趋势数据
     */
    Map<String, Object> getSalesTrend(int days);
}