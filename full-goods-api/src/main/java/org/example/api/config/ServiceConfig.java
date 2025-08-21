package org.example.api.config;

import org.example.api.service.AddressService;
import org.example.api.service.impl.AddressServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 服务配置类
 */
@Configuration
public class ServiceConfig {

    @Bean
    public AddressService addressService() {
        return new AddressServiceImpl();
    }
}