package org.example.imageservice.service;

import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.imageservice.config.ImageUploadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片服务
 */
@Service
public class ImageService {
    
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    
    @Autowired
    private ImageUploadConfig uploadConfig;
    
    @Autowired
    private MinioService minioService;
    
    /**
     * 上传图片
     */
    public Map<String, Object> uploadImage(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            logger.debug("开始处理图片上传请求");
            
            // 验证文件
            String validationError = validateFile(file);
            if (validationError != null) {
                result.put("success", false);
                result.put("message", validationError);
                logger.warn("文件验证失败: {}", validationError);
                return result;
            }
            
            // 上传到MinIO
            logger.debug("文件验证通过，开始上传到MinIO");
            String fileName = minioService.uploadFile(file);
            
            // 生成访问URL
            String url = "/api/images/" + fileName;
            
            result.put("success", true);
            result.put("fileName", fileName);
            result.put("url", url);
            result.put("message", "图片上传成功");
            
            logger.info("图片上传成功，文件名: {}", fileName);
            
        } catch (InvalidKeyException | InvalidResponseException | InsufficientDataException |
                NoSuchAlgorithmException | ServerException | InternalException |
                XmlParserException | ErrorResponseException e) {
            logger.error("MinIO相关错误: {}", e.getMessage(), e);
            String errorMsg = "文件上传失败: " + e.getMessage();
            // 检查是否是端口配置错误
            if (errorMsg.contains("API port")) {
                errorMsg = "MinIO服务器配置错误，请检查MinIO服务是否在正确的端口运行，通常API端口是9000";
            }
            result.put("success", false);
            result.put("message", errorMsg);
        } catch (IOException e) {
            logger.error("IO错误: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "文件上传失败: " + e.getMessage());
        } catch (RuntimeException e) {
            logger.error("运行时错误: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            logger.error("上传过程中发生未知错误: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "文件上传失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 删除图片
     */
    public Map<String, Object> deleteImage(String fileName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            logger.debug("开始删除图片: {}", fileName);
            minioService.deleteFile(fileName);
            result.put("success", true);
            result.put("message", "图片删除成功");
            logger.info("图片删除成功: {}", fileName);
        } catch (Exception e) {
            logger.error("删除图片时发生错误: {}", e.getMessage(), e);
            String errorMsg = "删除图片时发生错误: " + e.getMessage();
            // 检查是否是端口配置错误
            if (errorMsg.contains("API port")) {
                errorMsg = "MinIO服务器配置错误，请检查MinIO服务是否在正确的端口运行，通常API端口是9000";
            }
            result.put("success", false);
            result.put("message", errorMsg);
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