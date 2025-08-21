package org.example.common.config;

import org.example.common.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.ComponentScan;

/**
 * 安全配置
 * 配置请求安全相关设置
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    /**
     * 支持HTTP方法覆盖
     * 允许通过POST请求模拟PUT、DELETE等方法
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加日志拦截器
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/health", "/api/actuator/**");
        
        // 可以在这里添加其他安全拦截器
        // registry.addInterceptor(new SecurityInterceptor())
        //         .addPathPatterns("/api/**")
        //         .excludePathPatterns("/api/auth/login", "/api/auth/register");
    }
}