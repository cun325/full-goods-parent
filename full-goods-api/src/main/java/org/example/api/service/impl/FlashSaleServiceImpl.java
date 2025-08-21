package org.example.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.api.mapper.FlashSaleMapper;
import org.example.api.service.FlashSaleService;
import org.example.api.vo.FlashSaleVO;
import org.example.common.entity.FlashSale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 限时特惠服务实现类
 */
@Slf4j
@Service
public class FlashSaleServiceImpl implements FlashSaleService {

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    @Value("${server.servlet.context-path:}")
    private String contextPath;
    
    @Value("${server.port:8081}")
    private String serverPort;

    @Override
    public List<FlashSaleVO> getActiveFlashSales() {
        List<FlashSaleVO> flashSales = flashSaleMapper.selectActiveFlashSales();
        return flashSales.stream().map(flashSale -> {
            // 处理图片URL
            flashSale.setImageUrl(processImageUrl(flashSale.getImageUrl()));
            return flashSale;
        }).collect(Collectors.toList());
    }

    @Override
    public FlashSale getById(Long id) {
        return flashSaleMapper.selectById(id);
    }

    @Override
    public boolean save(FlashSale flashSale) {
        return flashSaleMapper.insert(flashSale) > 0;
    }

    @Override
    public boolean update(FlashSale flashSale) {
        return flashSaleMapper.update(flashSale) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return flashSaleMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateSoldCount(Long id, Integer quantity) {
        return flashSaleMapper.updateSoldCount(id, quantity) > 0;
    }

    /**
     * 处理图片URL
     * 为本地图片URL添加代理前缀
     */
    private String processImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return imageUrl;
        }
        
        // 如果是外部链接（http或https开头），直接返回
        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            return imageUrl;
        }
        
        // 如果是本地图片，添加图片代理前缀
        return "/images?url=" + imageUrl;
    }
}