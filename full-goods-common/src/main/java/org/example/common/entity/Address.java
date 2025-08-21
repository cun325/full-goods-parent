package org.example.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收货地址实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Address extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 地址ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区/县
     */
    private String district;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 是否默认地址：0-否，1-是
     */
    private Integer isDefault;
}