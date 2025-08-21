package org.example.admin.controller;

import org.example.admin.service.AdminBannerService;

import org.example.common.entity.Banner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController("adminBannerController")
@RequestMapping("/admin/banner")
@CrossOrigin(origins = "*")
public class BannerController {

    @Autowired
    private AdminBannerService adminBannerService;



    /**
     * 获取轮播图列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getBanners(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String search) {
        
        try {
            Map<String, Object> result = adminBannerService.getBannerList(page, size, search);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "获取轮播图列表成功");
            response.put("data", result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "获取轮播图列表失败: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 获取轮播图详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBannerDetail(@PathVariable Long id) {
        try {
            Banner banner = adminBannerService.getBannerById(id);
            
            Map<String, Object> response = new HashMap<>();
            if (banner != null) {
                response.put("code", 200);
                response.put("message", "获取轮播图详情成功");
                response.put("data", banner);
            } else {
                response.put("code", 404);
                response.put("message", "轮播图不存在");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "获取轮播图详情失败: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 新增轮播图
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addBanner(@RequestBody Banner banner) {
        try {
            boolean success = adminBannerService.addBanner(banner);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("code", 200);
                response.put("message", "轮播图添加成功");
                response.put("data", banner);
            } else {
                response.put("code", 500);
                response.put("message", "轮播图添加失败");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "轮播图添加失败: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 更新轮播图
     */
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateBanner(@RequestBody Banner banner) {
        try {
            boolean success = adminBannerService.updateBanner(banner);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("code", 200);
                response.put("message", "轮播图更新成功");
                response.put("data", banner);
            } else {
                response.put("code", 404);
                response.put("message", "轮播图不存在或更新失败");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "轮播图更新失败: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 删除轮播图
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteBanner(@PathVariable Long id) {
        try {
            boolean success = adminBannerService.deleteBanner(id);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("code", 200);
                response.put("message", "轮播图删除成功");
            } else {
                response.put("code", 404);
                response.put("message", "轮播图不存在或删除失败");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "轮播图删除失败: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 批量删除轮播图
     */
    @DeleteMapping("/batch-delete")
    public ResponseEntity<Map<String, Object>> batchDeleteBanners(@RequestBody List<Long> ids) {
        try {
            int deletedCount = adminBannerService.batchDeleteBanners(ids);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "批量删除成功，共删除 " + deletedCount + " 个轮播图");
            response.put("data", deletedCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "批量删除失败: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 切换轮播图状态
     */
    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<Map<String, Object>> toggleBannerStatus(@PathVariable Long id) {
        try {
            boolean success = adminBannerService.toggleBannerStatus(id);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                Banner banner = adminBannerService.getBannerById(id);
                response.put("code", 200);
                response.put("message", "状态切换成功");
                response.put("data", banner);
            } else {
                response.put("code", 404);
                response.put("message", "轮播图不存在或状态切换失败");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "状态切换失败: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 批量切换轮播图状态
     */
    @PutMapping("/batch-toggle-status")
    public ResponseEntity<Map<String, Object>> batchToggleBannerStatus(@RequestBody List<Long> ids) {
        try {
            int updatedCount = adminBannerService.batchToggleBannerStatus(ids);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "批量状态切换成功，共更新 " + updatedCount + " 个轮播图");
            response.put("data", updatedCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "批量状态切换失败: " + e.getMessage());
            
            return ResponseEntity.ok(response);
        }
    }


}