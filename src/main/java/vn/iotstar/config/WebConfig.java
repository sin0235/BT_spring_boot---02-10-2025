package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import vn.iotstar.interceptor.AuthorizationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // Đăng ký interceptor cho tất cả các URL trừ các URL tĩnh và authentication
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/auth/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/static/**",
                    "/uploads/**",
                    "/favicon.ico"
                );
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Handle static resources
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        // Handle uploaded files
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        
        // Handle images
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        // Fallback: allow serving a default image from project root in case it's placed there
        // so developers can drop default_image.png at the repo root and it will be served.
        registry.addResourceHandler("/images/default_image.png")
                .addResourceLocations("file:default_image.png", "classpath:/static/images/default_image.png");
    }
}