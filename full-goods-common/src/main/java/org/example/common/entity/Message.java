package org.example.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 消息实体类
 */
@Data
public class Message {
    
    /**
     * 消息ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 消息类型：1-物流通知，2-客服消息，3-系统通知
     */
    private Integer messageType;
    
    /**
     * 消息标题
     */
    private String title;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息状态：0-未读，1-已读
     */
    private Integer status;
    
    /**
     * 关联订单号（物流通知时使用）
     */
    private String orderNo;
    
    /**
     * 消息图标URL
     */
    private String iconUrl;
    
    /**
     * 跳转链接
     */
    private String linkUrl;
    
    /**
     * 发送者ID（客服消息时使用）
     */
    private Long senderId;
    
    /**
     * 发送者类型：1-系统，2-智能客服，3-人工客服
     */
    private Integer senderType;
    
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
    public Message() {}

    public Message(Long userId, Integer messageType, String title, String content) {
        this.userId = userId;
        this.messageType = messageType;
        this.title = title;
        this.content = content;
        this.status = 0; // 默认未读
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", userId=" + userId +
                ", messageType=" + messageType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", orderNo='" + orderNo + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", senderId=" + senderId +
                ", senderType=" + senderType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}