package com.hibob.anylink.mts.config;

import com.hibob.anylink.common.interceptor.PreHeaderInterceptor;
import com.hibob.anylink.common.interceptor.XssInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

    private final XssInterceptor xssInterceptor;
    private final PreHeaderInterceptor preHeaderInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(xssInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error");
        registry.addInterceptor(preHeaderInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/error",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/**",
                        "/api/anylink-mts/healthcheck",
                        "/swagger-ui.html/**"
                );
    }

}
