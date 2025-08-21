package org.example.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水果信息VO
 */
@Data
@ApiModel(description = "水果信息")
public class FruitVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "水果ID")
    private Long id;

    @ApiModelProperty(value = "水果名称")
    private String name;

    @ApiModelProperty(value = "水果描述")
    private String description;

    @ApiModelProperty(value = "产地")
    private String origin;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "图片URL")
    private String imageUrl;

    @ApiModelProperty(value = "分类")
    private String category;

    @ApiModelProperty(value = "口感")
    private String taste;

    @ApiModelProperty(value = "营养成分")
    private String nutrition;

    @ApiModelProperty(value = "适合人群")
    private String suitableCrowd;

    @ApiModelProperty(value = "库存数量")
    private Integer stock;

    @ApiModelProperty(value = "状态：0-下架，1-上架")
    private Integer status;

    @ApiModelProperty(value = "推荐状态：0-不推荐，1-推荐")
    private Integer recommended;
    
    @ApiModelProperty(value = "是否有限时特惠：0-无，1-有")
    private Integer flashSaleActive;
    
    @ApiModelProperty(value = "限时特惠价格")
    private BigDecimal flashSalePrice;
    
    @ApiModelProperty(value = "限时特惠原价")
    private BigDecimal flashSaleOriginalPrice;
    
    @ApiModelProperty(value = "限时特惠开始时间")
    private java.util.Date flashSaleStartTime;
    
    @ApiModelProperty(value = "限时特惠结束时间")
    private java.util.Date flashSaleEndTime;
    
    @ApiModelProperty(value = "限时特惠库存")
    private Integer flashSaleStock;
    
    @ApiModelProperty(value = "限时特惠已售数量")
    private Integer flashSaleSoldCount;
}