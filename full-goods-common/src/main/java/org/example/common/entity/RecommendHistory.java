package org.example.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 推荐历史记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RecommendHistory extends BaseEntity {

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 推荐条件
     */
    private String condition;

    /**
     * 创建时间
     */
    private Date createTime;
}