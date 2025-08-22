package org.example.admin.controller;

import org.example.admin.service.AdminOrderService;
import org.example.admin.service.AdminProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/admin/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);
    
    @Autowired
    private AdminOrderService adminOrderService;
    
    @Autowired
    private AdminProductService adminProductService;

    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 获取订单统计数据
            Map<String, Object> orderStats = adminOrderService.getOrderStatistics();
            
            // 获取商品统计数据
            Map<String, Object> productStats = adminProductService.getProductStatistics();
            
            // 今日销售额
            BigDecimal todayRevenue = adminOrderService.getTodayRevenue();
            statistics.put("todayRevenue", todayRevenue);
            
            // 订单总量
            Object totalOrders = orderStats.get("totalOrders");
            statistics.put("totalOrders", totalOrders != null ? totalOrders : 0);
            
            // 待处理订单 (待支付)
            Object pendingOrders = orderStats.get("pendingOrders");
            statistics.put("pendingOrders", pendingOrders != null ? pendingOrders : 0);
            
            // 库存不足商品
            Long lowStockProducts = adminProductService.getLowStockCount();
            statistics.put("lowStockProducts", lowStockProducts);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取统计数据成功");
            result.put("data", statistics);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("获取仪表盘统计数据失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "获取统计数据失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 获取销售趋势数据
     */
    @GetMapping("/sales-trend")
    public ResponseEntity<Map<String, Object>> getSalesTrend(@RequestParam(defaultValue = "7") int days) {
        try {
            Map<String, Object> trendData = adminOrderService.getSalesTrend(days);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取销售趋势成功");
            result.put("data", trendData);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("获取销售趋势失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "获取销售趋势失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 获取用户统计数据
     */
    @GetMapping("/user-statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        try {
            // 模拟用户统计数据
            Map<String, Object> userStats = new HashMap<>();
            userStats.put("totalUsers", 1234);
            userStats.put("newUsersToday", 25);
            userStats.put("activeUsers", 867);
            userStats.put("userGrowthRate", 2.5);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取用户统计数据成功");
            result.put("data", userStats);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("获取用户统计数据失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "获取用户统计数据失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 获取商品统计数据
     */
    @GetMapping("/product-statistics")
    public ResponseEntity<Map<String, Object>> getProductStatistics() {
        try {
            Map<String, Object> productStats = adminProductService.getProductStatistics();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取商品统计数据成功");
            result.put("data", productStats);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("获取商品统计数据失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "获取商品统计数据失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 获取推广效果数据
     */
    @GetMapping("/promotion-effect")
    public ResponseEntity<Map<String, Object>> getPromotionEffect() {
        try {
            // 模拟推广效果数据
            Map<String, Object> promotionData = new HashMap<>();
            
            List<String> channels = Arrays.asList("搜索引擎", "社交媒体", "邮件营销", "直接访问", "推荐链接");
            List<Integer> orders = Arrays.asList(120, 200, 150, 80, 70);
            List<Integer> revenue = Arrays.asList(12000, 25000, 18000, 9500, 8200);
            
            promotionData.put("channels", channels);
            promotionData.put("orders", orders);
            promotionData.put("revenue", revenue);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取推广效果数据成功");
            result.put("data", promotionData);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("获取推广效果数据失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "获取推广效果数据失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }
}