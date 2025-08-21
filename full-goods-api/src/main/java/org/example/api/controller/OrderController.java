package org.example.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.CreateOrderDTO;
import org.example.api.mapper.OrderItemMapper;
import org.example.api.mapper.OrderMapper;
import org.example.api.mapper.FruitMapper;
import org.example.api.service.OrderService;
import org.example.api.service.UserService;
import org.example.api.service.MessageService;
import org.example.api.vo.OrderItemVO;
import org.example.api.vo.OrderVO;
import org.example.common.entity.Order;
import org.example.common.entity.OrderItem;
import org.example.common.entity.User;
import org.example.common.response.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.stream.Collectors;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = "订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private FruitMapper fruitMapper;
    
    @Autowired
    private MessageService messageService;

    @PostMapping("/create")
    @ApiOperation(value = "创建订单", notes = "用户创建新订单，支持购物车结算和立即购买")
    @ApiResponses({
        @ApiResponse(code = 200, message = "订单创建成功"),
        @ApiResponse(code = 400, message = "参数错误或库存不足"),
        @ApiResponse(code = 401, message = "用户未登录或token已过期")
    })
    public Result<OrderVO> createOrder(
            @ApiParam(value = "订单创建信息", required = true) @Valid @RequestBody CreateOrderDTO createOrderDTO) {
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(createOrderDTO.getToken())) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(createOrderDTO.getToken());
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 创建订单
            Order order = orderService.createOrder(user.getId(), createOrderDTO.getAddressId(), createOrderDTO.getCartIds(), createOrderDTO.getBuyNowItems());
            
            // 转换为VO
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            // 设置状态描述
            orderVO.setStatusDesc(orderVO.getStatusDesc());
            
            // 获取订单项列表
            List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
            List<OrderItemVO> orderItemVOList = orderItems.stream().map(item -> {
                OrderItemVO itemVO = new OrderItemVO();
                BeanUtils.copyProperties(item, itemVO);
                return itemVO;
            }).collect(Collectors.toList());
            orderVO.setItems(orderItemVOList);
            
            // 计算商品总数量
            Integer totalQuantity = orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
            orderVO.setTotalQuantity(totalQuantity);
            
            return Result.success(orderVO);
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return Result.failed(e.getMessage());
        }
    }

    @PostMapping("/list")
    @ApiOperation("获取用户订单列表")
    public Result<List<OrderVO>> getOrderList(@RequestBody java.util.Map<String, Object> request) {
        String token = (String) request.get("token");
        Integer status = request.get("status") != null ? Integer.valueOf(request.get("status").toString()) : null;
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 获取订单列表
            List<Order> orderList = orderService.getOrderListByStatus(user.getId(), status);
            
            // 转换为VO列表
            List<OrderVO> orderVOList = orderList.stream().map(order -> {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(order, orderVO);
                // 设置状态描述
                orderVO.setStatusDesc(orderVO.getStatusDesc());
                
                // 获取订单项列表
                List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
                List<OrderItemVO> orderItemVOList = orderItems.stream().map(item -> {
                    OrderItemVO itemVO = new OrderItemVO();
                    BeanUtils.copyProperties(item, itemVO);
                    return itemVO;
                }).collect(Collectors.toList());
                orderVO.setItems(orderItemVOList);
                
                // 计算商品总数量
                Integer totalQuantity = orderItems.stream()
                    .mapToInt(OrderItem::getQuantity)
                    .sum();
                orderVO.setTotalQuantity(totalQuantity);
                
                return orderVO;
            }).collect(Collectors.toList());
            
            return Result.success(orderVOList);
        } catch (Exception e) {
            log.error("获取订单列表失败", e);
            return Result.failed("获取订单列表失败");
        }
    }

    @GetMapping("/detail/{orderId}")
    @ApiOperation("获取订单详情")
    public Result<OrderVO> getOrderDetail(@PathVariable Long orderId, @RequestParam String token) {
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 获取订单详情
            Order order = orderService.getOrderDetail(orderId);
            if (order == null) {
                return Result.failed("订单不存在");
            }

            // 验证订单是否属于当前用户
            if (!order.getUserId().equals(user.getId())) {
                return Result.failed("无权访问该订单");
            }
            
            // 转换为VO
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            // 设置状态描述
            orderVO.setStatusDesc(orderVO.getStatusDesc());
            
            return Result.success(orderVO);
        } catch (Exception e) {
            log.error("获取订单详情失败", e);
            return Result.failed("获取订单详情失败");
        }
    }

    @GetMapping("/detail/no/{orderNo}")
    @ApiOperation("根据订单号获取订单详情")
    public Result<OrderVO> getOrderByOrderNo(
            @ApiParam("订单号") @PathVariable String orderNo,
            @ApiParam("用户token") @RequestParam String token) {
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 根据订单号获取订单详情
            Order order = orderMapper.selectByOrderNo(orderNo);
            if (order == null) {
                return Result.failed("订单不存在");
            }

            // 验证订单是否属于当前用户
            if (!order.getUserId().equals(user.getId())) {
                return Result.failed("无权访问该订单");
            }

            // 转换为VO
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);

            // 获取订单项列表
            List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(orderNo);
            List<OrderItemVO> orderItemVOList = orderItems.stream().map(item -> {
                OrderItemVO itemVO = new OrderItemVO();
                BeanUtils.copyProperties(item, itemVO);
                return itemVO;
            }).collect(Collectors.toList());
            orderVO.setItems(orderItemVOList);

            // 计算商品总数量
            Integer totalQuantity = orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
            orderVO.setTotalQuantity(totalQuantity);

            return Result.success(orderVO);
        } catch (Exception e) {
            log.error("获取订单详情失败", e);
            return Result.failed("获取订单详情失败");
        }
    }

    @PostMapping("/cancel/{orderId}")
    @ApiOperation("取消订单")
    public Result<Boolean> cancelOrder(@PathVariable Long orderId, @RequestBody java.util.Map<String, String> request) {
        String token = request.get("token");
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 取消订单
            boolean success = orderService.cancelOrder(orderId, user.getId());
            
            return success ? Result.success(true) : Result.failed("取消订单失败");
        } catch (Exception e) {
            log.error("取消订单失败", e);
            return Result.failed("取消订单失败");
        }
    }

    @PostMapping("/pay/no/{orderNo}")
    @ApiOperation("通过订单号支付订单")
    public Result<Boolean> payOrderByOrderNo(@ApiParam("订单号") @PathVariable String orderNo, 
                                           @RequestBody java.util.Map<String, Object> request) {
        String token = (String) request.get("token");
        Integer payType = (Integer) request.get("payType");
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 根据订单号获取订单
            Order order = orderMapper.selectByOrderNo(orderNo);
            if (order == null) {
                return Result.failed("订单不存在");
            }

            // 验证订单是否属于当前用户
            if (!order.getUserId().equals(user.getId())) {
                return Result.failed("无权操作该订单");
            }

            // 支付订单
            boolean success = orderService.payOrder(order.getId(), user.getId(), payType);
            
            return success ? Result.success(true) : Result.failed("支付失败");
        } catch (Exception e) {
            log.error("支付订单失败", e);
            return Result.failed("支付订单失败");
        }
    }

    @PostMapping("/pay/{orderId}")
    @ApiOperation("支付订单")
    public Result<Boolean> payOrder(@PathVariable Long orderId, @RequestBody java.util.Map<String, Object> request) {
        String token = (String) request.get("token");
        Integer payType = (Integer) request.get("payType");
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 支付订单
            boolean success = orderService.payOrder(orderId, user.getId(), payType);
            
            return success ? Result.success(true) : Result.failed("支付订单失败");
        } catch (Exception e) {
            log.error("支付订单失败", e);
            return Result.failed("支付订单失败");
        }
    }

    @PostMapping("/confirm/{orderId}")
    @ApiOperation("确认收货")
    public Result<Boolean> confirmReceive(@PathVariable Long orderId, @RequestBody java.util.Map<String, String> request) {
        String token = request.get("token");
        try {
            // 验证token获取用户信息
            if (StringUtils.isEmpty(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 确认收货
            boolean success = orderService.confirmReceive(orderId, user.getId());
            
            return Result.success(success);
        } catch (Exception e) {
            log.error("确认收货失败", e);
            return Result.failed("确认收货失败");
        }
    }

    @GetMapping("/admin/list")
    @ApiOperation("管理员获取订单列表（分页）")
    public Result<PageInfo<OrderVO>> getOrderListForAdmin(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("搜索关键词") @RequestParam(required = false) String search,
            @ApiParam("订单状态") @RequestParam(required = false) String status,
            @ApiParam("用户ID") @RequestParam(required = false) Long userId) {
        try {
            // 设置分页参数
            PageHelper.startPage(page, size);
            
            // 将字符串状态转换为整数状态
            Integer statusInt = null;
            if (StringUtils.hasText(status)) {
                switch (status.toLowerCase()) {
                    case "pending":
                        statusInt = 0; // 待付款
                        break;
                    case "paid":
                        statusInt = 1; // 已付款（待发货）
                        break;
                    case "shipped":
                        statusInt = 2; // 已发货
                        break;
                    case "delivered":
                        statusInt = 3; // 已送达
                        break;
                    case "cancelled":
                        statusInt = 4; // 已取消
                        break;
                    default:
                        try {
                            statusInt = Integer.parseInt(status);
                        } catch (NumberFormatException e) {
                            // 如果无法解析，保持为null
                        }
                        break;
                }
            }

            List<Order> orders;
            Long total = 0L;
            if (StringUtils.hasText(search) || statusInt != null || userId != null) {
                // 根据条件查询
                orders = orderMapper.selectByConditionsWithPage(search, statusInt);
                // 手动查询总数
                total = orderMapper.countTotal(); // 这里应该根据条件查询对应的count方法
            } else {
                // 查询所有
                orders = orderMapper.selectAllWithPage();
                // 查询所有订单总数
                total = orderMapper.countTotal();
            }
            
            // 转换为VO并获取订单项
            List<OrderVO> orderVOList = orders.stream().map(order -> {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(order, orderVO);
                
                // 获取订单项列表
                List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
                List<OrderItemVO> orderItemVOList = orderItems.stream().map(item -> {
                    OrderItemVO itemVO = new OrderItemVO();
                    BeanUtils.copyProperties(item, itemVO);
                    return itemVO;
                }).collect(Collectors.toList());
                orderVO.setItems(orderItemVOList);
                
                // 计算商品总数量
                Integer totalQuantity = orderItems.stream()
                    .mapToInt(OrderItem::getQuantity)
                    .sum();
                orderVO.setTotalQuantity(totalQuantity);
                
                return orderVO;
            }).collect(Collectors.toList());
            
            // 创建分页信息
            PageInfo<OrderVO> pageInfo = new PageInfo<>(orderVOList);
            
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("获取订单列表失败", e);
            return Result.failed("获取订单列表失败");
        }
    }
    
    @GetMapping("/admin/detail/{orderId}")
    @ApiOperation("管理员获取订单详情")
    public Result<OrderVO> getOrderDetailForAdmin(@PathVariable Long orderId) {
        try {
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (order == null) {
                return Result.failed("订单不存在");
            }
            
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            
            // 获取订单项列表
            List<OrderItem> orderItems = orderItemMapper.selectByOrderId(orderId);
            List<OrderItemVO> orderItemVOList = orderItems.stream().map(item -> {
                OrderItemVO itemVO = new OrderItemVO();
                BeanUtils.copyProperties(item, itemVO);
                return itemVO;
            }).collect(Collectors.toList());
            orderVO.setItems(orderItemVOList);
            
            // 计算商品总数量
            Integer totalQuantity = orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
            orderVO.setTotalQuantity(totalQuantity);
            
            return Result.success(orderVO);
        } catch (Exception e) {
            log.error("获取订单详情失败", e);
            return Result.failed("获取订单详情失败");
        }
    }
    
    @PutMapping("/admin/status/{orderId}")
    @ApiOperation("管理员更新订单状态")
    public Result<Boolean> updateOrderStatus(@PathVariable Long orderId, @RequestBody java.util.Map<String, Object> request) {
        try {
            Integer status = (Integer) request.get("status");
            if (status == null) {
                return Result.failed("状态不能为空");
            }
            
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (order == null) {
                return Result.failed("订单不存在");
            }
            
            order.setStatus(status);
            int result = orderMapper.updateByPrimaryKey(order);
            
            return result > 0 ? Result.success(true) : Result.failed("更新失败");
        } catch (Exception e) {
            log.error("更新订单状态失败", e);
            return Result.failed("更新订单状态失败");
        }
    }
    
    @PutMapping("/admin/ship/{orderId}")
    @ApiOperation("管理员发货")
    public Result<Boolean> shipOrder(@PathVariable Long orderId, @RequestBody java.util.Map<String, Object> request) {
        try {
            String trackingNumber = (String) request.get("trackingNumber");
            String courier = (String) request.get("courier");
            
            if (StringUtils.isEmpty(trackingNumber)) {
                return Result.failed("快递单号不能为空");
            }
            if (StringUtils.isEmpty(courier)) {
                return Result.failed("快递公司不能为空");
            }
            
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (order == null) {
                return Result.failed("订单不存在");
            }
            
            if (order.getStatus() != 1) {
                return Result.failed("只有待发货订单才能发货");
            }
            
            // 更新订单状态为已发货
            order.setStatus(2);
            order.setTrackingNumber(trackingNumber);
            order.setCourier(courier);
            int result = orderMapper.updateByPrimaryKey(order);
            
            // 发送物流通知给用户
            if (result > 0) {
                try {
                    String content = String.format("您的订单%s已发货，快递公司：%s，快递单号：%s，预计1-3天送达。", 
                        order.getOrderNo(), courier, trackingNumber);
                    messageService.sendLogisticsNotification(order.getUserId(), order.getOrderNo(), "已发货", content);
                } catch (Exception e) {
                    log.error("发送物流通知失败，订单ID: {}", orderId, e);
                }
            }
            
            return result > 0 ? Result.success(true) : Result.failed("发货失败");
        } catch (Exception e) {
            log.error("订单发货失败", e);
            return Result.failed("订单发货失败");
        }
    }
    
    @DeleteMapping("/admin/delete/{orderId}")
    @ApiOperation("管理员删除订单")
    public Result<Boolean> deleteOrder(@PathVariable Long orderId) {
        try {
            // 先删除订单项
            orderItemMapper.deleteByOrderId(orderId);
            // 再删除订单
            int result = orderMapper.deleteByPrimaryKey(orderId);
            
            return result > 0 ? Result.success(true) : Result.failed("删除失败");
        } catch (Exception e) {
            log.error("删除订单失败", e);
            return Result.failed("删除订单失败");
        }
    }
    
    @GetMapping("/admin/statistics")
    @ApiOperation("管理员获取订单统计数据")
    public Result<java.util.Map<String, Object>> getOrderStatistics() {
        try {
            java.util.Map<String, Object> statistics = new java.util.HashMap<>();
            
            // 订单总数
            Long totalOrders = orderMapper.countTotal();
            statistics.put("totalOrders", totalOrders);
            
            // 今日订单数
            Long todayOrders = orderMapper.countTodayOrders();
            statistics.put("todayOrders", todayOrders);
            
            // 今日销售额
            BigDecimal todayRevenue = orderMapper.sumTodayRevenue();
            statistics.put("todayRevenue", todayRevenue != null ? todayRevenue : BigDecimal.ZERO);
            
            // 各状态订单数
            Long pendingOrders = orderMapper.countByStatus(0); // 待支付
            Long paidOrders = orderMapper.countByStatus(1); // 已支付
            Long shippedOrders = orderMapper.countByStatus(2); // 已发货
            Long completedOrders = orderMapper.countByStatus(3); // 已完成
            Long cancelledOrders = orderMapper.countByStatus(4); // 已取消
            
            statistics.put("pendingOrders", pendingOrders);
            statistics.put("paidOrders", paidOrders);
            statistics.put("shippedOrders", shippedOrders);
            statistics.put("completedOrders", completedOrders);
            statistics.put("cancelledOrders", cancelledOrders);
            
            // 库存不足商品数（库存小于等于10）
            Long lowStockProducts = fruitMapper.countLowStock(10);
            statistics.put("lowStockProducts", lowStockProducts);
            
            // 月增长率
            Double monthlyGrowth = orderMapper.calculateMonthlyGrowth();
            statistics.put("monthlyGrowth", monthlyGrowth);
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取订单统计数据失败", e);
            return Result.failed("获取订单统计数据失败");
        }
    }
    
    @GetMapping("/admin/today-revenue")
    @ApiOperation("管理员获取今日销售额")
    public Result<java.util.Map<String, Object>> getTodayRevenue() {
        try {
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            
            // 今日销售额
            BigDecimal todayRevenue = orderMapper.sumTodayRevenue();
            result.put("todayRevenue", todayRevenue != null ? todayRevenue : BigDecimal.ZERO);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取今日销售额失败", e);
            return Result.failed("获取今日销售额失败");
        }
    }
    
    /**
     * 获取销售趋势数据
     */
    @GetMapping("/sales-trend")
    @ApiOperation("获取销售趋势数据")
    public Result<Map<String, Object>> getSalesTrend(@RequestParam(defaultValue = "7") int days) {
        try {
            Map<String, Object> trendData = new HashMap<>();
            
            // 计算日期范围
            List<String> dates = new ArrayList<>();
            List<BigDecimal> sales = new ArrayList<>();
            List<Integer> orders = new ArrayList<>();
            
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -(days - 1));
            
            for (int i = 0; i < days; i++) {
                // 格式化日期
                String dateStr = String.format("%tY-%tm-%td", calendar, calendar, calendar);
                dates.add(dateStr);
                
                // 获取该日期的销售数据
                Date startDate = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date endDate = calendar.getTime();
                
                // 查询该日期的订单数量和销售总额
                Integer orderCount = orderMapper.countByDateRange(startDate, endDate);
                BigDecimal totalSales = orderMapper.sumSalesByDateRange(startDate, endDate);
                
                orders.add(orderCount != null ? orderCount : 0);
                sales.add(totalSales != null ? totalSales : BigDecimal.ZERO);
            }
            
            trendData.put("dates", dates);
            trendData.put("sales", sales);
            trendData.put("orders", orders);
            
            return Result.success(trendData);
        } catch (Exception e) {
            log.error("获取销售趋势数据失败", e);
            return Result.failed("获取销售趋势数据失败: " + e.getMessage());
        }
    }
}