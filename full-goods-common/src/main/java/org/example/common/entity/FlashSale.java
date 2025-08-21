package org.example.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 限时特惠实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FlashSale extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 限时特惠ID
     */
    private Long id;

    /**
     * 水果ID
     */
    private Long fruitId;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 特惠价
     */
    private BigDecimal salePrice;

    /**
     * 折扣率（如：0.8表示8折）
     */
    private BigDecimal discountRate;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 特惠库存
     */
    private Integer stock;

    /**
     * 已售数量
     */
    private Integer soldCount;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}