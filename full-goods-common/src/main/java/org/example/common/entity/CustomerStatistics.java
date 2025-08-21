package org.example.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 客服统计实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerStatistics extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 统计ID
     */
    private Long id;

    /**
     * 统计日期
     */
    private Date statisticsDate;

    /**
     * 客服ID
     */
    private Long serviceId;

    /**
     * 客服名称
     */
    private String serviceName;

    /**
     * 接待对话数
     */
    private Integer dialogCount;

    /**
     * 处理消息数
     */
    private Integer messageCount;

    /**
     * 平均响应时间（秒）
     */
    private Integer avgResponseTime;

    /**
     * 平均满意度评分
     */
    private Double avgSatisfaction;

    /**
     * 解决问题数
     */
    private Integer resolvedCount;

    /**
     * 在线时长（分钟）
     */
    private Integer onlineMinutes;

    /**
     * 工作效率评分
     */
    private Double efficiencyScore;

    /**
     * 备注
     */
    private String note;
}