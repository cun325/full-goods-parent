package org.example.imageservice.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
// 移除有问题的GetObjectResponse导入，因为它会在方法返回类型中直接使用
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class MinioService {
    
    private static final Logger logger = LoggerFactory.getLogger(MinioService.class);
    
    @Autowired
    private MinioClient minioClient;
    
    @Value("${minio.bucket-name}")
    private String bucketName;
    
    /**
     * 上传文件
     */
    public String uploadFile(MultipartFile file) throws IOException, InvalidKeyException, InvalidResponseException,
            InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException,
            XmlParserException, ErrorResponseException {
        
        logger.debug("开始上传文件到MinIO: {}", file.getOriginalFilename());
        
        try {
            // 检查bucket是否存在，不存在则创建
            logger.debug("检查bucket是否存在: {}", bucketName);
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                logger.debug("创建bucket: {}", bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            logger.error("检查或创建bucket时出错: {}", e.getMessage(), e);
            // 如果是MinIO服务器端口错误，给出更明确的提示
            if (e.getMessage() != null && e.getMessage().contains("API port")) {
                throw new RuntimeException("MinIO服务器端口配置错误，请检查MinIO服务是否在正确的端口运行", e);
            }
            throw e;
        }
        
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;
        logger.debug("生成文件名: {}", fileName);
        
        // 上传文件
        try (InputStream inputStream = file.getInputStream()) {
            logger.debug("开始上传文件流，大小: {} bytes", file.getSize());
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            logger.debug("文件上传成功: {}", fileName);
        } catch (Exception e) {
            logger.error("上传文件到MinIO时出错: {}", e.getMessage(), e);
            // 如果是MinIO服务器端口错误，给出更明确的提示
            if (e.getMessage() != null && e.getMessage().contains("API port")) {
                throw new RuntimeException("MinIO服务器端口配置错误，请检查MinIO服务是否在正确的端口运行", e);
            }
            throw e;
        }
        
        return fileName;
    }
    
    /**
     * 删除文件
     */
    public void deleteFile(String fileName) throws IOException, InvalidKeyException, InvalidResponseException,
            InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException,
            XmlParserException, ErrorResponseException {
        
        logger.debug("从MinIO删除文件: {}", fileName);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            logger.debug("文件删除成功: {}", fileName);
        } catch (Exception e) {
            logger.error("删除MinIO文件时出错: {}", e.getMessage(), e);
            // 如果是MinIO服务器端口错误，给出更明确的提示
            if (e.getMessage() != null && e.getMessage().contains("API port")) {
                throw new RuntimeException("MinIO服务器端口配置错误，请检查MinIO服务是否在正确的端口运行", e);
            }
            throw e;
        }
    }
    
    /**
     * 获取文件访问URL
     */
    public String getFileUrl(String fileName) throws IOException, InvalidKeyException, InvalidResponseException,
            InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException,
            XmlParserException, ErrorResponseException {
        
        logger.debug("获取文件预签名URL: {}", fileName);
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(7 * 24 * 60 * 60) // 7天过期
                            .build()
            );
            logger.debug("获取预签名URL成功: {}", url);
            return url;
        } catch (Exception e) {
            logger.error("获取预签名URL时出错: {}", e.getMessage(), e);
            // 如果是MinIO服务器端口错误，给出更明确的提示
            if (e.getMessage() != null && e.getMessage().contains("API port")) {
                throw new RuntimeException("MinIO服务器端口配置错误，请检查MinIO服务是否在正确的端口运行", e);
            }
            throw e;
        }
    }
    
    /**
     * 获取文件输入流
     */
    public InputStream getObject(String fileName) throws IOException, InvalidKeyException, InvalidResponseException,
            InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException,
            XmlParserException, ErrorResponseException {
        
        logger.debug("从MinIO获取文件: {}", fileName);
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            logger.debug("获取文件成功: {}", fileName);
            return response;
        } catch (Exception e) {
            logger.error("从MinIO获取文件时出错: {}", e.getMessage(), e);
            // 如果是MinIO服务器端口错误，给出更明确的提示
            if (e.getMessage() != null && e.getMessage().contains("API port")) {
                throw new RuntimeException("MinIO服务器端口配置错误，请检查MinIO服务是否在正确的端口运行", e);
            }
            throw e;
        }
    }
}