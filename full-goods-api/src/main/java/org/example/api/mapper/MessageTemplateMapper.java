package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.MessageTemplate;

import java.util.List;

/**
 * 消息模板Mapper接口
 */
@Mapper
public interface MessageTemplateMapper {

    /**
     * 根据模板编码查询模板
     *
     * @param code 模板编码
     * @return 消息模板
     */
    MessageTemplate selectByCode(String code);

    /**
     * 根据消息类型查询模板列表
     *
     * @param messageType 消息类型
     * @return 模板列表
     */
    List<MessageTemplate> selectByMessageType(Integer messageType);

    /**
     * 查询启用状态的模板列表
     *
     * @return 启用的模板列表
     */
    List<MessageTemplate> selectEnabled();

    /**
     * 根据ID查询模板
     *
     * @param id 模板ID
     * @return 消息模板
     */
    MessageTemplate selectById(Long id);

    /**
     * 查询所有模板
     *
     * @return 所有模板列表
     */
    List<MessageTemplate> selectAll();

    /**
     * 新增模板
     *
     * @param template 模板信息
     * @return 影响行数
     */
    int insert(MessageTemplate template);

    /**
     * 修改模板
     *
     * @param template 模板信息
     * @return 影响行数
     */
    int update(MessageTemplate template);

    /**
     * 更新模板状态
     *
     * @param id     模板ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 删除模板
     *
     * @param id 模板ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 批量删除模板
     *
     * @param ids 模板ID列表
     * @return 影响行数
     */
    int batchDelete(List<Long> ids);

    /**
     * 分页查询模板列表
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 模板列表
     */
    List<MessageTemplate> selectWithPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据条件查询模板列表（不分页）
     *
     * @param messageType 消息类型
     * @param status      状态
     * @return 模板列表
     */
    List<MessageTemplate> selectByCondition(@Param("messageType") Integer messageType, @Param("status") Integer status);

    /**
     * 根据条件分页查询模板列表
     *
     * @param messageType 消息类型
     * @param status      状态
     * @param offset      偏移量
     * @param limit       限制数量
     * @return 模板列表
     */
    List<MessageTemplate> selectByConditionWithPage(@Param("messageType") Integer messageType, @Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 统计模板总数
     *
     * @return 模板总数
     */
    int count();

    /**
     * 根据条件统计模板数量
     *
     * @param messageType 消息类型
     * @param status      状态
     * @return 模板数量
     */
    int countByCondition(@Param("messageType") Integer messageType, @Param("status") Integer status);

    /**
     * 检查模板编码是否存在
     *
     * @param code 模板编码
     * @param id   排除的模板ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByCode(@Param("code") String code, @Param("id") Long id);
}