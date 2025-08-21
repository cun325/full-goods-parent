package org.example.imageservice.controller;

import org.example.imageservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片服务控制器
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ImageController {
    
    @Autowired
    private ImageService imageService;
    
    /**
     * 上传图片
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = imageService.uploadImage(file);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 删除图片
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestParam("path") String relativePath) {
        Map<String, Object> result = imageService.deleteImage(relativePath);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("service", "image-service");
        healthInfo.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(healthInfo);
    }

}