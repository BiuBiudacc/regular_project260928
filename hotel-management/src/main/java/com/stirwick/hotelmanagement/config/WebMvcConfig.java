package com.stirwick.hotelmanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * SpringMVC配置类
 * @Configuration：代表Spring配置类
 * implements WebMvcConfigurer：MVC配置接口，可以注册拦截器、跨域等
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 注入我们写好的拦截器
    @Autowired
    private AuthInterceptor authInterceptor;

    /**
     * 添加拦截器到MVC拦截链
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**"); // /** 拦截全部请求路径
        // 白名单我写在拦截器内部代码判断uri；也可以在这里 .excludePathPatterns("/api/user/login")
    }
}
