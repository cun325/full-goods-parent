package org.example.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 客服会话实体类
 */
@Data
public class CustomerServiceSession {
    
    /**
     * 会话ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 客服ID（人工客服时使用）
     */
    private Long serviceId;
    
    /**
     * 会话类型：1-智能客服，2-人工客服
     */
    private Integer sessionType;
    
    /**
     * 会话状态：1-进行中，2-已结束，3-等待中
     */
    private Integer status;
    
    /**
     * 会话标题
     */
    private String title;
    
    /**
     * 最后一条消息内容
     */
    private String lastMessage;
    
    /**
     * 最后消息时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastMessageTime;
    
    /**
     * 未读消息数
     */
    private Integer unreadCount;
    
    /**
     * 会话开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    /**
     * 会话结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    
    /**
     * 创建者
     */
    private String createBy;
    
    /**
     * 更新者
     */
    private String updateBy;
    
    /**
     * 备注
     */
    private String remark;

    // 构造函数
    public CustomerServiceSession() {}

    public CustomerServiceSession(Long userId, Integer sessionType, String title) {
        this.userId = userId;
        this.sessionType = sessionType;
        this.title = title;
        this.status = 1; // 默认进行中
        this.unreadCount = 0;
        this.startTime = new Date();
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    @Override
    public String toString() {
        return "CustomerServiceSession{" +
                "id=" + id +
                ", userId=" + userId +
                ", serviceId=" + serviceId +
                ", sessionType=" + sessionType +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageTime=" + lastMessageTime +
                ", unreadCount=" + unreadCount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}