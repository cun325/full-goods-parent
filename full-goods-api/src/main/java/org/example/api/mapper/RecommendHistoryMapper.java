package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.RecommendHistory;

import java.util.List;

/**
 * 推荐历史Mapper接口
 */
@Mapper
public interface RecommendHistoryMapper {

    /**
     * 查询用户的推荐历史记录
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 历史记录列表
     */
    List<RecommendHistory> selectByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 新增推荐历史记录
     *
     * @param history 历史记录信息
     * @return 影响行数
     */
    int insert(RecommendHistory history);

    /**
     * 删除用户的推荐历史记录
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(Long userId);
    
    /**
     * 根据ID列表删除历史记录
     *
     * @param ids ID列表
     * @return 影响行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);
}