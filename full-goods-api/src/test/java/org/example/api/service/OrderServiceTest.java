package org.example.api.service;

import org.example.api.dto.CreateOrderDTO;
import org.example.api.mapper.OrderMapper;
import org.example.api.mapper.OrderItemMapper;
import org.example.api.mapper.AddressMapper;
import org.example.api.mapper.CartMapper;
import org.example.api.mapper.FruitMapper;
import org.example.api.service.impl.OrderServiceImpl;
import org.example.common.entity.Order;
import org.example.common.entity.OrderItem;
import org.example.common.entity.Address;
import org.example.common.entity.Cart;
import org.example.common.entity.Fruit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * OrderService单元测试
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;
    
    @Mock
    private OrderItemMapper orderItemMapper;
    
    @Mock
    private AddressMapper addressMapper;
    
    @Mock
    private CartMapper cartMapper;
    
    @Mock
    private FruitMapper fruitMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUserId(1L);
        testOrder.setOrderNo("ORD20240101001");
        testOrder.setTotalAmount(new BigDecimal("99.99"));
        testOrder.setStatus(1);
        testOrder.setCreateTime(new Date());
        
        testOrderItem = new OrderItem();
        testOrderItem.setId(1L);
        testOrderItem.setOrderNo("ORD20240101001");
        testOrderItem.setFruitId(1L);
        testOrderItem.setQuantity(2);
        testOrderItem.setPrice(new BigDecimal("49.99"));
    }

    @Test
    void testCreateOrderSuccess() {
        // Mock address
        Address address = new Address();
        address.setId(1L);
        address.setUserId(1L);
        address.setReceiverName("张三");
        address.setReceiverPhone("13800138000");
        address.setProvince("北京市");
        address.setCity("北京市");
        address.setDistrict("朝阳区");
        address.setDetailAddress("某某街道123号");
        when(addressMapper.selectById(1L)).thenReturn(address);
        
        // Mock cart
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUserId(1L);
        cart.setFruitId(1L);
        cart.setQuantity(2);
        when(cartMapper.selectById(1L)).thenReturn(cart);
        
        // Mock fruit
        Fruit fruit = new Fruit();
        fruit.setId(1L);
        fruit.setName("苹果");
        fruit.setPrice(new BigDecimal("49.99"));
        fruit.setStock(10); // 添加库存信息
        when(fruitMapper.selectById(1L)).thenReturn(fruit);
        when(fruitMapper.decreaseStock(1L, 2)).thenReturn(1); // 添加decreaseStock方法的mock行为
        
        when(orderMapper.insert(any(Order.class))).thenReturn(1);
        when(orderItemMapper.insert(any(OrderItem.class))).thenReturn(1);
        
        Order result = orderService.createOrder(1L, 1L, Arrays.asList(1L));
        
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        
        verify(orderMapper).insert(any(Order.class));
    }

    @Test
    void testCreateOrderWithBuyNowItems() {
        // Mock address
        Address address = new Address();
        address.setId(1L);
        address.setUserId(1L);
        address.setReceiverName("张三");
        address.setReceiverPhone("13800138000");
        address.setProvince("北京市");
        address.setCity("北京市");
        address.setDistrict("朝阳区");
        address.setDetailAddress("某某街道123号");
        when(addressMapper.selectById(1L)).thenReturn(address);
        
        // Mock fruit for buyNowItem validation
        Fruit fruit = new Fruit();
        fruit.setId(1L);
        fruit.setName("苹果");
        fruit.setPrice(new BigDecimal("49.99"));
        fruit.setStock(10); // 添加库存信息
        when(fruitMapper.selectById(1L)).thenReturn(fruit);
        when(fruitMapper.decreaseStock(1L, 2)).thenReturn(1); // 添加decreaseStock方法的mock行为
        
        when(orderMapper.insert(any(Order.class))).thenReturn(1);
        when(orderItemMapper.insert(any(OrderItem.class))).thenReturn(1);
        
        CreateOrderDTO.BuyNowItem buyNowItem = new CreateOrderDTO.BuyNowItem();
        buyNowItem.setFruitId(1L);
        buyNowItem.setName("苹果");
        buyNowItem.setPrice(new BigDecimal("49.99"));
        buyNowItem.setQuantity(2);
        
        Order result = orderService.createOrder(1L, 1L, null, Arrays.asList(buyNowItem));
        
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        
        verify(orderMapper).insert(any(Order.class));
    }

    @Test
    void testGetOrderListSuccess() {
        when(orderMapper.selectByUserId(1L)).thenReturn(Arrays.asList(testOrder));
        
        List<Order> result = orderService.getOrderList(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testOrder.getId(), result.get(0).getId());
        
        verify(orderMapper).selectByUserId(1L);
    }

    @Test
    void testGetOrderListByStatusSuccess() {
        when(orderMapper.selectByUserIdAndStatus(1L, 1)).thenReturn(Arrays.asList(testOrder));
        
        List<Order> result = orderService.getOrderListByStatus(1L, 1);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testOrder.getId(), result.get(0).getId());
        
        verify(orderMapper).selectByUserIdAndStatus(1L, 1);
    }

    @Test
    void testGetOrderDetailSuccess() {
        when(orderMapper.selectById(1L)).thenReturn(testOrder);
        
        Order result = orderService.getOrderDetail(1L);
        
        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
        
        verify(orderMapper).selectById(1L);
    }

    @Test
    void testGetOrderDetailNotFound() {
        when(orderMapper.selectById(1L)).thenReturn(null);
        
        Order result = orderService.getOrderDetail(1L);
        
        assertNull(result);
        verify(orderMapper).selectById(1L);
    }

    @Test
    void testCancelOrderSuccess() {
        testOrder.setStatus(0); // 待付款状态
        when(orderMapper.selectById(1L)).thenReturn(testOrder);
        when(orderMapper.update(any(Order.class))).thenReturn(1);
        
        boolean result = orderService.cancelOrder(1L, 1L);
        
        assertTrue(result);
        verify(orderMapper).update(any(Order.class));
    }

    @Test
    void testPayOrderSuccess() {
        testOrder.setStatus(0); // 待付款状态
        when(orderMapper.selectById(1L)).thenReturn(testOrder);
        when(orderMapper.update(any(Order.class))).thenReturn(1);
        
        boolean result = orderService.payOrder(1L, 1L, 1);
        
        assertTrue(result);
        verify(orderMapper).update(any(Order.class));
    }

    @Test
    void testConfirmReceiveSuccess() {
        testOrder.setStatus(2); // 待收货状态
        when(orderMapper.selectById(1L)).thenReturn(testOrder);
        when(orderMapper.update(any(Order.class))).thenReturn(1);
        
        boolean result = orderService.confirmReceive(1L, 1L);
        
        assertTrue(result);
        verify(orderMapper).update(any(Order.class));
    }
}