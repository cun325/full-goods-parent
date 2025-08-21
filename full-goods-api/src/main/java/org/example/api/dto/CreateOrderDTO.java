package org.example.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单DTO
 */
@Data
public class CreateOrderDTO {

    /**
     * 用户token
     */
    @NotBlank(message = "用户token不能为空")
    private String token;

    /**
     * 收货地址ID
     */
    @NotNull(message = "收货地址ID不能为空")
    private Long addressId;

    /**
     * 购物车ID列表，为空则下单所有购物车商品
     */
    private List<Long> cartIds;

    /**
     * 立即购买商品列表（用于立即购买功能）
     */
    private List<BuyNowItem> buyNowItems;

    /**
     * 立即购买商品项
     */
    @Data
    public static class BuyNowItem {
        /**
         * 商品ID
         */
        private Long fruitId;
        
        /**
         * 商品名称
         */
        private String name;
        
        /**
         * 商品价格
         */
        private BigDecimal price;
        
        /**
         * 商品图片
         */
        private String imageUrl;
        
        /**
         * 购买数量
         */
        private Integer quantity;
    }
}