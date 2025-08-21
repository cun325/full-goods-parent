package org.example.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 消息模板实体类
 */
@Data
public class MessageTemplate {
    
    /**
     * 模板ID
     */
    private Long id;
    
    /**
     * 模板编码
     */
    private String templateCode;
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 消息类型：1-物流通知，2-客服消息，3-系统通知
     */
    private Integer messageType;
    
    /**
     * 模板标题
     */
    private String title;
    
    /**
     * 模板内容
     */
    private String content;
    
    /**
     * 模板图标URL
     */
    private String iconUrl;
    
    /**
     * 跳转链接模板
     */
    private String linkTemplate;
    
    /**
     * 模板状态：1-启用，0-禁用
     */
    private Integer status;
    
    /**
     * 模板描述
     */
    private String description;
    
    /**
     * 参数说明（JSON格式）
     */
    private String paramDescription;
    
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
    public MessageTemplate() {}

    public MessageTemplate(String templateCode, String templateName, Integer messageType, String title, String content) {
        this.templateCode = templateCode;
        this.templateName = templateName;
        this.messageType = messageType;
        this.title = title;
        this.content = content;
        this.status = 1; // 默认启用
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    @Override
    public String toString() {
        return "MessageTemplate{" +
                "id=" + id +
                ", templateCode='" + templateCode + '\'' +
                ", templateName='" + templateName + '\'' +
                ", messageType=" + messageType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", linkTemplate='" + linkTemplate + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", paramDescription='" + paramDescription + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}