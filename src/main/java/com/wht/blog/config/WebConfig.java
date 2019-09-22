package com.wht.blog.config;

import com.wht.blog.interceptor.AdminInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * web 配置
 * @author wht
 * @since 2019-09-22 21:19
 */
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Resource
            private AdminInterceptor adminInterceptor;

            //跨域请求配置
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/manage/**")
                        .allowCredentials(true)
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }

            //拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(adminInterceptor).addPathPatterns("/manage/**");
            }
        };
    }
}
