package org.example.admin.controller;

import org.example.admin.service.AdminProductService;

import org.example.admin.vo.CategoryVo;
import org.example.common.entity.Fruit;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 商品管理控制器
 */
@RestController
@RequestMapping("/admin/fruit")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private AdminProductService adminProductService;
    


    /**
     * 获取商品列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getProductList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        try {
            Map<String, Object> result = adminProductService.getProductList(page, size, search, category, status);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed("获取商品列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/{id}")
    public Result<Fruit> getProduct(@PathVariable Long id) {
        try {
            Fruit fruit = adminProductService.getProductById(id);
            if (fruit != null) {
                return Result.success(fruit);
            } else {
                return Result.failed("商品不存在");
            }
        } catch (Exception e) {
            return Result.failed("获取商品详情失败: " + e.getMessage());
        }
    }

    /**
     * 设置商品推荐状态
     */
    @PutMapping("/recommend/{id}")
    public Result<String> setRecommended(@PathVariable Long id, @RequestParam Boolean recommended) {
        try {
            adminProductService.setRecommended(id, recommended);
            return Result.success("推荐状态设置成功");
        } catch (Exception e) {
            return Result.failed("设置推荐状态失败: " + e.getMessage());
        }
    }

    /**
     * 批量设置商品推荐状态
     */
    @PutMapping("/recommend/batch")
    public Result<String> batchSetRecommended(@RequestBody Map<String, Object> request) {
        try {
            List<Long> ids = (List<Long>) request.get("ids");
            Boolean recommended = (Boolean) request.get("recommended");
            adminProductService.batchSetRecommended(ids, recommended);
            return Result.success("批量设置推荐状态成功");
        } catch (Exception e) {
            return Result.failed("批量设置推荐状态失败: " + e.getMessage());
        }
    }

    /**
     * 设置商品限时特惠
     */
    @PostMapping("/flash-sale/{id}")
    public Result<String> setFlashSale(@PathVariable Long id, @RequestBody Map<String, Object> flashSaleData) {
        try {
            adminProductService.setFlashSale(id, flashSaleData);
            return Result.success("限时特惠设置成功");
        } catch (Exception e) {
            return Result.failed("设置限时特惠失败: " + e.getMessage());
        }
    }

    /**
     * 取消商品限时特惠
     */
    @DeleteMapping("/flash-sale/{id}")
    public Result<String> cancelFlashSale(@PathVariable Long id) {
        try {
            adminProductService.cancelFlashSale(id);
            return Result.success("取消限时特惠成功");
        } catch (Exception e) {
            return Result.failed("取消限时特惠失败: " + e.getMessage());
        }
    }

    /**
     * 新增商品
     */
    @PostMapping("/add")
    public Result<String> addProduct(@RequestBody Fruit fruit) {
        try {
            adminProductService.addProduct(fruit);
            return Result.success("商品添加成功");
        } catch (Exception e) {
            return Result.failed("添加商品失败: " + e.getMessage());
        }
    }

    /**
     * 更新商品
     */
    @PutMapping("/update")
    public Result<String> updateProduct(@RequestBody Fruit fruit) {
        try {
            adminProductService.updateProduct(fruit);
            return Result.success("商品更新成功");
        } catch (Exception e) {
            return Result.failed("更新商品失败: " + e.getMessage());
        }
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteProduct(@PathVariable Long id) {
        try {
            adminProductService.deleteProduct(id);
            return Result.success("商品删除成功");
        } catch (Exception e) {
            return Result.failed("删除商品失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除商品
     */
    @DeleteMapping("/batch-delete")
    public Result<String> batchDeleteProducts(@RequestBody List<Long> ids) {
        try {
            adminProductService.batchDeleteProducts(ids);
            return Result.success("批量删除成功");
        } catch (Exception e) {
            return Result.failed("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 切换商品状态（上架/下架）
     */
    @PutMapping("/toggle-status/{id}")
    public Result<String> toggleProductStatus(@PathVariable Long id) {
        try {
            adminProductService.toggleProductStatus(id);
            return Result.success("状态切换成功");
        } catch (Exception e) {
            return Result.failed("状态切换失败: " + e.getMessage());
        }
    }

    /**
     * 批量切换商品状态
     */
    @PutMapping("/batch-toggle-status")
    public Result<String> batchToggleStatus(@RequestBody List<Long> ids) {
        try {
            adminProductService.batchToggleStatus(ids);
            return Result.success("批量状态切换成功");
        } catch (Exception e) {
            return Result.failed("批量状态切换失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品分类列表
     */
    @GetMapping("/categories")
    public Result<List<CategoryVo>> getCategories() {
        try {
            List<CategoryVo> categories = adminProductService.getCategories();
            return Result.success(categories);
        } catch (Exception e) {
            return Result.failed("获取分类列表失败: " + e.getMessage());
        }
    }
    

}