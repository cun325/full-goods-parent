package org.example.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 轮播图实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Banner extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 轮播图ID
     */
    private Long id;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 链接URL
     */
    private String linkUrl;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}