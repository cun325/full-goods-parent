package org.example.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 水果分类实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FruitCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 图标名称（Element Plus图标）
     */
    private String iconName;

    /**
     * 图标URL
     */
    private String iconUrl;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}