package org.example.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.api.service.BannerService;
import org.example.api.vo.BannerVO;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 轮播图控制器
 */
@Slf4j
@RestController
@RequestMapping("/banner")
@Api(tags = "轮播图接口")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/list")
    @ApiOperation("获取所有启用的轮播图")
    public Result<List<BannerVO>> getAllEnabled() {
        try {
            List<BannerVO> banners = bannerService.getAllEnabled();
            return Result.success(banners);
        } catch (Exception e) {
            log.error("获取轮播图失败", e);
            return Result.failed("获取轮播图失败");
        }
    }
}