package org.example.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.common.entity.Message;
import org.example.common.entity.MessageTemplate;
import org.example.api.mapper.MessageMapper;
import org.example.api.mapper.MessageTemplateMapper;
import org.example.api.service.MessageService;
import org.example.api.service.WebSocketMessageService;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 消息服务实现类
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Autowired
    private WebSocketMessageService webSocketMessageService;

    @Override
    @Transactional
    public Result<Boolean> sendMessage(Message message) {
        try {
            if (message == null) {
                return Result.failed("消息对象不能为空");
            }
            
            // 设置默认值
            message.setCreateTime(new Date());
            message.setUpdateTime(new Date());
            message.setStatus(0); // 0-未读
            
            int result = messageMapper.insert(message);
            if (result > 0) {
                // 发送 WebSocket 推送通知
                try {
                    Map<String, Object> wsMessage = new HashMap<>();
                    wsMessage.put("type", "new_message");
                    wsMessage.put("messageId", message.getId());
                    wsMessage.put("title", message.getTitle());
                    wsMessage.put("content", message.getContent());
                    wsMessage.put("messageType", message.getMessageType());
                    wsMessage.put("createTime", message.getCreateTime());
                    wsMessage.put("showNotification", true);
                    
                    webSocketMessageService.sendMessageToUser(message.getUserId(), wsMessage);
                    log.info("WebSocket推送消息成功，用户ID: {}, 消息ID: {}", message.getUserId(), message.getId());
                } catch (Exception e) {
                    log.error("WebSocket推送消息失败，用户ID: {}, 消息ID: {}", message.getUserId(), message.getId(), e);
                    // WebSocket推送失败不影响消息发送结果
                }
                return Result.success(true, "消息发送成功");
            } else {
                return Result.failed("消息发送失败");
            }
        } catch (Exception e) {
            log.error("发送消息失败", e);
            return Result.failed("消息发送失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> sendBatchMessages(List<Message> messages) {
        try {
            if (messages == null || messages.isEmpty()) {
                return Result.failed("消息列表不能为空");
            }
            
            Date now = new Date();
            for (Message message : messages) {
                message.setCreateTime(now);
                message.setUpdateTime(now);
                message.setStatus(0); // 0-未读
            }
            
            int result = messageMapper.batchInsert(messages);
            if (result > 0) {
                // 批量发送 WebSocket 推送通知
                for (Message message : messages) {
                    try {
                        Map<String, Object> wsMessage = new HashMap<>();
                        wsMessage.put("type", "new_message");
                        wsMessage.put("messageId", message.getId());
                        wsMessage.put("title", message.getTitle());
                        wsMessage.put("content", message.getContent());
                        wsMessage.put("messageType", message.getMessageType());
                        wsMessage.put("createTime", message.getCreateTime());
                        wsMessage.put("showNotification", true);
                        
                        webSocketMessageService.sendMessageToUser(message.getUserId(), wsMessage);
                        log.info("批量WebSocket推送消息成功，用户ID: {}, 消息ID: {}", message.getUserId(), message.getId());
                    } catch (Exception e) {
                        log.error("批量WebSocket推送消息失败，用户ID: {}, 消息ID: {}", message.getUserId(), message.getId(), e);
                        // WebSocket推送失败不影响消息发送结果
                    }
                }
                return Result.success(true, "批量消息发送成功");
            } else {
                return Result.failed("批量消息发送失败");
            }
        } catch (Exception e) {
            log.error("批量发送消息失败", e);
            return Result.failed("批量消息发送失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> sendMessageByTemplate(String templateCode, Long userId, Map<String, Object> params) {
        try {
            if (!StringUtils.hasText(templateCode) || userId == null) {
                return Result.failed("模板编码和用户ID不能为空");
            }
            
            // 获取模板
            MessageTemplate template = messageTemplateMapper.selectByCode(templateCode);
            if (template == null) {
                return Result.failed("消息模板不存在");
            }
            
            if (template.getStatus() != 1) {
                return Result.failed("消息模板未启用");
            }
            
            // 渲染模板内容
            String title = renderTemplateContent(template.getTitle(), params);
            String content = renderTemplateContent(template.getContent(), params);
            String linkUrl = renderTemplateContent(template.getLinkTemplate(), params);
            
            // 创建消息
            Message message = new Message();
            message.setUserId(userId);
            message.setMessageType(template.getMessageType());
            message.setTitle(title);
            message.setContent(content);
            message.setIconUrl(template.getIconUrl());
            message.setLinkUrl(linkUrl);
            message.setStatus(0); // 0-未读
            message.setCreateTime(new Date());
            message.setUpdateTime(new Date());
            
            int result = messageMapper.insert(message);
            if (result > 0) {
                // 发送 WebSocket 推送通知
                try {
                    Map<String, Object> wsMessage = new HashMap<>();
                    wsMessage.put("type", "new_message");
                    wsMessage.put("messageId", message.getId());
                    wsMessage.put("title", message.getTitle());
                    wsMessage.put("content", message.getContent());
                    wsMessage.put("messageType", message.getMessageType());
                    wsMessage.put("createTime", message.getCreateTime());
                    wsMessage.put("showNotification", true);
                    
                    webSocketMessageService.sendMessageToUser(message.getUserId(), wsMessage);
                    log.info("模板消息WebSocket推送成功，用户ID: {}, 消息ID: {}", message.getUserId(), message.getId());
                } catch (Exception e) {
                    log.error("模板消息WebSocket推送失败，用户ID: {}, 消息ID: {}", message.getUserId(), message.getId(), e);
                    // WebSocket推送失败不影响消息发送结果
                }
                return Result.success(true, "模板消息发送成功");
            } else {
                return Result.failed("模板消息发送失败");
            }
        } catch (Exception e) {
            log.error("根据模板发送消息失败", e);
            return Result.failed("模板消息发送失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> sendLogisticsNotification(Long userId, String orderNo, String status, String content) {
        try {
            Message message = new Message();
            message.setUserId(userId);
            message.setMessageType(1); // 1-物流通知
            message.setTitle("物流通知");
            message.setContent(content);
            message.setOrderNo(orderNo);
            message.setIconUrl("/icons/logistics.png");
            message.setStatus(0); // 0-未读
            message.setCreateTime(new Date());
            message.setUpdateTime(new Date());
            
            return sendMessage(message);
        } catch (Exception e) {
            log.error("发送物流通知失败", e);
            return Result.failed("发送物流通知失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> sendSystemNotification(Long userId, String title, String content, String iconUrl, String linkUrl, Integer messageType) {
        try {
            Message message = new Message();
            message.setUserId(userId);
            message.setMessageType(messageType); // 使用传入的消息类型
            message.setTitle(title);
            message.setContent(content);
            message.setIconUrl(iconUrl);
            message.setLinkUrl(linkUrl);
            message.setStatus(0); // 0-未读
            message.setCreateTime(new Date());
            message.setUpdateTime(new Date());
            
            return sendMessage(message);
        } catch (Exception e) {
            log.error("发送系统通知失败", e);
            return Result.failed("发送系统通知失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> sendPromotionNotification(Long userId, String activityTitle, String activityContent, String iconUrl, String linkUrl) {
        try {
            Message message = new Message();
            message.setUserId(userId);
            message.setMessageType(4); // 4-优惠活动
            message.setTitle(activityTitle);
            message.setContent(activityContent);
            message.setIconUrl(iconUrl);
            message.setLinkUrl(linkUrl);
            message.setStatus(0); // 0-未读
            message.setCreateTime(new Date());
            message.setUpdateTime(new Date());
            
            return sendMessage(message);
        } catch (Exception e) {
            log.error("发送优惠活动通知失败", e);
            return Result.failed("发送优惠活动通知失败: " + e.getMessage());
        }
    }

    @Override
    public Result<PageInfo<Message>> getUserMessages(Long userId, Integer page, Integer size) {
        try {
            if (userId == null) {
                return Result.failed("用户ID不能为空");
            }
            
            PageHelper.startPage(page != null ? page : 1, size != null ? size : 10);
            List<Message> messages = messageMapper.selectByUserId(userId);
            PageInfo<Message> pageInfo = new PageInfo<>(messages);
            
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("获取用户消息列表失败", e);
            return Result.failed("获取用户消息列表失败: " + e.getMessage());
        }
    }

    @Override
    public Result<PageInfo<Message>> getUserMessagesByType(Long userId, Integer messageType, Integer page, Integer size) {
        try {
            if (userId == null || messageType == null) {
                return Result.failed("用户ID和消息类型不能为空");
            }
            
            PageHelper.startPage(page != null ? page : 1, size != null ? size : 10);
            List<Message> messages = messageMapper.selectByUserIdAndType(userId, messageType);
            PageInfo<Message> pageInfo = new PageInfo<>(messages);
            
            log.info("获取用户{}的{}类型消息，共{}条", userId, messageType, pageInfo.getTotal());
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("根据类型获取用户消息列表失败", e);
            return Result.failed("获取用户消息列表失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Message>> getUnreadMessages(Long userId) {
        try {
            if (userId == null) {
                return Result.failed("用户ID不能为空");
            }
            
            List<Message> messages = messageMapper.selectUnreadByUserId(userId);
            return Result.success(messages);
        } catch (Exception e) {
            log.error("获取用户未读消息失败", e);
            return Result.failed("获取用户未读消息失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Message>> getUnreadMessagesByType(Long userId, Integer messageType) {
        try {
            if (userId == null || messageType == null) {
                return Result.failed("用户ID和消息类型不能为空");
            }
            
            // 需要实现这个方法，暂时返回空列表
            List<Message> messages = new ArrayList<>();
            return Result.success(messages);
        } catch (Exception e) {
            log.error("根据类型获取用户未读消息失败", e);
            return Result.failed("获取用户未读消息失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Integer> getUnreadMessageCount(Long userId) {
        try {
            if (userId == null) {
                return Result.failed("用户ID不能为空");
            }
            
            int count = messageMapper.countUnreadByUserId(userId);
            return Result.success(count);
        } catch (Exception e) {
            log.error("获取用户未读消息数量失败", e);
            return Result.failed("获取用户未读消息数量失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Integer> getUnreadMessageCountByType(Long userId, Integer messageType) {
        try {
            if (userId == null || messageType == null) {
                return Result.failed("用户ID和消息类型不能为空");
            }
            
            int count = messageMapper.countUnreadByUserIdAndType(userId, messageType);
            return Result.success(count);
        } catch (Exception e) {
            log.error("根据类型获取用户未读消息数量失败", e);
            return Result.failed("获取用户未读消息数量失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Integer> getUnreadCustomerServiceMessageCountByTitle(Long userId, String title) {
        try {
            if (userId == null || title == null) {
                return Result.failed("用户ID和消息标题不能为空");
            }
            
            int count = messageMapper.countUnreadByUserIdAndTitle(userId, title);
            return Result.success(count);
        } catch (Exception e) {
            log.error("根据标题获取用户未读客服消息数量失败", e);
            return Result.failed("获取用户未读客服消息数量失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> markMessageAsRead(Long messageId, Long userId) {
        try {
            if (messageId == null || userId == null) {
                return Result.failed("消息ID和用户ID不能为空");
            }
            
            int result = messageMapper.markAsRead(messageId);
            if (result > 0) {
                // 发送 WebSocket 推送通知消息状态更新
                try {
                    Map<String, Object> wsMessage = new HashMap<>();
                    wsMessage.put("type", "message_read");
                    wsMessage.put("messageId", messageId);
                    wsMessage.put("userId", userId);
                    wsMessage.put("timestamp", System.currentTimeMillis());
                    
                    webSocketMessageService.sendMessageToUser(userId, wsMessage);
                    log.info("消息已读状态WebSocket推送成功，用户ID: {}, 消息ID: {}", userId, messageId);
                } catch (Exception e) {
                    log.error("消息已读状态WebSocket推送失败，用户ID: {}, 消息ID: {}", userId, messageId, e);
                    // WebSocket推送失败不影响标记已读结果
                }
                return Result.success(true, "消息已标记为已读");
            } else {
                return Result.failed("标记消息已读失败");
            }
        } catch (Exception e) {
            log.error("标记消息已读失败", e);
            return Result.failed("标记消息已读失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> markMessagesAsRead(List<Long> messageIds, Long userId) {
        try {
            if (messageIds == null || messageIds.isEmpty() || userId == null) {
                return Result.failed("消息ID列表和用户ID不能为空");
            }
            
            int result = messageMapper.batchMarkAsRead(messageIds);
            if (result > 0) {
                // 发送 WebSocket 推送通知批量消息状态更新
                try {
                    Map<String, Object> wsMessage = new HashMap<>();
                    wsMessage.put("type", "messages_read");
                    wsMessage.put("messageIds", messageIds);
                    wsMessage.put("userId", userId);
                    wsMessage.put("timestamp", System.currentTimeMillis());
                    
                    webSocketMessageService.sendMessageToUser(userId, wsMessage);
                    log.info("批量消息已读状态WebSocket推送成功，用户ID: {}, 消息数量: {}", userId, messageIds.size());
                } catch (Exception e) {
                    log.error("批量消息已读状态WebSocket推送失败，用户ID: {}, 消息数量: {}", userId, messageIds.size(), e);
                    // WebSocket推送失败不影响标记已读结果
                }
                return Result.success(true, "批量标记消息已读成功");
            } else {
                return Result.failed("批量标记消息已读失败");
            }
        } catch (Exception e) {
            log.error("批量标记消息已读失败", e);
            return Result.failed("批量标记消息已读失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> markAllMessagesAsRead(Long userId) {
        try {
            if (userId == null) {
                return Result.failed("用户ID不能为空");
            }
            
            int result = messageMapper.markAllAsReadByUserId(userId);
            return result > 0 ? Result.success(true, "全部消息已标记为已读") : Result.failed("标记全部消息已读失败");
        } catch (Exception e) {
            log.error("标记全部消息已读失败", e);
            return Result.failed("标记全部消息已读失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> markAllMessagesByTypeAsRead(Long userId, Integer messageType) {
        try {
            if (userId == null || messageType == null) {
                return Result.failed("用户ID和消息类型不能为空");
            }
            
            int result = messageMapper.markAsReadByUserIdAndType(userId, messageType);
            return result > 0 ? Result.success(true, "该类型全部消息已标记为已读") : Result.failed("标记该类型全部消息已读失败");
        } catch (Exception e) {
            log.error("根据类型标记全部消息已读失败", e);
            return Result.failed("标记该类型全部消息已读失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> deleteMessage(Long messageId, Long userId) {
        try {
            if (messageId == null || userId == null) {
                return Result.failed("消息ID和用户ID不能为空");
            }
            
            int result = messageMapper.deleteById(messageId);
            return result > 0 ? Result.success(true, "消息删除成功") : Result.failed("消息删除失败");
        } catch (Exception e) {
            log.error("删除消息失败", e);
            return Result.failed("删除消息失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> deleteMessages(List<Long> messageIds, Long userId) {
        try {
            if (messageIds == null || messageIds.isEmpty() || userId == null) {
                return Result.failed("消息ID列表和用户ID不能为空");
            }
            
            int result = messageMapper.batchDelete(messageIds);
            return result > 0 ? Result.success(true, "批量删除消息成功") : Result.failed("批量删除消息失败");
        } catch (Exception e) {
            log.error("批量删除消息失败", e);
            return Result.failed("批量删除消息失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Message> getMessageDetail(Long messageId, Long userId) {
        try {
            if (messageId == null || userId == null) {
                return Result.failed("消息ID和用户ID不能为空");
            }
            
            Message message = messageMapper.selectById(messageId);
            if (message == null) {
                return Result.failed("消息不存在或无权限访问");
            }
            
            return Result.success(message);
        } catch (Exception e) {
            log.error("获取消息详情失败", e);
            return Result.failed("获取消息详情失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Message>> getLogisticsMessagesByOrderNo(Long userId, String orderNo) {
        try {
            if (userId == null || !StringUtils.hasText(orderNo)) {
                return Result.failed("用户ID和订单号不能为空");
            }
            
            List<Message> messages = messageMapper.selectByOrderNo(orderNo);
            return Result.success(messages);
        } catch (Exception e) {
            log.error("根据订单号获取物流消息失败", e);
            return Result.failed("获取物流消息失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getMessageStatistics(Long userId) {
        try {
            if (userId == null) {
                return Result.failed("用户ID不能为空");
            }
            
            Map<String, Object> statistics = new HashMap<>();
            
            // 总消息数 - 通过查询所有消息计算
            List<Message> allMessages = messageMapper.selectByUserId(userId);
            int totalCount = allMessages.size();
            statistics.put("totalCount", totalCount);
            
            // 未读消息数
            int unreadCount = messageMapper.countUnreadByUserId(userId);
            statistics.put("unreadCount", unreadCount);
            
            // 各类型消息数量
            Map<String, Integer> typeCount = new HashMap<>();
            typeCount.put("logistics", messageMapper.countUnreadByUserIdAndType(userId, 1)); // 1-物流
            typeCount.put("system", messageMapper.countUnreadByUserIdAndType(userId, 3)); // 3-系统
            typeCount.put("promotion", messageMapper.countUnreadByUserIdAndType(userId, 4)); // 4-优惠
            typeCount.put("service", messageMapper.countUnreadByUserIdAndType(userId, 2)); // 2-客服
            statistics.put("typeCount", typeCount);
            
            // 各类型未读消息数量
            Map<String, Integer> unreadTypeCount = new HashMap<>();
            unreadTypeCount.put("logistics", messageMapper.countUnreadByUserIdAndType(userId, 1));
            unreadTypeCount.put("system", messageMapper.countUnreadByUserIdAndType(userId, 3));
            unreadTypeCount.put("promotion", messageMapper.countUnreadByUserIdAndType(userId, 4));
            unreadTypeCount.put("service", messageMapper.countUnreadByUserIdAndType(userId, 2));
            statistics.put("unreadTypeCount", unreadTypeCount);
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取消息统计信息失败", e);
            return Result.failed("获取消息统计信息失败: " + e.getMessage());
        }
    }

    // ==================== 管理员相关接口实现 ====================

    @Override
    public Result<PageInfo<Message>> getAllUserMessages(Integer page, Integer size, Integer messageType, Integer status, Long userId) {
        try {
            if (page == null || page < 1) page = 1;
            if (size == null || size < 1) size = 10;
            
            PageHelper.startPage(page, size);
            List<Message> messages = messageMapper.selectAllWithFilters(messageType, status, userId);
            PageInfo<Message> pageInfo = new PageInfo<>(messages);
            
            return Result.success(pageInfo, "获取消息列表成功");
        } catch (Exception e) {
            log.error("获取所有用户消息列表失败", e);
            return Result.failed("获取消息列表失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Message> getAdminMessageDetail(Long messageId) {
        try {
            if (messageId == null) {
                return Result.failed("消息ID不能为空");
            }
            
            Message message = messageMapper.selectById(messageId);
            if (message == null) {
                return Result.failed("消息不存在");
            }
            
            return Result.success(message, "获取消息详情成功");
        } catch (Exception e) {
            log.error("获取消息详情失败，消息ID: {}", messageId, e);
            return Result.failed("获取消息详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> replyMessage(Long messageId, String replyContent, Long adminId) {
        try {
            if (messageId == null || !StringUtils.hasText(replyContent) || adminId == null) {
                return Result.failed("消息ID、回复内容和管理员ID不能为空");
            }
            
            // 获取原消息
            Message originalMessage = messageMapper.selectById(messageId);
            if (originalMessage == null) {
                return Result.failed("原消息不存在");
            }
            
            // 创建回复消息
            Message replyMessage = new Message();
            replyMessage.setUserId(originalMessage.getUserId());
            replyMessage.setMessageType(2); // 2-客服回复
            replyMessage.setTitle(originalMessage.getTitle());
            replyMessage.setContent(replyContent);
            replyMessage.setStatus(0); // 0-未读
            replyMessage.setCreateTime(new Date());
            replyMessage.setUpdateTime(new Date());
            
            int result = messageMapper.insert(replyMessage);
            if (result > 0) {
                // 发送 WebSocket 推送通知
                try {
                    Map<String, Object> wsMessage = new HashMap<>();
                    wsMessage.put("type", "customer_service_reply");
                    wsMessage.put("messageId", replyMessage.getId());
                    wsMessage.put("title", replyMessage.getTitle());
                    wsMessage.put("content", replyMessage.getContent());
                    wsMessage.put("createTime", replyMessage.getCreateTime());
                    wsMessage.put("showNotification", true);
                    
                    webSocketMessageService.sendMessageToUser(originalMessage.getUserId(), wsMessage);
                    log.info("客服回复WebSocket推送成功，用户ID: {}, 消息ID: {}", originalMessage.getUserId(), replyMessage.getId());
                } catch (Exception e) {
                    log.error("客服回复WebSocket推送失败，用户ID: {}, 消息ID: {}", originalMessage.getUserId(), replyMessage.getId(), e);
                }
                return Result.success(true, "回复消息成功");
            } else {
                return Result.failed("回复消息失败");
            }
        } catch (Exception e) {
            log.error("回复消息失败，消息ID: {}", messageId, e);
            return Result.failed("回复消息失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> markMessageRead(Long messageId) {
        try {
            if (messageId == null) {
                return Result.failed("消息ID不能为空");
            }
            
            int result = messageMapper.markAsRead(messageId);
            if (result > 0) {
                return Result.success(true, "标记消息已读成功");
            } else {
                return Result.failed("标记消息已读失败");
            }
        } catch (Exception e) {
            log.error("标记消息已读失败，消息ID: {}", messageId, e);
            return Result.failed("标记消息已读失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> batchMarkMessageRead(List<Long> messageIds) {
        try {
            if (messageIds == null || messageIds.isEmpty()) {
                return Result.failed("消息ID列表不能为空");
            }
            
            int result = messageMapper.batchMarkAsRead(messageIds);
            if (result > 0) {
                return Result.success(true, "批量标记消息已读成功");
            } else {
                return Result.failed("批量标记消息已读失败");
            }
        } catch (Exception e) {
            log.error("批量标记消息已读失败", e);
            return Result.failed("批量标记消息已读失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Boolean> batchDeleteMessages(List<Long> messageIds) {
        try {
            if (messageIds == null || messageIds.isEmpty()) {
                return Result.failed("消息ID列表不能为空");
            }
            
            int result = messageMapper.batchDelete(messageIds);
            if (result > 0) {
                return Result.success(true, "批量删除消息成功");
            } else {
                return Result.failed("批量删除消息失败");
            }
        } catch (Exception e) {
            log.error("批量删除消息失败", e);
            return Result.failed("批量删除消息失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getAdminMessageStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 总消息数
            int totalMessages = messageMapper.countAll();
            statistics.put("totalMessages", totalMessages);
            
            // 未读消息数
            int unreadMessages = messageMapper.countByStatus(0);
            statistics.put("unreadMessages", unreadMessages);
            
            // 已读消息数
            int readMessages = messageMapper.countByStatus(1);
            statistics.put("readMessages", readMessages);
            
            // 按类型统计
            Map<String, Integer> messageTypeStats = new HashMap<>();
            messageTypeStats.put("systemNotification", messageMapper.countByType(1)); // 系统通知
            messageTypeStats.put("orderNotification", messageMapper.countByType(2)); // 订单通知
            messageTypeStats.put("promotionNotification", messageMapper.countByType(3)); // 优惠活动
            messageTypeStats.put("customerServiceReply", messageMapper.countByType(4)); // 客服回复
            statistics.put("messageTypeStats", messageTypeStats);
            
            // 今日新增消息数
            int todayMessages = messageMapper.countTodayMessages();
            statistics.put("todayMessages", todayMessages);
            
            return Result.success(statistics, "获取消息统计信息成功");
        } catch (Exception e) {
            log.error("获取管理员消息统计信息失败", e);
            return Result.failed("获取消息统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 渲染模板内容
     * @param template 模板字符串
     * @param params 参数映射
     * @return 渲染后的内容
     */
    private String renderTemplateContent(String template, Map<String, Object> params) {
        if (!StringUtils.hasText(template) || params == null || params.isEmpty()) {
            return template;
        }
        
        String result = template;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        
        return result;
    }

    @Override
    public Result<PageInfo<Map<String, Object>>> getUserChatList(Integer page, Integer size, String keyword) {
        try {
            if (page == null || page < 1) page = 1;
            if (size == null || size < 1) size = 10;
            
            int offset = (page - 1) * size;
            List<Map<String, Object>> userChatList = messageMapper.selectUserChatList(keyword, offset, size);
            int totalCount = messageMapper.countUserChatList(keyword);
            
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>();
            pageInfo.setList(userChatList);
            pageInfo.setTotal(totalCount);
            pageInfo.setPageNum(page);
            pageInfo.setPageSize(size);
            pageInfo.setPages((int) Math.ceil((double) totalCount / size));
            pageInfo.setHasNextPage(page < pageInfo.getPages());
            pageInfo.setHasPreviousPage(page > 1);
            
            return Result.success(pageInfo, "获取用户聊天列表成功");
        } catch (Exception e) {
            log.error("获取用户聊天列表失败", e);
            return Result.failed("获取用户聊天列表失败: " + e.getMessage());
        }
    }

    @Override
    public Result<PageInfo<Message>> getUserChatMessages(Long userId, Integer page, Integer size) {
        try {
            if (userId == null) {
                return Result.failed("用户ID不能为空");
            }
            if (page == null || page < 1) page = 1;
            if (size == null || size < 1) size = 10;
            
            int offset = (page - 1) * size;
            List<Message> messages = messageMapper.selectChatMessagesByUserId(userId, offset, size);
            int totalCount = messageMapper.countChatMessagesByUserId(userId);
            
            PageInfo<Message> pageInfo = new PageInfo<>();
            pageInfo.setList(messages);
            pageInfo.setTotal(totalCount);
            pageInfo.setPageNum(page);
            pageInfo.setPageSize(size);
            pageInfo.setPages((int) Math.ceil((double) totalCount / size));
            pageInfo.setHasNextPage(page < pageInfo.getPages());
            pageInfo.setHasPreviousPage(page > 1);
            
            return Result.success(pageInfo, "获取用户聊天记录成功");
        } catch (Exception e) {
            log.error("获取用户聊天记录失败，用户ID: {}", userId, e);
            return Result.failed("获取用户聊天记录失败: " + e.getMessage());
        }
    }
}