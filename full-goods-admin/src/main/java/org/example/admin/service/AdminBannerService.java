package org.example.admin.service;

import org.example.common.entity.Banner;

import java.util.List;
import java.util.Map;

/**
 * 轮播图管理服务接口
 */
public interface AdminBannerService {

    /**
     * 获取轮播图列表（分页）
     *
     * @param page   页码
     * @param size   每页大小
     * @param search 搜索关键词
     * @return 分页结果
     */
    Map<String, Object> getBannerList(int page, int size, String search);

    /**
     * 根据ID获取轮播图详情
     *
     * @param id 轮播图ID
     * @return 轮播图信息
     */
    Banner getBannerById(Long id);

    /**
     * 新增轮播图
     *
     * @param banner 轮播图信息
     * @return 是否成功
     */
    boolean addBanner(Banner banner);

    /**
     * 更新轮播图
     *
     * @param banner 轮播图信息
     * @return 是否成功
     */
    boolean updateBanner(Banner banner);

    /**
     * 删除轮播图
     *
     * @param id 轮播图ID
     * @return 是否成功
     */
    boolean deleteBanner(Long id);

    /**
     * 批量删除轮播图
     *
     * @param ids 轮播图ID列表
     * @return 删除成功的数量
     */
    int batchDeleteBanners(List<Long> ids);

    /**
     * 切换轮播图状态
     *
     * @param id 轮播图ID
     * @return 是否成功
     */
    boolean toggleBannerStatus(Long id);

    /**
     * 批量切换轮播图状态
     *
     * @param ids 轮播图ID列表
     * @return 更新成功的数量
     */
    int batchToggleBannerStatus(List<Long> ids);
}