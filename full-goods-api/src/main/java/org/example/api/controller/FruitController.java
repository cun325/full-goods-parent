package org.example.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.FruitRecommendDTO;
import org.example.api.mapper.FruitMapper;
import org.example.api.service.FruitService;
import org.example.api.service.UserService;
import org.example.api.vo.CategoryVO;
import org.example.api.vo.FruitVO;
import org.example.api.vo.HomeVO;
import org.example.api.vo.RecommendHistoryVO;
import org.example.common.entity.Fruit;
import org.example.common.entity.User;
import org.example.common.entity.FlashSale;
import org.example.common.exception.BusinessException;
import org.example.common.response.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 水果控制器
 */
@Slf4j
@RestController
@RequestMapping("/fruit")
@Api(tags = "水果接口")
public class FruitController {

    @Autowired
    private FruitService fruitService;

    @Autowired
    private UserService userService;

    @Autowired
    private FruitMapper fruitMapper;

    @GetMapping("/list")
    @ApiOperation(value = "查询水果列表", notes = "支持按关键词、分类、状态筛选水果")
    @ApiResponses({
        @ApiResponse(code = 200, message = "查询成功"),
        @ApiResponse(code = 500, message = "查询失败")
    })
    public Result<List<FruitVO>> getAllFruits(
            @ApiParam(value = "搜索关键词，支持水果名称模糊搜索", required = false)
            @RequestParam(required = false) String search,
            @ApiParam(value = "水果分类名称", required = false)
            @RequestParam(required = false) String category,
            @ApiParam(value = "水果状态：1-上架，0-下架", required = false)
            @RequestParam(required = false) Integer status) {
        
        try {
            // 使用新的联表查询方法，包含限时特惠信息
            List<FruitVO> fruits = fruitMapper.selectWithFlashSaleInfo(search, category, status);
            
            // 转换为VO
         //   List<FruitVO> voList = convertToVOList(fruits);
            
            return Result.success(fruits);
        } catch (Exception e) {
            log.error("查询水果列表失败", e);
            return Result.failed("查询失败");
        }
    }

