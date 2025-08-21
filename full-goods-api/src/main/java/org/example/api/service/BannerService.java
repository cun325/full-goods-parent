package org.example.api.service;

import org.example.api.vo.BannerVO;
import org.example.common.entity.Banner;

import java.util.List;

/**
 * 轮播图服务接口
 */
public interface BannerService {

    /**
     * 查询所有启用的轮播图
     *
     * @return 轮播图列表
     */
    List<BannerVO> getAllEnabled();

    /**
     * 根据ID查询轮播图
     *
     * @param id 轮播图ID
     * @return 轮播图信息
     */
    BannerVO getById(Long id);

    /**
     * 新增轮播图
     *
     * @param banner 轮播图信息
     * @return 是否成功
     */
    boolean add(Banner banner);

    /**
     * 修改轮播图
     *
     * @param banner 轮播图信息
     * @return 是否成功
     */
    boolean update(Banner banner);

    /**
     * 删除轮播图
     *
     * @param id 轮播图ID
     * @return 是否成功
     */
    boolean delete(Long id);
}