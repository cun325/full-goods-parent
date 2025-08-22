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