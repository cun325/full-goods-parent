package org.example.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * API服务启动类
 */
@SpringBootApplication(scanBasePackages = {"org.example.api", "org.example.common"})
@MapperScan(basePackages = {"org.example.api.mapper", "org.example.api.mapper"})
@EnableScheduling
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}