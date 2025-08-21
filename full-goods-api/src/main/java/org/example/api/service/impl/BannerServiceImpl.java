package org.example.api.service.impl;

import org.example.api.vo.BannerVO;
import org.example.common.entity.Banner;
import org.example.api.mapper.BannerMapper;
import org.example.api.service.BannerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 轮播图服务实现类
 */
@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public List<BannerVO> getAllEnabled() {
        List<Banner> bannerList = bannerMapper.selectAllEnabled();
        return bannerList.stream().map(banner -> {
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(banner, bannerVO);
            // 为本地图片URL添加/api前缀
            bannerVO.setImageUrl(processImageUrl(bannerVO.getImageUrl()));
            return bannerVO;
        }).collect(Collectors.toList());
    }

    @Override
    public BannerVO getById(Long id) {
        Banner banner = bannerMapper.selectById(id);
        if (banner == null) {
            return null;
        }
        BannerVO bannerVO = new BannerVO();
        BeanUtils.copyProperties(banner, bannerVO);
        // 为本地图片URL添加/api前缀
        bannerVO.setImageUrl(processImageUrl(bannerVO.getImageUrl()));
        return bannerVO;
    }

    /**
     * 处理图片URL，为本地资源添加/images前缀以使用图片代理服务
     * @param imageUrl 原始图片URL
     * @return 处理后的图片URL
     */
    private String processImageUrl(String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            return imageUrl;
        }
        
        // 如果是外部链接（http或https开头），不添加前缀
        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            return imageUrl;
        }
        
        // 如果已经是/images开头，不重复添加
        if (imageUrl.startsWith("/images/")) {
            return imageUrl;
        }
        
        // 为本地资源添加/images前缀，通过图片代理服务访问
        if (imageUrl.startsWith("/")) {
            return "/images?url=" + imageUrl;
        } else {
            return "/images?url=/" + imageUrl;
        }
    }

    @Override
    public boolean add(Banner banner) {
        if (banner.getCreateTime() == null) {
            banner.setCreateTime(new Date());
        }
        if (banner.getUpdateTime() == null) {
            banner.setUpdateTime(new Date());
        }
        if (banner.getStatus() == null) {
            banner.setStatus(1); // 默认启用
        }
        return bannerMapper.insert(banner) > 0;
    }

    @Override
    public boolean update(Banner banner) {
        banner.setUpdateTime(new Date());
        return bannerMapper.update(banner) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return bannerMapper.deleteById(id) > 0;
    }
}