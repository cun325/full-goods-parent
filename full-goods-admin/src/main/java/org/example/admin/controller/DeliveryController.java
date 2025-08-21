package org.example.admin.controller;

import org.example.admin.service.AdminOrderService;
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
            // 如果调用真实API失败，返回模拟数据
            List<Map<String, Object>> filteredOrders = new ArrayList<>(orders);
            
            // 强制筛选待发货状态的订单（已付款状态，status=1）
            Integer deliveryStatus = 1; // 默认筛选已付款待发货的订单
            if (status != null && !status.trim().isEmpty()) {
                 try {
                     deliveryStatus = Integer.parseInt(status);
                 } catch (NumberFormatException nfe) {
                     // 如果传入的是字符串状态，转换为数字
                     if ("paid".equals(status)) {
                         deliveryStatus = 1;
                     } else if ("pending".equals(status)) {
                         deliveryStatus = 0;
                     } else if ("shipped".equals(status)) {
                         deliveryStatus = 2;
                     } else if ("delivered".equals(status)) {
                         deliveryStatus = 3;
                     }
                 }
             }
            final Integer finalDeliveryStatus = deliveryStatus;
            filteredOrders = filteredOrders.stream()
                .filter(order -> order.get("status").equals(finalDeliveryStatus))
                .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
            
            // 搜索过滤
            if (search != null && !search.trim().isEmpty()) {
                filteredOrders = filteredOrders.stream()
                    .filter(order -> 
                        order.get("orderNo").toString().contains(search) ||
                        order.get("userName").toString().contains(search) ||
                        order.get("phone").toString().contains(search)
                    )
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
            }
            
            // 按订单时间排序（最新的在前）
            filteredOrders.sort((a, b) -> {
                String timeA = (String) a.get("orderTime");
                String timeB = (String) b.get("orderTime");
                return timeB.compareTo(timeA);
            });
            
            // 分页
            int total = filteredOrders.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            List<Map<String, Object>> pagedOrders = filteredOrders.subList(start, end);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取订单列表成功（模拟数据）");
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", pagedOrders);
            data.put("total", total);
            data.put("page", page);
            data.put("size", size);
            result.put("data", data);
            
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<Map<String, Object>> getOrderDetail(@PathVariable Long id) {
        Map<String, Object> order = orders.stream()
            .filter(o -> o.get("id").equals(id))
            .findFirst()
            .orElse(null);
        
        Map<String, Object> result = new HashMap<>();
        if (order != null) {
            result.put("code", 200);
            result.put("message", "获取订单详情成功");
            result.put("data", order);
        } else {
            result.put("code", 404);
            result.put("message", "订单不存在");
        }
        
        return ResponseEntity.ok(result);
    }

    /**
     * 发货
     */
    @PostMapping("/ship")
    public ResponseEntity<Map<String, Object>> shipOrder(@RequestBody Map<String, Object> shipData) {
        Long orderId = Long.valueOf(shipData.get("orderId").toString());
        String expressCompany = (String) shipData.get("expressCompany");
        String expressNo = (String) shipData.get("expressNo");
        
        // 先更新模拟数据中的订单状态
        Map<String, Object> order = orders.stream()
            .filter(o -> o.get("id").equals(orderId))
            .findFirst()
            .orElse(null);
        
        Map<String, Object> result = new HashMap<>();
        if (order == null) {
            result.put("code", 404);
            result.put("message", "订单不存在");
            return ResponseEntity.ok(result);
        }
        
        try {
            // 调用真实的发货服务
            adminOrderService.shipOrder(orderId, expressNo, expressCompany);
            
            // 更新模拟数据中的订单状态
            order.put("status", 2); // 已发货
            order.put("expressCompany", expressCompany);
            order.put("expressNo", expressNo);
            order.put("shipTime", LocalDateTime.now().format(formatter));
            order.put("updateTime", LocalDateTime.now().format(formatter));
            
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
            // 如果调用真实API失败，仍然更新模拟数据
            order.put("status", 2); // 已发货
            order.put("expressCompany", expressCompany);
            order.put("expressNo", expressNo);
            order.put("shipTime", LocalDateTime.now().format(formatter));
            order.put("updateTime", LocalDateTime.now().format(formatter));
            
            result.put("code", 200);
            result.put("message", "发货成功（模拟数据）");
            result.put("data", order);
            
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
        
        int shippedCount = 0;
        for (Long orderId : orderIds) {
            Map<String, Object> order = orders.stream()
                .filter(o -> o.get("id").equals(orderId))
                .findFirst()
                .orElse(null);
            
            if (order != null && Integer.valueOf(1).equals(order.get("status"))) {
                order.put("status", 2); // 已发货
                order.put("expressCompany", expressCompany);
                order.put("expressNo", expressCompany.substring(0, 2).toUpperCase() + System.currentTimeMillis() + orderId);
                order.put("shipTime", LocalDateTime.now().format(formatter));
                order.put("updateTime", LocalDateTime.now().format(formatter));
                shippedCount++;
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "批量发货成功，共发货 " + shippedCount + " 个订单");
        result.put("data", shippedCount);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Long id) {
        boolean removed = orders.removeIf(order -> order.get("id").equals(id));
        
        Map<String, Object> result = new HashMap<>();
        if (removed) {
            result.put("code", 200);
            result.put("message", "订单删除成功");
        } else {
            result.put("code", 404);
            result.put("message", "订单不存在");
        }
        
        return ResponseEntity.ok(result);
    }

    /**
     * 批量删除订单
     */
    @DeleteMapping("/batch-delete")
    public ResponseEntity<Map<String, Object>> batchDeleteOrders(@RequestBody List<Long> orderIds) {
        int deletedCount = 0;
        for (Long orderId : orderIds) {
            boolean removed = orders.removeIf(order -> order.get("id").equals(orderId));
            if (removed) {
                deletedCount++;
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "批量删除成功，共删除 " + deletedCount + " 个订单");
        result.put("data", deletedCount);
        
        return ResponseEntity.ok(result);
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
        long pendingCount = orders.stream().filter(o -> Integer.valueOf(0).equals(o.get("status"))).count();
        long paidCount = orders.stream().filter(o -> Integer.valueOf(1).equals(o.get("status"))).count();
        long shippedCount = orders.stream().filter(o -> Integer.valueOf(2).equals(o.get("status"))).count();
        long deliveredCount = orders.stream().filter(o -> Integer.valueOf(3).equals(o.get("status"))).count();
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("pendingCount", pendingCount);
        statistics.put("paidCount", paidCount);
        statistics.put("shippedCount", shippedCount);
        statistics.put("deliveredCount", deliveredCount);
        statistics.put("totalCount", orders.size());
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取配送统计成功");
        result.put("data", statistics);
        
        return ResponseEntity.ok(result);
    }
}