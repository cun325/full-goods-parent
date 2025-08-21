package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * 消息Mapper接口
 */
@Mapper
public interface MessageMapper {

    /**
     * 根据用户ID查询消息列表
     *
     * @param userId 用户ID
     * @return 消息列表
     */
    List<Message> selectByUserId(Long userId);

    /**
     * 根据用户ID和消息类型查询消息列表
     *
     * @param userId      用户ID
     * @param messageType 消息类型
     * @return 消息列表
     */
    List<Message> selectByUserIdAndType(@Param("userId") Long userId, @Param("messageType") Integer messageType);

    /**
     * 根据用户ID查询未读消息列表
     *
     * @param userId 用户ID
     * @return 未读消息列表
     */
    List<Message> selectUnreadByUserId(Long userId);

    /**
     * 根据用户ID和消息类型查询未读消息数量
     *
     * @param userId      用户ID
     * @param messageType 消息类型
     * @return 未读消息数量
     */
    int countUnreadByUserIdAndType(@Param("userId") Long userId, @Param("messageType") Integer messageType);

    /**
     * 根据用户ID查询未读消息总数
     *
     * @param userId 用户ID
     * @return 未读消息总数
     */
    int countUnreadByUserId(Long userId);

    /**
     * 根据ID查询消息
     *
     * @param id 消息ID
     * @return 消息信息
     */
    Message selectById(Long id);

    /**
     * 新增消息
     *
     * @param message 消息信息
     * @return 影响行数
     */
    int insert(Message message);

    /**
     * 批量新增消息
     *
     * @param messages 消息列表
     * @return 影响行数
     */
    int batchInsert(List<Message> messages);

    /**
     * 修改消息
     *
     * @param message 消息信息
     * @return 影响行数
     */
    int update(Message message);

    /**
     * 标记消息为已读
     *
     * @param id 消息ID
     * @return 影响行数
     */
    int markAsRead(Long id);

    /**
     * 批量标记消息为已读
     *
     * @param ids 消息ID列表
     * @return 影响行数
     */
    int batchMarkAsRead(List<Long> ids);

    /**
     * 根据用户ID标记所有消息为已读
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int markAllAsReadByUserId(Long userId);

    /**
     * 根据用户ID和消息类型标记消息为已读
     *
     * @param userId      用户ID
     * @param messageType 消息类型
     * @return 影响行数
     */
    int markAsReadByUserIdAndType(@Param("userId") Long userId, @Param("messageType") Integer messageType);

    /**
     * 删除消息
     *
     * @param id 消息ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 批量删除消息
     *
     * @param ids 消息ID列表
     * @return 影响行数
     */
    int batchDelete(List<Long> ids);

    /**
     * 根据用户ID删除消息
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(Long userId);

    /**
     * 根据订单号查询物流消息
     *
     * @param orderNo 订单号
     * @return 物流消息列表
     */
    List<Message> selectByOrderNo(String orderNo);

    /**
     * 分页查询消息列表
     *
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 消息列表
     */
    List<Message> selectByUserIdWithPage(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 分页查询指定类型消息列表
     *
     * @param userId      用户ID
     * @param messageType 消息类型
     * @param offset      偏移量
     * @param limit       限制数量
     * @return 消息列表
     */
    List<Message> selectByUserIdAndTypeWithPage(@Param("userId") Long userId, @Param("messageType") Integer messageType, @Param("offset") int offset, @Param("limit") int limit);

    // ==================== 管理员相关查询方法 ====================

    /**
     * 根据条件查询所有用户消息列表（管理员）
     *
     * @param messageType 消息类型（可选）
     * @param status      消息状态（可选）
     * @param userId      用户ID（可选）
     * @return 消息列表
     */
    List<Message> selectAllWithFilters(@Param("messageType") Integer messageType, @Param("status") Integer status, @Param("userId") Long userId);

    /**
     * 统计所有消息数量
     *
     * @return 消息总数
     */
    int countAll();

    /**
     * 根据状态统计消息数量
     *
     * @param status 消息状态
     * @return 消息数量
     */
    int countByStatus(@Param("status") Integer status);

    /**
     * 根据消息类型统计消息数量
     *
     * @param messageType 消息类型
     * @return 消息数量
     */
    int countByType(@Param("messageType") Integer messageType);

    /**
     * 统计今日新增消息数量
     *
     * @return 今日消息数量
     */
    int countTodayMessages();

    /**
     * 获取用户聊天列表（按用户分组）
     * 返回每个用户的最新消息、未读数量、用户信息等
     *
     * @param keyword 搜索关键词（用户名、昵称、手机号）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 用户聊天列表
     */
    List<Map<String, Object>> selectUserChatList(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 获取用户聊天列表总数
     *
     * @param keyword 搜索关键词（用户名、昵称、手机号）
     * @return 用户聊天列表总数
     */
    int countUserChatList(@Param("keyword") String keyword);

    /**
     * 获取与特定用户的聊天记录（客服消息类型）
     *
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 聊天记录列表
     */
    List<Message> selectChatMessagesByUserId(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 获取与特定用户的聊天记录总数
     *
     * @param userId 用户ID
     * @return 聊天记录总数
     */
    int countChatMessagesByUserId(@Param("userId") Long userId);
}