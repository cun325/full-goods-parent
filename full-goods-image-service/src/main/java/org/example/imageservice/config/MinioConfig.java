package org.example.imageservice.config;

import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(MinioConfig.class);
    
    @Value("${minio.endpoint}")
    private String endpoint;
    
    @Value("${minio.access-key}")
    private String accessKey;
    
    @Value("${minio.secret-key}")
    private String secretKey;
    
    @Bean
    public MinioClient minioClient() {
        logger.info("初始化MinIO客户端，endpoint: {}, accessKey: {}", endpoint, accessKey);
        try {
            MinioClient client = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            logger.info("MinIO客户端初始化成功");
            return client;
        } catch (Exception e) {
            logger.error("MinIO客户端初始化失败: {}", e.getMessage(), e);
            logger.error("请检查以下配置是否正确：");
            logger.error("1. MinIO服务器是否正在运行");
            logger.error("2. MinIO服务器地址是否正确: {}", endpoint);
            logger.error("3. 访问密钥是否正确: {}", accessKey);
            logger.error("4. MinIO服务器API端口是否正确（通常是9000）");
            throw new RuntimeException("MinIO客户端初始化失败: " + e.getMessage(), e);
        }
    }
}