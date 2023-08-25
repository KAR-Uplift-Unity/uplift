package com.KARUpliftUnity.config;

import com.KARUpliftUnity.interceptors.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .excludePathPatterns("/", "/posts", "/posts/*", "/signup", "/login", "/contact_us", "/mission","/css/**", "/js/**", "/images/**", "/feed", "/posts/search", "/posts/category/*");
    }
}

