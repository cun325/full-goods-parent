package org.example.imageservice.controller;

import io.minio.errors.*;
import org.example.imageservice.service.ImageService;
import org.example.imageservice.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片服务控制器
 */
@RestController
@RequestMapping("/api")
public class ImageController {
    
    @Autowired
    private ImageService imageService;
    
    @Autowired
    private MinioService minioService;
    
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
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestParam("fileName") String fileName) {
        Map<String, Object> result = imageService.deleteImage(fileName);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 访问图片 - 直接从MinIO获取并返回图片内容
     */
    @GetMapping(value = "/images/{fileName}")
    public void getImage(@PathVariable String fileName, HttpServletResponse response) {
        InputStream inputStream = null;
        try {
            // 从MinIO获取文件输入流
            inputStream = minioService.getObject(fileName);
            
            // 设置响应头
            response.setContentType("image/jpeg"); // 默认类型
            response.setHeader("Cache-Control", "max-age=3600"); // 缓存1小时
            
            // 将图片内容写入响应
            java.io.OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            try {
                response.getWriter().write("Image not found: " + e.getMessage());
            } catch (IOException ioException) {
                // 忽略
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // 忽略
                }
            }
        }
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