package org.example.api.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单项VO
 */
@Data
public class OrderItemVO {

    /**
     * 订单项ID
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 水果ID
     */
    private Long fruitId;

    /**
     * 水果名称
     */
    private String fruitName;

    /**
     * 水果图片
     */
    private String fruitImage;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 总价
     */
    private BigDecimal totalPrice;
}