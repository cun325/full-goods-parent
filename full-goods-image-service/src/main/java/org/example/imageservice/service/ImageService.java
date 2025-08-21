package org.example.imageservice.service;

import org.example.imageservice.config.ImageUploadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 图片服务
 */
@Service
public class ImageService {
    
    @Autowired
    private ImageUploadConfig uploadConfig;
    
    /**
     * 上传图片
     */
    public Map<String, Object> uploadImage(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证文件
            String validationError = validateFile(file);
            if (validationError != null) {
                result.put("success", false);
                result.put("message", validationError);
                return result;
            }
            
            // 生成文件名
            String fileName = generateFileName(file.getOriginalFilename());
            
            // 创建目录结构
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String uploadPath = uploadConfig.getPath() + datePath + "/";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 保存文件
            File targetFile = new File(uploadDir, fileName);
            file.transferTo(targetFile);
            
            // 生成访问URL
            String relativePath = datePath + "/" + fileName;
            String url = uploadConfig.getUrlPrefix() + relativePath;
            String fullUrl = "http://localhost:8083/image-service" + url;
            
            result.put("success", true);
            result.put("fileName", fileName);
            result.put("url", url);
            result.put("fullUrl", fullUrl);
            result.put("relativePath", relativePath);
            result.put("message", "图片上传成功");
            
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "文件保存失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 删除图片
     */
    public Map<String, Object> deleteImage(String relativePath) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            File file = new File(uploadConfig.getPath() + relativePath);
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                if (deleted) {
                    result.put("success", true);
                    result.put("message", "图片删除成功");
                } else {
                    result.put("success", false);
                    result.put("message", "图片删除失败");
                }
            } else {
                result.put("success", false);
                result.put("message", "图片文件不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除图片时发生错误: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 验证文件
     */
    private String validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "请选择要上传的文件";
        }
        
        // 检查文件大小
        long maxSize = uploadConfig.getMaxSize() * 1024 * 1024; // MB转字节
        if (file.getSize() > maxSize) {
            return "文件大小不能超过" + uploadConfig.getMaxSize() + "MB";
        }
        
        // 检查文件类型
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            return "文件名不能为空";
        }
        
        String fileExtension = getFileExtension(fileName).toLowerCase();
        String[] allowedTypes = uploadConfig.getAllowedTypes().split(",");
        boolean isAllowed = Arrays.stream(allowedTypes)
                .anyMatch(type -> type.trim().toLowerCase().equals(fileExtension));
        
        if (!isAllowed) {
            return "不支持的文件类型，仅支持: " + uploadConfig.getAllowedTypes();
        }
        
        return null;
    }
    
    /**
     * 生成唯一文件名
     */
    private String generateFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + extension;
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
}