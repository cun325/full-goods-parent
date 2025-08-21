package org.example.admin.controller;

import org.example.admin.service.AdminOrderService;
import org.example.common.entity.Order;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单管理控制器
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private AdminOrderService adminOrderService;

    /**
     * 获取订单列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getOrderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> result = adminOrderService.getOrderList(page, size, search, status, startDate, endDate);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed("获取订单列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<Order> getOrder(@PathVariable Long id) {
        try {
            Order order = adminOrderService.getOrderById(id);
            if (order != null) {
                return Result.success(order);
            } else {
                return Result.failed("订单不存在");
            }
        } catch (Exception e) {
            return Result.failed("获取订单详情失败: " + e.getMessage());
        }
    }

    /**
     * 标记订单已付款
     */
    @PutMapping("/mark-paid/{id}")
    public Result<String> markOrderPaid(@PathVariable Long id) {
        try {
            adminOrderService.markOrderPaid(id);
            return Result.success("订单已标记为已付款");
        } catch (Exception e) {
            return Result.failed("标记付款失败: " + e.getMessage());
        }
    }

    /**
     * 发货
     */
    @PutMapping("/ship")
    public Result<String> shipOrder(@RequestBody Map<String, Object> shipInfo) {
        try {
            Long orderId = Long.valueOf(shipInfo.get("orderId").toString());
            String trackingNumber = shipInfo.get("trackingNumber").toString();
            String courier = shipInfo.get("courier").toString();
            
            adminOrderService.shipOrder(orderId, trackingNumber, courier);
            return Result.success("订单发货成功");
        } catch (Exception e) {
            return Result.failed("发货失败: " + e.getMessage());
        }
    }

    /**
     * 批量发货
     */
    @PutMapping("/batch-ship")
    public Result<String> batchShipOrders(@RequestBody List<Long> orderIds) {
        try {
            adminOrderService.batchShipOrders(orderIds);
            return Result.success("批量发货成功");
        } catch (Exception e) {
            return Result.failed("批量发货失败: " + e.getMessage());
        }
    }

    /**
     * 退款
     */
    @PutMapping("/refund/{id}")
    public Result<String> refundOrder(@PathVariable Long id) {
        try {
            adminOrderService.refundOrder(id);
            return Result.success("退款成功");
        } catch (Exception e) {
            return Result.failed("退款失败: " + e.getMessage());
        }
    }

    /**
     * 批量退款
     */
    @PutMapping("/batch-refund")
    public Result<String> batchRefundOrders(@RequestBody List<Long> orderIds) {
        try {
            adminOrderService.batchRefundOrders(orderIds);
            return Result.success("批量退款成功");
        } catch (Exception e) {
            return Result.failed("批量退款失败: " + e.getMessage());
        }
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteOrder(@PathVariable Long id) {
        try {
            adminOrderService.deleteOrder(id);
            return Result.success("订单删除成功");
        } catch (Exception e) {
            return Result.failed("删除订单失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除订单
     */
    @DeleteMapping("/batch-delete")
    public Result<String> batchDeleteOrders(@RequestBody List<Long> orderIds) {
        try {
            adminOrderService.batchDeleteOrders(orderIds);
            return Result.success("批量删除成功");
        } catch (Exception e) {
            return Result.failed("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getOrderStatistics() {
        try {
            Map<String, Object> statistics = adminOrderService.getOrderStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.failed("获取统计数据失败: " + e.getMessage());
        }
    }
}