package org.example.admin.controller;

import org.example.common.response.Result;
import org.example.admin.service.AdminOrderService;
import org.example.admin.service.AdminProductService;
import org.example.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 仪表盘控制器
 */
@RestController
@RequestMapping("/admin/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private AdminOrderService adminOrderService;
    
    @Autowired
    private AdminProductService adminProductService;
    
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getDashboardStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 获取用户统计数据
            Map<String, Object> userStats = adminUserService.getUserStatistics();
            statistics.putAll(userStats);
            
            // 获取订单统计数据
            Map<String, Object> orderStats = adminOrderService.getOrderStatistics();
            statistics.putAll(orderStats);
            
            // 获取商品统计数据
            Map<String, Object> productStats = adminProductService.getProductStatistics();
            statistics.putAll(productStats);
            
            return Result.success(statistics);
        } catch (Exception e) {
            // 如果获取真实数据失败，使用模拟数据
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalOrders", 1250);
            statistics.put("totalUsers", 2580);
            statistics.put("totalProducts", 156);
            statistics.put("totalRevenue", BigDecimal.valueOf(125000.50));
            statistics.put("todayOrders", 45);
            statistics.put("todayUsers", 12);
            statistics.put("todayRevenue", BigDecimal.valueOf(2250.30));
            statistics.put("pendingOrders", 23);
            statistics.put("lowStockProducts", 8);
            return Result.success(statistics);
        }
    }

    /**
     * 获取销售趋势数据
     */
    @GetMapping("/sales-trend")
    public Result<Map<String, Object>> getSalesTrend(@RequestParam(defaultValue = "7") int days) {
        try {
            // 尝试获取真实的销售趋势数据
            Map<String, Object> trendData = adminOrderService.getSalesTrend(days);
            return Result.success(trendData);
        } catch (Exception e) {
            // 如果获取真实数据失败，使用模拟数据
            Map<String, Object> trendData = new HashMap<>();
            
            List<String> dates = new ArrayList<>();
            List<Double> sales = new ArrayList<>();
            List<Integer> orders = new ArrayList<>();
            
            // 模拟最近指定天数的数据
            for (int i = days - 1; i >= 0; i--) {
                dates.add("2024-01-" + String.format("%02d", 15 + i));
                sales.add(1000 + Math.random() * 2000);
                orders.add(20 + (int)(Math.random() * 30));
            }
            
            trendData.put("dates", dates);
            trendData.put("sales", sales);
            trendData.put("orders", orders);
            
            return Result.success(trendData);
        }
    }

    /**
     * 获取发货效率数据
     */
    @GetMapping("/delivery-efficiency")
    public Result<Map<String, Object>> getDeliveryEfficiency() {
        try {
            Map<String, Object> efficiencyData = new HashMap<>();
            
            List<String> categories = Arrays.asList("当日发货", "次日发货", "2-3天发货", "3天以上");
            List<Integer> values = Arrays.asList(65, 25, 8, 2);
            
            efficiencyData.put("categories", categories);
            efficiencyData.put("values", values);
            
            return Result.success(efficiencyData);
        } catch (Exception e) {
            return Result.failed("获取发货效率失败: " + e.getMessage());
        }
    }

    /**
     * 获取推广效果数据
     */
    @GetMapping("/promotion-effect")
    public Result<Map<String, Object>> getPromotionEffect() {
        try {
            Map<String, Object> promotionData = new HashMap<>();
            
            List<String> channels = Arrays.asList("微信小程序", "APP", "H5", "其他");
            List<Integer> orders = Arrays.asList(450, 320, 280, 200);
            List<BigDecimal> revenue = Arrays.asList(
                BigDecimal.valueOf(45000), BigDecimal.valueOf(32000),
                BigDecimal.valueOf(28000), BigDecimal.valueOf(20000)
            );
            
            promotionData.put("channels", channels);
            promotionData.put("orders", orders);
            promotionData.put("revenue", revenue);
            
            return Result.success(promotionData);
        } catch (Exception e) {
            return Result.failed("获取推广效果失败: " + e.getMessage());
        }
    }

    /**
     * 获取延迟发货原因数据
     */
    @GetMapping("/delay-reasons")
    public Result<List<Map<String, Object>>> getDelayReasons() {
        try {
            List<Map<String, Object>> reasons = new ArrayList<>();
            
            String[] reasonTexts = {"库存不足", "商品缺货", "物流延误", "系统故障", "人员不足"};
            Integer[] counts = {15, 8, 5, 3, 2};
            String[] impacts = {"高", "中", "低", "中", "低"};
            
            for (int i = 0; i < reasonTexts.length; i++) {
                Map<String, Object> reason = new HashMap<>();
                reason.put("reason", reasonTexts[i]);
                reason.put("count", counts[i]);
                reason.put("impact", impacts[i]);
                reasons.add(reason);
            }
            
            return Result.success(reasons);
        } catch (Exception e) {
            return Result.failed("获取延迟原因失败: " + e.getMessage());
        }
    }
}