    @GetMapping("/admin/list")
    @ApiOperation("分页查询水果列表（管理后台专用）")
    public Result<PageInfo<FruitVO>> getAdminFruitList(
            @ApiParam(value = "页码", required = false)
            @RequestParam(defaultValue = "1") int page,
            @ApiParam(value = "每页大小", required = false)
            @RequestParam(defaultValue = "10") int size,
            @ApiParam(value = "搜索关键词", required = false)
            @RequestParam(required = false) String search,
            @ApiParam(value = "分类", required = false)
            @RequestParam(required = false) String category,
            @ApiParam(value = "状态", required = false)
            @RequestParam(required = false) Integer status) {
        
        try {
            // 开启分页
            PageHelper.startPage(page, size);
            
            // 使用新的联表查询方法，包含限时特惠信息
            List<FruitVO> fruits = fruitMapper.selectWithFlashSaleInfo(search, category, status);
            
            // 转换为VO
          //  List<FruitVO> voList = convertToVOList(fruits);
            
            // 创建分页信息
            PageInfo<FruitVO> fruitPageInfo = new PageInfo<>(fruits);
            PageInfo<FruitVO> pageInfo = new PageInfo<>();
            BeanUtils.copyProperties(fruitPageInfo, pageInfo);
            pageInfo.setList(fruits);
            
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("查询水果列表失败", e);
            return Result.failed("查询失败");
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取水果")
    public Result<FruitVO> getFruitById(
            @ApiParam(value = "水果ID", required = true)
            @PathVariable Long id) {
        Fruit fruit = fruitService.getById(id);
        if (fruit == null) {
            return Result.failed("水果不存在");
        }
        return Result.success(convertToVO(fruit));
    }

    @PostMapping("/recommend")
    @ApiOperation("智能推荐水果")
    public Result<List<FruitVO>> recommendFruits(@RequestBody FruitRecommendDTO recommendDTO) {
        // 参数校验
        if (recommendDTO == null || StringUtils.isEmpty(recommendDTO.getCondition())) {
            return Result.failed("推荐条件不能为空");
        }

        // 验证token
        if (!StringUtils.isEmpty(recommendDTO.getToken())) {
            try {
                // 这里简化处理，使用手机号作为token
                User user = userService.getByMobile(recommendDTO.getToken());
                if (user == null) {
                    log.warn("无效的token: {}", recommendDTO.getToken());
                }
            } catch (Exception e) {
                log.warn("验证token异常", e);
            }
        }

        try {
            // 获取推荐水果
            List<Fruit> recommendList = fruitService.recommendFruits(recommendDTO);
            List<FruitVO> voList = convertToVOList(recommendList);
            return Result.success(voList);
        } catch (BusinessException e) {
            return Result.failed(e.getMessage());
        } catch (Exception e) {
            log.error("推荐水果异常", e);
            return Result.failed("推荐失败，请稍后重试");
        }
    }

    @GetMapping("/history")
    @ApiOperation("获取推荐历史")
    public Result<List<RecommendHistoryVO>> getRecommendHistory(
            @ApiParam(value = "用户token", required = true)
            @RequestParam String token,
            @ApiParam(value = "限制数量", required = false)
            @RequestParam(required = false) Integer limit) {

        // 验证token
        User user = userService.getByToken(token);
        if (user == null) {
            return Result.failed("无效的token");
        }

        // 获取历史记录
        List<RecommendHistoryVO> historyList = fruitService.getRecommendHistory(user.getId(), limit);
        return Result.success(historyList);
    }

    /**
     * 转换为VO列表
     */
    private List<FruitVO> convertToVOList(List<Fruit> fruitList) {
        return fruitList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 转换为VO
     */
    private FruitVO convertToVO(Fruit fruit) {
        FruitVO vo = new FruitVO();
        BeanUtils.copyProperties(fruit, vo);
        // 确保图片URL已经被正确处理（Service层已处理，这里作为双重保险）
        return vo;
    }
    
    @GetMapping("/recommend-list")
    @ApiOperation("获取推荐水果列表")
    public Result<List<FruitVO>> getRecommendFruits(
            @ApiParam(value = "限制数量", required = false)
            @RequestParam(required = false, defaultValue = "6") Integer limit,
            @ApiParam(value = "是否只返回推荐商品", required = false)
            @RequestParam(required = false, defaultValue = "true") Boolean isRecommended) {
        List<Fruit> recommendFruits = fruitService.getRecommendFruits(limit, isRecommended);
        List<FruitVO> voList = convertToVOList(recommendFruits);
        return Result.success(voList);
    }
    
    @GetMapping("/new-list")
    @ApiOperation("获取新品水果列表")
    public Result<List<FruitVO>> getNewFruits(
            @ApiParam(value = "限制数量", required = false)
            @RequestParam(required = false, defaultValue = "6") Integer limit) {
        List<Fruit> newFruits = fruitService.getNewFruits(limit);
        List<FruitVO> voList = convertToVOList(newFruits);
        return Result.success(voList);
    }
    
    @GetMapping("/categories")
    @ApiOperation("获取所有水果分类")
    public Result<List<CategoryVO>> getCategories() {
        List<CategoryVO> categories = fruitService.getCategories();
        return Result.success(categories);
    }
    
    @GetMapping("/list/category/{category}")
    @ApiOperation("根据分类获取水果列表")
    public Result<List<FruitVO>> getFruitsByCategory(
            @ApiParam(value = "水果分类", required = true)
            @PathVariable String category,
            @ApiParam(value = "排序字段：price(价格)、stock(库存)、createTime(时间)", required = false)
            @RequestParam(required = false, defaultValue = "createTime") String sortBy,
            @ApiParam(value = "排序方式：asc(升序)、desc(降序)", required = false)
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        List<Fruit> fruitList;
        
        // 判断传入的是分类ID还是分类名称
        try {
            Long categoryId = Long.parseLong(category);
            // 如果能解析为Long，则按分类ID查询
            fruitList = fruitService.getFruitsByCategoryIdWithSort(categoryId, sortBy, sortOrder);
        } catch (NumberFormatException e) {
            // 如果不能解析为Long，则按分类名称查询
            fruitList = fruitService.getFruitsByCategoryWithSort(category, sortBy, sortOrder);
        }
        
        List<FruitVO> voList = convertToVOList(fruitList);
        return Result.success(voList);
    }
    
    @GetMapping("/search")
    @ApiOperation("搜索水果")
    public Result<List<FruitVO>> searchFruits(
            @ApiParam(value = "搜索关键词", required = true)
            @RequestParam String keyword,
            @ApiParam(value = "排序字段：price(价格)、stock(库存)、createTime(时间)", required = false)
            @RequestParam(required = false, defaultValue = "createTime") String sortBy,
            @ApiParam(value = "排序方式：asc(升序)、desc(降序)", required = false)
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        if (StringUtils.isEmpty(keyword)) {
            return Result.failed("搜索关键词不能为空");
        }
        
        try {
            // 处理URL编码的中文字符
            keyword = URLDecoder.decode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("URL解码失败: {}", e.getMessage());
        }
        
        List<Fruit> fruitList = fruitService.searchFruitsWithSort(keyword, sortBy, sortOrder);
        List<FruitVO> voList = convertToVOList(fruitList);
        return Result.success(voList);
    }
    
    // ==================== Admin接口 ====================
    
    @ApiOperation("管理员添加商品")
    @PostMapping("/admin/add")
    public Result<String> addProduct(@Valid @RequestBody Fruit fruit) {
        try {
            fruit.setCreateTime(new java.util.Date());
            fruit.setUpdateTime(new java.util.Date());
            fruit.setStatus(1); // 默认启用
            
            int result = fruitMapper.insert(fruit);
            if (result > 0) {
                return Result.success("商品添加成功");
            } else {
                return Result.failed("商品添加失败");
            }
        } catch (Exception e) {
            log.error("添加商品失败", e);
            return Result.failed("添加商品失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("管理员更新商品")
    @PutMapping("/admin/update/{id}")
    public Result<String> updateProduct(@PathVariable Long id, @Valid @RequestBody Fruit fruit) {
        try {
            Fruit existingFruit = fruitMapper.selectById(id);
            if (existingFruit == null) {
                return Result.failed("商品不存在");
            }
            
            fruit.setId(id);
            fruit.setUpdateTime(new java.util.Date());
            
            int result = fruitMapper.update(fruit);
            if (result > 0) {
                return Result.success("商品更新成功");
            } else {
                return Result.failed("商品更新失败");
            }
        } catch (Exception e) {
            log.error("更新商品失败", e);
            return Result.failed("更新商品失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("管理员删除商品")
    @DeleteMapping("/admin/delete/{id}")
    public Result<String> deleteProduct(@PathVariable Long id) {
        try {
            int result = fruitMapper.deleteById(id);
            if (result > 0) {
                return Result.success("商品删除成功");
            } else {
                return Result.failed("商品删除失败，商品不存在");
            }
        } catch (Exception e) {
            log.error("删除商品失败", e);
            return Result.failed("删除商品失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("管理员更新商品状态")
    @PutMapping("/admin/status/{id}")
    public Result<String> toggleProductStatus(@PathVariable Long id) {
        try {
            Fruit fruit = fruitMapper.selectById(id);
            if (fruit == null) {
                return Result.failed("商品不存在");
            }
            
            // 切换状态：1-上架，0-下架
            Integer newStatus = fruit.getStatus() == 1 ? 0 : 1;
            fruit.setStatus(newStatus);
            fruit.setUpdateTime(new java.util.Date());
            
            int result = fruitMapper.update(fruit);
            if (result > 0) {
                return Result.success("商品状态更新成功");
            } else {
                return Result.failed("商品状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新商品状态失败", e);
            return Result.failed("更新商品状态失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("管理员获取商品统计数据")
    @GetMapping("/admin/statistics")
    public Result<Map<String, Object>> getProductStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 总商品数
            Long totalProducts = fruitMapper.countTotal();
            statistics.put("totalProducts", totalProducts);
            
            // 上架商品数
            Long onlineProducts = fruitMapper.countByStatus(1);
            statistics.put("onlineProducts", onlineProducts);
            
            // 下架商品数
            Long offlineProducts = fruitMapper.countByStatus(0);
            statistics.put("offlineProducts", offlineProducts);
            
            // 库存不足商品数（库存小于10）
            Long lowStockProducts = fruitMapper.countLowStock(10);
            statistics.put("lowStockProducts", lowStockProducts);
            
            // 今日新增商品数
            Long todayAdded = fruitMapper.countTodayAdded();
            statistics.put("todayAdded", todayAdded);
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取商品统计数据失败", e);
            return Result.failed("获取商品统计数据失败: " + e.getMessage());
        }
    }

    @ApiOperation("管理员设置商品推荐状态")
    @PutMapping("/admin/recommend/{id}")
    public Result<String> setRecommended(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            // 检查商品是否存在且上架
            Fruit fruit = fruitMapper.selectById(id);
            if (fruit == null) {
                return Result.failed("商品不存在");
            }
            if (fruit.getStatus() != 1) {
                return Result.failed("只有上架的商品才能设置推荐");
            }
            
            Boolean recommended = (Boolean) request.get("recommended");
            int result = fruitMapper.updateRecommended(id, recommended ? 1 : 0);
            if (result > 0) {
                return Result.success("推荐状态设置成功");
            } else {
                return Result.failed("商品不存在或推荐状态设置失败");
            }
        } catch (Exception e) {
            log.error("设置商品推荐状态失败", e);
            return Result.failed("设置商品推荐状态失败: " + e.getMessage());
        }
    }

    @ApiOperation("管理员批量设置商品推荐状态")
    @PutMapping("/admin/recommend/batch")
    public Result<String> batchSetRecommended(@RequestBody Map<String, Object> request) {
        try {
            List<Long> ids = (List<Long>) request.get("ids");
            Boolean recommended = (Boolean) request.get("recommended");
            
            // 检查所有商品是否存在且上架
            for (Long id : ids) {
                Fruit fruit = fruitMapper.selectById(id);
                if (fruit == null) {
                    return Result.failed("商品ID " + id + " 不存在");
                }
                if (fruit.getStatus() != 1) {
                    return Result.failed("商品 " + fruit.getName() + " 未上架，只有上架的商品才能设置推荐");
                }
            }
            
            int result = fruitMapper.batchUpdateRecommended(ids, recommended ? 1 : 0);
            if (result > 0) {
                return Result.success("批量设置推荐状态成功");
            } else {
                return Result.failed("批量设置推荐状态失败");
            }
        } catch (Exception e) {
            log.error("批量设置商品推荐状态失败", e);
            return Result.failed("批量设置商品推荐状态失败: " + e.getMessage());
        }
    }

    @ApiOperation("管理员设置商品限时特惠")
    @PostMapping("/admin/flash-sale/{id}")
    public Result<String> setFlashSale(@PathVariable Long id, @RequestBody Map<String, Object> flashSaleData) {
        try {
            // 检查商品是否存在且上架
            Fruit fruit = fruitMapper.selectById(id);
            if (fruit == null) {
                return Result.failed("商品不存在");
            }
            if (fruit.getStatus() != 1) {
                return Result.failed("只有上架的商品才能设置限时特惠");
            }

            // 先删除已有的限时特惠
            fruitMapper.deleteFlashSaleByFruitId(id);

            // 创建新的限时特惠
             FlashSale flashSale = new FlashSale();
             flashSale.setFruitId(id);
             flashSale.setOriginalPrice(fruit.getPrice());
             flashSale.setSalePrice(new java.math.BigDecimal(flashSaleData.get("salePrice").toString()));
             flashSale.setStartTime(java.sql.Timestamp.valueOf(flashSaleData.get("startTime").toString()));
             flashSale.setEndTime(java.sql.Timestamp.valueOf(flashSaleData.get("endTime").toString()));
             flashSale.setStock((Integer) flashSaleData.get("stock"));
             flashSale.setSoldCount(0);
             flashSale.setStatus(1); // 启用状态
             flashSale.setCreateTime(new java.util.Date());
             flashSale.setUpdateTime(new java.util.Date());

             int result = fruitMapper.insertFlashSale(id, flashSale);
            if (result > 0) {
                return Result.success("限时特惠设置成功");
            } else {
                return Result.failed("限时特惠设置失败");
            }
        } catch (Exception e) {
            log.error("设置商品限时特惠失败", e);
            return Result.failed("设置商品限时特惠失败: " + e.getMessage());
        }
    }

    @ApiOperation("管理员取消商品限时特惠")
    @DeleteMapping("/admin/flash-sale/{id}")
    public Result<String> cancelFlashSale(@PathVariable Long id) {
        try {
            int result = fruitMapper.deleteFlashSaleByFruitId(id);
            if (result > 0) {
                return Result.success("取消限时特惠成功");
            } else {
                return Result.success("该商品没有设置限时特惠");
            }
        } catch (Exception e) {
            log.error("取消商品限时特惠失败", e);
            return Result.failed("取消商品限时特惠失败: " + e.getMessage());
        }
    }
}