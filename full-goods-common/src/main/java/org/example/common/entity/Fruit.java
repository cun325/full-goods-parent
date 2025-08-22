package org.example.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 水果实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Fruit extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 水果ID
     */
    private Long id;

    /**
     * 水果名称
     */
    private String name;

    /**
     * 水果描述
     */
    private String description;

    /**
     * 产地
     */
    private String origin;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 单位（如：500g/份，1kg/箱）
     */
    private String unit;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 分类ID（如：热带水果、应季水果等）
     */
    private Integer categoryId;

    /**
     * 口感（如：酸甜、香甜等）
     */
    private String taste;

    /**
     * 营养成分（如：富含维生素C等）
     */
    private String nutrition;

    /**
     * 适合人群（如：适合孕妇、儿童等）
     */
    private String suitableCrowd;

    /**
     * 状态：0-下架，1-上架
     */
    private Integer status;

    /**
     * 推荐状态（0-不推荐，1-推荐）
     */
    private Integer recommended;
    
    /**
     * 是否有限时特惠（0-无，1-有）
     */
    private Integer flashSaleActive;
    
    /**
     * 限时特惠价格
     */
    private BigDecimal flashSalePrice;
    
    /**
     * 限时特惠原价
     */
    private BigDecimal flashSaleOriginalPrice;
    
    /**
     * 限时特惠开始时间
     */
    private java.util.Date flashSaleStartTime;
    
    /**
     * 限时特惠结束时间
     */
    private java.util.Date flashSaleEndTime;
    
    /**
     * 限时特惠库存
     */
    private Integer flashSaleStock;
    
    /**
     * 限时特惠已售数量
     */
    private Integer flashSaleSoldCount;
}