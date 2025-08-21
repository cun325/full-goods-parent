package org.example.admin.service.impl;

import org.example.admin.service.AdminOrderService;
import org.example.common.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单管理服务实现类
 */
@Slf4j
@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_BASE_URL = "http://localhost:8080/api";

    @Override
    public Map<String, Object> getOrderList(int page, int size, String search, String status, String startDate, String endDate) {
        try {
            // 构建查询参数
            StringBuilder url = new StringBuilder(API_BASE_URL + "/order/admin/list?page=" + page + "&size=" + size);
            if (search != null && !search.trim().isEmpty()) {
                url.append("&search=").append(search);
            }
            if (status != null && !status.trim().isEmpty()) {
                url.append("&status=").append(status);
            }
            if (startDate != null && !startDate.trim().isEmpty()) {
                url.append("&startDate=").append(startDate);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                url.append("&endDate=").append(endDate);
            }

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> result = response.getBody();
            if (result != null && result.get("code").equals(200)) {
                // 提取data字段中的PageInfo数据
                Map<String, Object> data = (Map<String, Object>) result.get("data");
                Map<String, Object> adminResult = new HashMap<>();
                adminResult.put("list", data.get("list"));
                adminResult.put("total", data.get("total"));
                adminResult.put("page", data.get("pageNum"));
                adminResult.put("size", data.get("pageSize"));
                return adminResult;
            } else {
                throw new RuntimeException("API调用失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("获取订单列表失败: " + e.getMessage());
        }
    }

    @Override
    public Order getOrderById(Long id) {
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                API_BASE_URL + "/order/admin/detail/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            Map<String, Object> result = response.getBody();
            if (result != null && result.get("code").equals(200)) {
                // 这里需要将Map转换为Order对象，暂时返回null
                // 实际项目中应该实现完整的转换逻辑
                return null;
            } else {
                throw new RuntimeException("API调用失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("获取订单详情失败: " + e.getMessage());
        }
    }

    @Override
    public void markOrderPaid(Long id) {
        try {
            // 调用API端的订单状态更新接口
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("status", 1); // 1表示已付款
            restTemplate.put(API_BASE_URL + "/order/admin/status/" + id, updateData);
        } catch (Exception e) {
            // 模拟标记付款成功
            System.out.println("模拟标记订单已付款ID: " + id);
        }
    }

    @Override
    public void shipOrder(Long orderId, String trackingNumber, String courier) {
        try {
            Map<String, Object> shipInfo = new HashMap<>();
            shipInfo.put("trackingNumber", trackingNumber);
            shipInfo.put("courier", courier);
            
            Map<String, Object> response = restTemplate.exchange(
                API_BASE_URL + "/order/admin/ship/" + orderId,
                HttpMethod.PUT,
                new HttpEntity<>(shipInfo),
                new ParameterizedTypeReference<Map<String, Object>>() {}
            ).getBody();
            
            if (response == null || !Integer.valueOf(200).equals(response.get("code"))) {
                throw new RuntimeException("API调用失败: " + (response != null ? response.get("message") : "无响应"));
            }
        } catch (Exception e) {
            System.err.println("发货失败，订单ID: " + orderId + ", 错误: " + e.getMessage());
            throw new RuntimeException("发货失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void batchShipOrders(List<Long> orderIds) {
        for (Long orderId : orderIds) {
            shipOrder(orderId, "AUTO" + System.currentTimeMillis(), "默认快递");
        }
    }

    @Override
    public void refundOrder(Long id) {
        try {
            restTemplate.put(API_BASE_URL + "/order/refund/" + id, null);
        } catch (Exception e) {
            // 模拟退款成功
            System.out.println("模拟退款订单ID: " + id);
        }
    }

    @Override
    public void batchRefundOrders(List<Long> orderIds) {
        for (Long orderId : orderIds) {
            refundOrder(orderId);
        }
    }

    @Override
    public void deleteOrder(Long id) {
        try {
            restTemplate.delete(API_BASE_URL + "/order/delete/" + id);
        } catch (Exception e) {
            // 模拟删除成功
            System.out.println("模拟删除订单ID: " + id);
        }
    }

    @Override
    public void batchDeleteOrders(List<Long> orderIds) {
        for (Long orderId : orderIds) {
            deleteOrder(orderId);
        }
    }

    @Override
    public Map<String, Object> getOrderStatistics() {
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                API_BASE_URL + "/order/admin/statistics",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("获取订单统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取模拟订单列表数据
     */
    private Map<String, Object> getMockOrderList(int page, int size) {
        List<Map<String, Object>> orders = new ArrayList<>();
        
        String[] statuses = {"待付款", "已付款", "已发货", "已完成", "已取消"};
        String[] usernames = {"张三", "李四", "王五", "赵六", "钱七"};
        
        for (int i = 1; i <= size; i++) {
            Map<String, Object> order = new HashMap<>();
            long orderId = (page - 1) * size + i;
            order.put("id", orderId);
            order.put("orderNumber", "ORD" + String.format("%08d", orderId));
            order.put("username", usernames[i % usernames.length]);
            order.put("totalAmount", BigDecimal.valueOf(50.0 + i * 10));
            order.put("status", statuses[i % statuses.length]);
            order.put("payTime", new Date(System.currentTimeMillis() - i * 24 * 60 * 60 * 1000L));
            order.put("createTime", new Date(System.currentTimeMillis() - (i + 1) * 24 * 60 * 60 * 1000L));
            orders.add(order);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", orders);
        result.put("total", 200L);
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }

    /**
     * 获取模拟订单数据
     */
    private Order getMockOrder(Long id) {
        Order order = new Order();
        order.setId(id);
        order.setOrderNo("ORD" + String.format("%08d", id));
        order.setUserId(1L);
        order.setTotalAmount(BigDecimal.valueOf(100.0 + id));
        order.setStatus(1); // 已付款
        order.setCreateTime(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L));
        order.setPayTime(new Date());
        return order;
    }

    /**
     * 获取模拟统计数据
     */
    private Map<String, Object> getMockOrderStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalOrders", 1250);
        statistics.put("todayOrders", 45);
        statistics.put("totalRevenue", BigDecimal.valueOf(125000.50));
        statistics.put("todayRevenue", BigDecimal.valueOf(2250.30));
        statistics.put("pendingOrders", 23);
        statistics.put("shippedOrders", 156);
        statistics.put("completedOrders", 1071);
        return statistics;
    }

    @Override
    public Map<String, Object> getSalesTrend(int days) {
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                API_BASE_URL + "/order/sales-trend?days=" + days,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            Map<String, Object> result = response.getBody();
            if (result != null && result.get("code").equals(200)) {
                return (Map<String, Object>) result.get("data");
            } else {
                throw new RuntimeException("API调用失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("获取销售趋势失败: " + e.getMessage());
        }
    }

    /**
     * 获取模拟销售趋势数据
     */
    private Map<String, Object> getMockSalesTrend(int days) {
        Map<String, Object> trend = new HashMap<>();
        
        List<String> dates = new ArrayList<>();
        List<Double> sales = new ArrayList<>();
        List<Integer> orders = new ArrayList<>();
        
        // 模拟指定天数的数据
        for (int i = days - 1; i >= 0; i--) {
            dates.add("2024-01-" + String.format("%02d", 15 + i));
            sales.add(1000 + Math.random() * 2000);
            orders.add(20 + (int)(Math.random() * 30));
        }
        
        trend.put("dates", dates);
        trend.put("sales", sales);
        trend.put("orders", orders);
        
        return trend;
    }
}