package org.example.admin.config;

import org.example.common.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration("adminWebConfig")
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:/uploads/}")
    private String uploadPath;
    
    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加日志拦截器，拦截所有admin路径
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/health", "/admin/actuator/**", "/admin/druid/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置上传文件静态资源访问路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
        
        // 配置图标静态资源访问路径
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
        
        // 配置默认静态资源
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}