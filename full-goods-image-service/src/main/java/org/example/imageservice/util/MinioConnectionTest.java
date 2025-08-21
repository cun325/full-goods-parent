package org.example.imageservice.util;

import io.minio.MinioClient;
import io.minio.BucketExistsArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MinioConnectionTest {
    
    private static final Logger logger = LoggerFactory.getLogger(MinioConnectionTest.class);
    
    @Value("${minio.endpoint}")
    private String endpoint;
    
    @Value("${minio.access-key}")
    private String accessKey;
    
    @Value("${minio.secret-key}")
    private String secretKey;
    
    @Value("${minio.bucket-name}")
    private String bucketName;
    
    @PostConstruct
    public void testConnection() {
        logger.info("开始测试MinIO连接...");
        logger.info("Endpoint: {}", endpoint);
        logger.info("Access Key: {}", accessKey);
        logger.info("Bucket Name: {}", bucketName);
        
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (exists) {
                logger.info("MinIO连接测试成功！Bucket '{}' 存在。", bucketName);
            } else {
                logger.info("MinIO连接测试成功！Bucket '{}' 不存在，但连接正常。", bucketName);
            }
        } catch (Exception e) {
            logger.error("MinIO连接测试失败: {}", e.getMessage(), e);
        }
    }
}