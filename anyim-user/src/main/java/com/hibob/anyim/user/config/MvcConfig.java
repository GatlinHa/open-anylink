package com.hibob.anyim.user.config;

import com.hibob.anyim.user.interceptor.PreHeaderInterceptor;
import com.hibob.anyim.common.interceptor.XssInterceptor;
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
                        "/user/login",
                        "/user/register",
                        "/user/validateAccount",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/**",
                        "/swagger-ui.html/**"
                );
    }

}
