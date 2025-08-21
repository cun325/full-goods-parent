package org.example.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 购物车实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Cart extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
     * 数量
     */
    private Integer quantity;
}