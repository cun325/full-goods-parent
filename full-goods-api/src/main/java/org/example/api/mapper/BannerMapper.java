package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.common.entity.Banner;

import java.util.List;

/**
 * 轮播图Mapper接口
 */
@Mapper
public interface BannerMapper {

    /**
     * 查询所有启用的轮播图
     *
     * @return 轮播图列表
     */
    List<Banner> selectAllEnabled();

    /**
     * 根据ID查询轮播图
     *
     * @param id 轮播图ID
     * @return 轮播图信息
     */
    Banner selectById(Long id);

    /**
     * 新增轮播图
     *
     * @param banner 轮播图信息
     * @return 影响行数
     */
    int insert(Banner banner);

    /**
     * 修改轮播图
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
    int deleteById(Long id);
}