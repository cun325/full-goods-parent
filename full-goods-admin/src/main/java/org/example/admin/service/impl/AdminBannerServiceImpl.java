package org.example.admin.service.impl;

import org.example.admin.mapper.AdminBannerMapper;
import org.example.admin.service.AdminBannerService;
import org.example.common.entity.Banner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 轮播图管理服务实现类
 */
@Service
public class AdminBannerServiceImpl implements AdminBannerService {

    @Autowired
    private AdminBannerMapper adminBannerMapper;

    @Override
    public Map<String, Object> getBannerList(int page, int size, String search) {
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询总数
        int total = adminBannerMapper.countBanners(search);
        
        // 查询分页数据
        List<Banner> banners = adminBannerMapper.selectBannerList(offset, size, search);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", banners);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }

    @Override
    public Banner getBannerById(Long id) {
        return adminBannerMapper.selectById(id);
    }

    @Override
    public boolean addBanner(Banner banner) {
        banner.setCreateTime(new Date());
        banner.setUpdateTime(new Date());
        if (!StringUtils.hasText(banner.getCreateBy())) {
            banner.setCreateBy("admin");
        }
        if (!StringUtils.hasText(banner.getUpdateBy())) {
            banner.setUpdateBy("admin");
        }
        return adminBannerMapper.insert(banner) > 0;
    }

    @Override
    public boolean updateBanner(Banner banner) {
        banner.setUpdateTime(new Date());
        if (!StringUtils.hasText(banner.getUpdateBy())) {
            banner.setUpdateBy("admin");
        }
        return adminBannerMapper.update(banner) > 0;
    }

    @Override
    public boolean deleteBanner(Long id) {
        return adminBannerMapper.deleteById(id) > 0;
    }

    @Override
    public int batchDeleteBanners(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return adminBannerMapper.batchDeleteByIds(ids);
    }

    @Override
    public boolean toggleBannerStatus(Long id) {
        Banner banner = adminBannerMapper.selectById(id);
        if (banner == null) {
            return false;
        }
        
        // 切换状态：1-启用，0-禁用
        Integer newStatus = banner.getStatus() == 1 ? 0 : 1;
        banner.setStatus(newStatus);
        banner.setUpdateTime(new Date());
        banner.setUpdateBy("admin");
        
        return adminBannerMapper.update(banner) > 0;
    }

    @Override
    public int batchToggleBannerStatus(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        
        int updatedCount = 0;
        for (Long id : ids) {
            if (toggleBannerStatus(id)) {
                updatedCount++;
            }
        }
        return updatedCount;
    }
}