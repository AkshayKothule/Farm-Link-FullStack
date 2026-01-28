package com.farmlink.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.farmlink.logging.ApiLoggingInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ApiLoggingInterceptor interceptor;

    public WebConfig(ApiLoggingInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**");
    }
}
