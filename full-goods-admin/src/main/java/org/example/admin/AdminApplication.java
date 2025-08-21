package org.example.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 管理后台启动类
 */
@SpringBootApplication(scanBasePackages = {"org.example.admin", "org.example.common", "org.example.api.service"})
@MapperScan(basePackages = {"org.example.admin.mapper", "org.example.admin.mapper"})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}