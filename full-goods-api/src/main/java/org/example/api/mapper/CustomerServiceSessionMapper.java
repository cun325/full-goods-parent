package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.CustomerServiceSession;

import java.util.List;

/**
 * 客服会话Mapper接口
 */
@Mapper
public interface CustomerServiceSessionMapper {

    /**
     * 根据用户ID查询会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<CustomerServiceSession> selectByUserId(Long userId);

    /**
     * 根据用户ID查询进行中的会话
     *
     * @param userId 用户ID
     * @return 进行中的会话
     */
    CustomerServiceSession selectActiveByUserId(Long userId);

    /**
     * 根据客服ID查询会话列表
     *
     * @param serviceId 客服ID
     * @return 会话列表
     */
    List<CustomerServiceSession> selectByServiceId(Long serviceId);

    /**
     * 根据会话类型查询等待中的会话
     *
     * @param sessionType 会话类型
     * @return 等待中的会话列表
     */
    List<CustomerServiceSession> selectWaitingByType(Integer sessionType);

    /**
     * 根据ID查询会话
     *
     * @param id 会话ID
     * @return 会话信息
     */
    CustomerServiceSession selectById(Long id);

    /**
     * 新增会话
     *
     * @param session 会话信息
     * @return 影响行数
     */
    int insert(CustomerServiceSession session);

    /**
     * 修改会话
     *
     * @param session 会话信息
     * @return 影响行数
     */
    int update(CustomerServiceSession session);

    /**
     * 更新会话状态
     *
     * @param id     会话ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新最后消息信息
     *
     * @param id          会话ID
     * @param lastMessage 最后消息内容
     * @return 影响行数
     */
    int updateLastMessage(@Param("id") Long id, @Param("lastMessage") String lastMessage);

    /**
     * 增加未读消息数
     *
     * @param id    会话ID
     * @param count 增加数量
     * @return 影响行数
     */
    int increaseUnreadCount(@Param("id") Long id, @Param("count") Integer count);

    /**
     * 清零未读消息数
     *
     * @param id 会话ID
     * @return 影响行数
     */
    int clearUnreadCount(Long id);

    /**
     * 分配客服
     *
     * @param id        会话ID
     * @param serviceId 客服ID
     * @return 影响行数
     */
    int assignService(@Param("id") Long id, @Param("serviceId") Long serviceId);

    /**
     * 删除会话
     *
     * @param id 会话ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据用户ID删除会话
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(Long userId);

    /**
     * 分页查询会话列表
     *
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 会话列表
     */
    List<CustomerServiceSession> selectByUserIdWithPage(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 统计用户会话数量
     *
     * @param userId 用户ID
     * @return 会话数量
     */
    int countByUserId(Long userId);

    /**
     * 统计客服会话数量
     *
     * @param serviceId 客服ID
     * @return 会话数量
     */
    int countByServiceId(Long serviceId);
}