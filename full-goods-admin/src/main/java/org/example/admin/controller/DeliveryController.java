package org.example.admin.controller;

import org.example.admin.service.AdminOrderService;
import org.example.common.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

@RestController
@RequestMapping("/admin/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    private static final Logger log = LoggerFactory.getLogger(DeliveryController.class);
    @Autowired
    private AdminOrderService adminOrderService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 模拟订单数据
    private static List<Map<String, Object>> orders = new ArrayList<>();
    private static Long nextId = 1L;
    
    static {
        // 初始化一些模拟订单数据，包含已付款待发货的订单
        orders.add(createOrder(nextId++, "ORD202401001", "张三", "13800138001", new BigDecimal("128.50"), 1, "北京市朝阳区xxx街道xxx号"));
        orders.add(createOrder(nextId++, "ORD202401002", "李四", "13800138002", new BigDecimal("89.90"), 1, "上海市浦东新区xxx路xxx号"));
        orders.add(createOrder(nextId++, "ORD202401003", "王五", "13800138003", new BigDecimal("256.80"), 2, "广州市天河区xxx大道xxx号"));
        orders.add(createOrder(nextId++, "ORD202401004", "赵六", "13800138004", new BigDecimal("45.60"), 1, "深圳市南山区xxx街xxx号"));
        orders.add(createOrder(nextId++, "ORD202401005", "钱七", "13800138005", new BigDecimal("178.30"), 3, "杭州市西湖区xxx路xxx号"));
        orders.add(createOrder(nextId++, "ORD202401006", "孙八", "13800138006", new BigDecimal("99.80"), 1, "成都市锦江区xxx街道xxx号"));
        orders.add(createOrder(nextId++, "ORD202401007", "周九", "13800138007", new BigDecimal("156.20"), 1, "武汉市洪山区xxx路xxx号"));
    }
    
    private static Map<String, Object> createOrder(Long id, String orderNo, String userName, String phone, BigDecimal amount, Integer status, String address) {
        Map<String, Object> order = new HashMap<>();
        order.put("id", id);
        order.put("orderNo", orderNo);
        order.put("receiverName", userName);
        order.put("receiverPhone", phone);
        order.put("totalAmount", amount);
        order.put("payAmount", amount);
        order.put("status", status);
        order.put("receiverAddress", address);
        order.put("receiverProvince", "省份");
        order.put("receiverCity", "城市");
        order.put("receiverDistrict", "区县");
        order.put("orderTime", LocalDateTime.now().minusDays((long)(Math.random() * 7)).format(formatter));
        order.put("createTime", LocalDateTime.now().format(formatter));
        order.put("updateTime", LocalDateTime.now().format(formatter));
        
        // 添加商品明细
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(createOrderItem("苹果", 2, new BigDecimal("12.50")));
        items.add(createOrderItem("香蕉", 3, new BigDecimal("8.90")));
        order.put("items", items);
        
        // 如果已发货，添加快递信息
        if (status == 2 || status == 3) {
            order.put("expressCompany", "顺丰速运");
            order.put("expressNo", "SF" + System.currentTimeMillis());
            order.put("shipTime", LocalDateTime.now().minusDays(1).format(formatter));
        }
        
        return order;
    }
    
    private static Map<String, Object> createOrderItem(String productName, Integer quantity, BigDecimal price) {
        Map<String, Object> item = new HashMap<>();
        item.put("productName", productName);
        item.put("quantity", quantity);
        item.put("price", price);
        item.put("subtotal", price.multiply(new BigDecimal(quantity)));
        return item;
    }

    /**
     * 获取待发货订单列表
     */
    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> getDeliveryOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        try {
            // 强制筛选待发货状态的订单，如果没有指定状态，默认为已付款待发货
            String deliveryStatus = (status != null && !status.trim().isEmpty()) ? status : "paid";
            
            // 调用真实的订单管理服务获取订单列表
            Map<String, Object> orderResult = adminOrderService.getOrderList(page, size, search, deliveryStatus, startDate, endDate);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取待发货订单列表成功");
            result.put("data", orderResult);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "获取订单列表失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<Map<String, Object>> getOrderDetail(@PathVariable Long id) {
        try {
            // 调用真实的订单管理服务获取订单详情
            Order order = adminOrderService.getOrderById(id);
            
            if (order != null) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", order.getId());
                orderMap.put("orderNo", order.getOrderNo());
                orderMap.put("receiverName", order.getReceiverName());
                orderMap.put("receiverPhone", order.getReceiverPhone());
                orderMap.put("totalAmount", order.getTotalAmount());
                orderMap.put("payAmount", order.getPayAmount());
                orderMap.put("status", order.getStatus());
                orderMap.put("receiverAddress", order.getReceiverAddress());
                orderMap.put("receiverProvince", order.getReceiverProvince());
                orderMap.put("receiverCity", order.getReceiverCity());
                orderMap.put("receiverDistrict", order.getReceiverDistrict());
                orderMap.put("orderTime", order.getCreateTime() != null ? order.getCreateTime().toString() : "");
                orderMap.put("createTime", order.getCreateTime() != null ? order.getCreateTime().toString() : "");
                orderMap.put("expressCompany", order.getCourier());
                orderMap.put("expressNo", order.getTrackingNumber());
                orderMap.put("shipTime", order.getDeliveryTime() != null ? order.getDeliveryTime().toString() : "");
                
                Map<String, Object> result = new HashMap<>();
                result.put("code", 200);
                result.put("message", "获取订单详情成功");
                result.put("data", orderMap);
                
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 404);
                result.put("message", "订单不存在");
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "获取订单详情失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 发货
     */
    @PostMapping("/ship")
    public ResponseEntity<Map<String, Object>> shipOrder(@RequestBody Map<String, Object> shipData) {
        Long orderId = Long.valueOf(shipData.get("orderId").toString());
        String expressCompany = (String) shipData.get("courier");
        String expressNo = (String) shipData.get("trackingNumber");
        log.info("发货请求，订单ID: {}, 快递公司: {}, 快递单号: {}", orderId, expressCompany, expressNo);
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用真实的发货服务
            adminOrderService.shipOrder(orderId, expressNo, expressCompany);
            
            result.put("code", 200);
            result.put("message", "发货成功");
            
            Map<String, Object> data = new HashMap<>();
            data.put("orderId", orderId);
            data.put("expressCompany", expressCompany);
            data.put("expressNo", expressNo);
            data.put("shipTime", LocalDateTime.now().format(formatter));
            result.put("data", data);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("发货失败，订单ID: " + orderId + ", 错误: " + e.getMessage());
            result.put("code", 500);
            result.put("message", "发货失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 批量发货
     */
    @PostMapping("/batch-ship")
    public ResponseEntity<Map<String, Object>> batchShipOrders(@RequestBody Map<String, Object> batchShipData) {
        @SuppressWarnings("unchecked")
        List<Long> orderIds = (List<Long>) batchShipData.get("orderIds");
        String expressCompany = (String) batchShipData.get("expressCompany");
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用真实的批量发货服务
            adminOrderService.batchShipOrders(orderIds, expressCompany);
            
            result.put("code", 200);
            result.put("message", "批量发货成功");
            result.put("data", orderIds.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("批量发货失败，订单ID列表: " + orderIds + ", 错误: " + e.getMessage());
            result.put("code", 500);
            result.put("message", "批量发货失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用真实的订单删除服务
            adminOrderService.deleteOrder(id);
            
            result.put("code", 200);
            result.put("message", "订单删除成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("删除订单失败，订单ID: " + id + ", 错误: " + e.getMessage());
            result.put("code", 500);
            result.put("message", "删除订单失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 批量删除订单
     */
    @DeleteMapping("/batch-delete")
    public ResponseEntity<Map<String, Object>> batchDeleteOrders(@RequestBody List<Long> orderIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用真实的订单批量删除服务
            adminOrderService.batchDeleteOrders(orderIds);
            
            result.put("code", 200);
            result.put("message", "批量删除成功，共删除 " + orderIds.size() + " 个订单");
            result.put("data", orderIds.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("批量删除订单失败，订单ID列表: " + orderIds + ", 错误: " + e.getMessage());
            result.put("code", 500);
            result.put("message", "批量删除订单失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 获取快递公司列表
     */
    @GetMapping("/express-companies")
    public ResponseEntity<Map<String, Object>> getExpressCompanies() {
        List<Map<String, Object>> companies = Arrays.asList(
            createExpressCompany("SF", "顺丰速运"),
            createExpressCompany("YTO", "圆通速递"),
            createExpressCompany("ZTO", "中通快递"),
            createExpressCompany("STO", "申通快递"),
            createExpressCompany("YD", "韵达速递"),
            createExpressCompany("HTKY", "百世快递"),
            createExpressCompany("JD", "京东物流"),
            createExpressCompany("EMS", "中国邮政")
        );
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取快递公司列表成功");
        result.put("data", companies);
        
        return ResponseEntity.ok(result);
    }
    
    private static Map<String, Object> createExpressCompany(String code, String name) {
        Map<String, Object> company = new HashMap<>();
        company.put("code", code);
        company.put("name", name);
        return company;
    }

    /**
     * 获取配送统计数据
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getDeliveryStatistics() {
        try {
            // 调用真实的订单统计服务
            Map<String, Object> statistics = adminOrderService.getOrderStatistics();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取配送统计成功");
            result.put("data", statistics);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "获取配送统计数据失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }
}