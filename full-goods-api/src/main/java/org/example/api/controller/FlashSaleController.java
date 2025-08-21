package org.example.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.api.service.FlashSaleService;
import org.example.api.vo.FlashSaleVO;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 限时特惠控制器
 */
@Slf4j
@RestController
@RequestMapping("/flash-sale")
@Api(tags = "限时特惠接口")
public class FlashSaleController {

    @Autowired
    private FlashSaleService flashSaleService;

    @GetMapping("/list")
    @ApiOperation(value = "获取所有有效的限时特惠商品", notes = "获取当前时间段内有效的限时特惠商品列表")
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功"),
        @ApiResponse(code = 500, message = "获取失败")
    })
    public Result<List<FlashSaleVO>> getActiveFlashSales() {
        try {
            List<FlashSaleVO> flashSales = flashSaleService.getActiveFlashSales();
            return Result.success(flashSales);
        } catch (Exception e) {
            log.error("获取限时特惠商品失败", e);
            return Result.failed("获取限时特惠商品失败");
        }
    }
}