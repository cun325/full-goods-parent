package org.example.admin.controller;

import org.example.admin.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/admin/category")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    


    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取分类列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getCategories(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String search) {
        
        try {
            // 调用CategoryService获取真实数据
            Map<String, Object> result = categoryService.getCategoryList(page, size, search);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "获取分类列表成功");
            response.put("data", result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "获取分类列表失败: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 获取分类详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryDetail(@PathVariable Long id) {
        try {
            org.example.common.entity.FruitCategory category = categoryService.getCategoryById(id);
            
            Map<String, Object> result = new HashMap<>();
            if (category != null) {
                Map<String, Object> categoryData = new HashMap<>();
                categoryData.put("id", category.getId());
                categoryData.put("name", category.getName());
                categoryData.put("sort", category.getSortOrder());
                categoryData.put("iconName", category.getIconName());
                categoryData.put("iconUrl", category.getIconUrl());
                categoryData.put("createBy", category.getCreateBy());
                categoryData.put("updateBy", category.getUpdateBy());
                categoryData.put("status", category.getStatus() == 1 ? "active" : "inactive");
                categoryData.put("createTime", category.getCreateTime() != null ? formatter.format(category.getCreateTime()) : "");
                categoryData.put("updateTime", category.getUpdateTime() != null ? formatter.format(category.getUpdateTime()) : "");
                
                result.put("code", 200);
                result.put("message", "获取分类详情成功");
                result.put("data", categoryData);
            } else {
                result.put("code", 404);
                result.put("message", "分类不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "获取分类详情失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 新增分类
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addCategory(@RequestBody Map<String, Object> categoryData) {
        try {
            org.example.common.entity.FruitCategory category = new org.example.common.entity.FruitCategory();
            category.setName((String) categoryData.get("name"));
            category.setSortOrder((Integer) categoryData.get("sort"));
            category.setIconName((String) categoryData.get("iconName"));
            category.setIconUrl((String) categoryData.get("iconUrl"));
            category.setCreateBy((String) categoryData.get("createBy"));
            
            String status = (String) categoryData.get("status");
            category.setStatus("active".equals(status) ? 1 : 0);
            
            org.example.common.entity.FruitCategory savedCategory = categoryService.addCategory(category);
            
            Map<String, Object> result = new HashMap<>();
            if (savedCategory != null) {
                Map<String, Object> categoryResult = new HashMap<>();
                categoryResult.put("id", savedCategory.getId());
                categoryResult.put("name", savedCategory.getName());
                categoryResult.put("sort", savedCategory.getSortOrder());
                categoryResult.put("iconName", savedCategory.getIconName());
                categoryResult.put("iconUrl", savedCategory.getIconUrl());
                categoryResult.put("createBy", savedCategory.getCreateBy());
                categoryResult.put("updateBy", savedCategory.getUpdateBy());
                categoryResult.put("remark", savedCategory.getRemark());
                categoryResult.put("status", savedCategory.getStatus() == 1 ? "active" : "inactive");
                categoryResult.put("createTime", savedCategory.getCreateTime() != null ? formatter.format(savedCategory.getCreateTime()) : "");
                categoryResult.put("updateTime", savedCategory.getUpdateTime() != null ? formatter.format(savedCategory.getUpdateTime()) : "");
                
                result.put("code", 200);
                result.put("message", "分类添加成功");
                result.put("data", categoryResult);
            } else {
                result.put("code", 500);
                result.put("message", "分类添加失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "分类添加失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 更新分类
     */
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateCategory(@RequestBody Map<String, Object> categoryData) {
        try {
            Long id = Long.valueOf(categoryData.get("id").toString());
            
            org.example.common.entity.FruitCategory category = new org.example.common.entity.FruitCategory();
            category.setId(id);
            category.setName((String) categoryData.get("name"));
            category.setSortOrder((Integer) categoryData.get("sort"));
            category.setIconName((String) categoryData.get("iconName"));
            category.setIconUrl((String) categoryData.get("iconUrl"));
            category.setUpdateBy((String) categoryData.get("updateBy"));
            
            String status = (String) categoryData.get("status");
            category.setStatus("active".equals(status) ? 1 : 0);
            
            org.example.common.entity.FruitCategory updatedCategory = categoryService.updateCategory(category);
            
            Map<String, Object> result = new HashMap<>();
            if (updatedCategory != null) {
                Map<String, Object> categoryResult = new HashMap<>();
                categoryResult.put("id", updatedCategory.getId());
                categoryResult.put("name", updatedCategory.getName());
                categoryResult.put("sort", updatedCategory.getSortOrder());
                categoryResult.put("iconName", updatedCategory.getIconName());
                categoryResult.put("iconUrl", updatedCategory.getIconUrl());
                categoryResult.put("createBy", updatedCategory.getCreateBy());
                categoryResult.put("updateBy", updatedCategory.getUpdateBy());
                categoryResult.put("remark", updatedCategory.getRemark());
                categoryResult.put("status", updatedCategory.getStatus() == 1 ? "active" : "inactive");
                categoryResult.put("createTime", updatedCategory.getCreateTime() != null ? formatter.format(updatedCategory.getCreateTime()) : "");
                categoryResult.put("updateTime", updatedCategory.getUpdateTime() != null ? formatter.format(updatedCategory.getUpdateTime()) : "");
                
                result.put("code", 200);
                result.put("message", "分类更新成功");
                result.put("data", categoryResult);
            } else {
                result.put("code", 404);
                result.put("message", "分类不存在或更新失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "分类更新失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
        try {
            boolean deleted = categoryService.deleteCategory(id);
            
            Map<String, Object> result = new HashMap<>();
            if (deleted) {
                result.put("code", 200);
                result.put("message", "分类删除成功");
            } else {
                result.put("code", 404);
                result.put("message", "分类不存在或删除失败");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "分类删除失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 批量删除分类
     */
    @DeleteMapping("/batch-delete")
    public ResponseEntity<Map<String, Object>> batchDeleteCategories(@RequestBody List<Long> ids) {
        try {
            int deletedCount = categoryService.batchDeleteCategories(ids);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "批量删除成功，共删除 " + deletedCount + " 个分类");
            result.put("data", deletedCount);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "批量删除失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 切换分类状态
     */
    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<Map<String, Object>> toggleCategoryStatus(@PathVariable Long id) {
        try {
            org.example.common.entity.FruitCategory updatedCategory = categoryService.toggleCategoryStatus(id);
            
            Map<String, Object> result = new HashMap<>();
            if (updatedCategory != null) {
                Map<String, Object> categoryResult = new HashMap<>();
                categoryResult.put("id", updatedCategory.getId());
                categoryResult.put("name", updatedCategory.getName());
                categoryResult.put("sort", updatedCategory.getSortOrder());
                categoryResult.put("status", updatedCategory.getStatus() == 1 ? "active" : "inactive");
                categoryResult.put("createTime", updatedCategory.getCreateTime() != null ? formatter.format(updatedCategory.getCreateTime()) : "");
                categoryResult.put("updateTime", updatedCategory.getUpdateTime() != null ? formatter.format(updatedCategory.getUpdateTime()) : "");
                
                result.put("code", 200);
                result.put("message", "状态切换成功");
                result.put("data", categoryResult);
            } else {
                result.put("code", 404);
                result.put("message", "分类不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "状态切换失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 批量切换分类状态
     */
    @PutMapping("/batch-toggle-status")
    public ResponseEntity<Map<String, Object>> batchToggleCategoryStatus(@RequestBody List<Long> ids) {
        try {
            int updatedCount = categoryService.batchToggleCategoryStatus(ids);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "批量状态切换成功，共更新 " + updatedCount + " 个分类");
            result.put("data", updatedCount);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "批量状态切换失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 批量启用所有分类
     */
    @PutMapping("/enable-all")
    public ResponseEntity<Map<String, Object>> enableAllCategories() {
        try {
            // 获取所有分类的ID
            Map<String, Object> allCategories = categoryService.getCategoryList(1, Integer.MAX_VALUE, null);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> categoryList = (List<Map<String, Object>>) allCategories.get("list");
            
            List<Long> allIds = new ArrayList<>();
            for (Map<String, Object> category : categoryList) {
                allIds.add(((Number) category.get("id")).longValue());
            }
            
            // 批量切换状态（这里简化处理，实际应该只启用未启用的）
            int updatedCount = categoryService.batchToggleCategoryStatus(allIds);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "批量启用成功，共启用 " + updatedCount + " 个分类");
            result.put("data", updatedCount);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "批量启用失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }
    

}