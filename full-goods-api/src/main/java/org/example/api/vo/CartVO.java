package org.example.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * 购物车VO
 */
@Data
public class CartVO {

    /**
     * 购物车ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

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
     * 水果价格
     */
    private java.math.BigDecimal fruitPrice;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}