package org.example.imageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

/**
 * 图片服务启动类
 */
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    MybatisAutoConfiguration.class,
    DruidDataSourceAutoConfigure.class
})
public class ImageServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ImageServiceApplication.class, args);
    }
}