package org.example.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.Banner;

import java.util.List;

/**
 * 轮播图管理Mapper接口
 */
@Mapper
public interface AdminBannerMapper {

    /**
     * 查询轮播图总数
     *
     * @param search 搜索关键词
     * @return 总数
     */
    int countBanners(@Param("search") String search);

    /**
     * 分页查询轮播图列表
     *
     * @param offset 偏移量
     * @param size   每页大小
     * @param search 搜索关键词
     * @return 轮播图列表
     */
    List<Banner> selectBannerList(@Param("offset") int offset, @Param("size") int size, @Param("search") String search);

    /**
     * 根据ID查询轮播图
     *
     * @param id 轮播图ID
     * @return 轮播图信息
     */
    Banner selectById(@Param("id") Long id);

    /**
     * 新增轮播图
     *
     * @param banner 轮播图信息
     * @return 影响行数
     */
    int insert(Banner banner);

    /**
     * 更新轮播图
     *
     * @param banner 轮播图信息
     * @return 影响行数
     */
    int update(Banner banner);

    /**
     * 删除轮播图
     *
     * @param id 轮播图ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除轮播图
     *
     * @param ids 轮播图ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("ids") List<Long> ids);
}